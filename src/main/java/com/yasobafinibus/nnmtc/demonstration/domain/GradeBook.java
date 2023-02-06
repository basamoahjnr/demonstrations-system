package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author koko
 */

@Entity
@Table(name = "gradebooks", indexes = {
        @Index(name = "idx_grade", columnList = "demonstration_id, tutor_id, procedure_id, student_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_grade", columnNames = {"student_id", "schedule_id", "demonstration_id"})
})
@NamedQueries(
        @NamedQuery(
                name = "GradeBook.findGradeBook",
                query = "select g from GradeBook  g where g.student= :student and g.demonstration= :demonstration and g.schedule= :schedule"
        ))
@SqlResultSetMapping(
        name = "GradeDtoMapping",
        classes = @ConstructorResult(
                targetClass = GradeDto.class,
                columns = {
                        @ColumnResult(name = "scheduleStartDate", type = LocalDateTime.class),
                        @ColumnResult(name = "studentNumber"),
                        @ColumnResult(name = "studentSurname"),
                        @ColumnResult(name = "studentOthernames"),
                        @ColumnResult(name = "studentEmail"),
                        @ColumnResult(name = "cohortCode"),
                        @ColumnResult(name = "demonstrationCode"),
                        @ColumnResult(name = "totalScore", type = Integer.class)}))
public class GradeBook extends AbstractEntity implements Serializable {

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Schedule schedule;
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Demonstration demonstration;
    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Cohort cohort;
    @Column(name = "total_outcome")
    private Integer totalScore;
    @NotNull
    @OneToMany(mappedBy = "gradeBook", cascade = CascadeType.ALL)
    private List<GradeOutcome> gradeOutcomes;


    public GradeBook() {
    }

    public GradeBook(Student student, Schedule schedule, Demonstration demonstration) {
        this.student = student;
        this.schedule = schedule;
        this.demonstration = demonstration;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }


    public Demonstration getDemonstration() {
        return demonstration;
    }

    public void setDemonstration(Demonstration demonstration) {
        this.demonstration = demonstration;
    }

    public Cohort getCohort() {
        return cohort;
    }

    public void setCohort(Cohort cohort) {
        this.cohort = cohort;
    }

    //create grade for all demonstration procedures
    public List<GradeOutcome> getGradeOutcomes() {
        if (gradeOutcomes == null) {
            gradeOutcomes = new ArrayList<>();
        }
        return gradeOutcomes;
    }

    public void setGradeOutcomes(List<GradeOutcome> gradeOutcomes) {
        this.gradeOutcomes = gradeOutcomes;
    }

    public void addGradeOutcome(GradeOutcome gradeOutcome) {
        getGradeOutcomes().add(gradeOutcome);
        gradeOutcome.setGradeBook(this);
    }

    public void addAllGradeOutcome(List<GradeOutcome> gradeOutcomes) {
        for (GradeOutcome gradeOutcome : gradeOutcomes) {
            getGradeOutcomes().add(gradeOutcome);
            gradeOutcome.setGradeBook(this);
        }
    }

    public void removeGradeOutcome(GradeOutcome score) {
        getGradeOutcomes().remove(score);
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeBook gradeBook = (GradeBook) o;
        return id != null && Objects.equals(id, gradeBook.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}