package com.yasobafinibus.nnmtc.demonstration.controller;


import com.yasobafinibus.nnmtc.demonstration.domain.*;
import com.yasobafinibus.nnmtc.demonstration.repository.AbstractRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.GradeBookRepository;
import com.yasobafinibus.nnmtc.demonstration.repository.TutorRepository;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Named
@ConversationScoped
public class GradingController extends AbstractController<GradeBook> implements Serializable {


    private static final long serialVersionUID = 4004730525501291906L;
    @EJB
    private GradeBookRepository repository;
    @EJB
    private TutorRepository tutorRepository;
    @Inject
    private Conversation conversation;
    private TreeSet<Integer> scores = new TreeSet<>(Arrays.asList(1, 2, 3, 4, 5));
    private Integer score = 0;
    private LinkedHashSet<GradeOutcome> preliminaryGradeOutcomes;
    private StreamedContent file;

    public GradingController() {
        this.selected = new GradeBook();
    }


    /* method to complete student autocomplete search*/
    public List<Student> completeStudentNumber(String query) {
        List<Student> students = Collections.emptyList();

        if (Objects.nonNull(getSelected().getCohort())) {
            students = getSelected().getCohort().getStudents();
        }

        Predicate<Student> predicate = student -> student.getNumber().toLowerCase().contains(query.toLowerCase());
        return query.isBlank() || query.isEmpty() ? students : students.stream().filter(predicate).collect(Collectors.toList());
    }

    public void initConversation() {
        if (!FacesContext.getCurrentInstance().isPostback()
                && conversation.isTransient()) {
            conversation.begin();
        }

    }

    //redirect to select-student page if you can't find a conversation id
    public void checkConversation() {
        String cid = Faces.getRequest().getParameter("cid");
        if (Objects.isNull(cid)) {
            String requestBaseURL = Faces.getRequestBaseURL();
            Faces.redirect(requestBaseURL + "grading/select-schedule?faces-redirect=true");
        }
    }

    public String beginGrading() {
        String name = Faces.getExternalContext().getUserPrincipal().getName();
        Optional<GradeBook> gradeBook = repository.findGradeBook(getSelected());

        if (gradeBook.isPresent()) {
            Predicate<GradeOutcome> predicate = gradeOutcome -> gradeOutcome.getExaminer().getEmail().equals(name);
            if (gradeBook.get().getGradeOutcomes().stream().anyMatch(predicate)) {
                Messages.addError("messages", "Tutor already examined Student");
                return "";
            }
            //if grade book is present set it to selected
            setSelected(gradeBook.get());
        }

        Optional<Tutor> tutor = getTutor(name);
        if (tutor.isPresent()) {
            //generate preliminary grade outcomes for this grade book
            preliminaryGradeOutcomes = new LinkedHashSet<>();
            TreeSet<Procedure> procedures = getSelected().getDemonstration().getProcedures();
            for (Procedure procedure : procedures) {
                preliminaryGradeOutcomes.add(new GradeOutcome(tutor.get(), procedure, 0));
            }
        }
        return "/grading/grade-demonstration?faces-redirect=true";
    }

    public String endGrading() {
        ArrayList<GradeOutcome> gradeOutcomes = new ArrayList<>(getPreliminaryGradeOutcomes());
        getSelected().addAllGradeOutcome(gradeOutcomes);
        if (getSelected().getId() != null) {
            super.update();
        } else {
            super.create();
        }

        if (!conversation.isTransient()) {
            conversation.end();
        }

        Messages.addFlashGlobalInfo("Student Examined Successfully");
        return "/grading/select-schedule?faces-redirect=true";
    }


    private Optional<Tutor> getTutor(String name) {
        return tutorRepository.findByTutorEmail(name);
    }


    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public TreeSet<Integer> getScores() {
        return scores;
    }

    public void setScores(TreeSet<Integer> scores) {
        this.scores = scores;
    }


    public LinkedHashSet<GradeOutcome> getPreliminaryGradeOutcomes() {
        return preliminaryGradeOutcomes;
    }

    public void setPreliminaryGradeOutcomes(LinkedHashSet<GradeOutcome> preliminaryGradeOutcomes) {
        this.preliminaryGradeOutcomes = preliminaryGradeOutcomes;
    }


    @Override
    public AbstractRepository<GradeBook, Integer> getRepository() {
        return repository;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}
