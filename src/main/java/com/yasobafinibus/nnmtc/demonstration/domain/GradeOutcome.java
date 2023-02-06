package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;


@Entity
@Table(name = "grade_outcomes")
public class GradeOutcome extends AbstractEntity {


    @Basic
    @Column(columnDefinition = "integer DEFAULT 0")
    private Integer mark = 0;
    @ManyToOne(optional = false)
    private Procedure procedure;
    @ManyToOne(optional = false)
    private GradeBook gradeBook;
    @ManyToOne(optional = false)
    private Tutor examiner;

    public GradeOutcome(Procedure procedure, Integer mark) {
        this.mark = mark;
        this.procedure = procedure;
    }

    public GradeOutcome() {
    }

    public GradeOutcome(Tutor examiner, Procedure procedure, int mark) {
        this.examiner = examiner;
        this.procedure = procedure;
        this.mark = mark;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public GradeBook getGradeBook() {
        return gradeBook;
    }

    public void setGradeBook(GradeBook gradeBook) {
        this.gradeBook = gradeBook;
    }

    public Tutor getExaminer() {
        return examiner;
    }

    public void setExaminer(Tutor tutor) {
        this.examiner = tutor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeOutcome that = (GradeOutcome) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "GradeOutcome{" +
                "mark=" + mark +
                ", procedure=" + procedure +
                ", gradeBook=" + gradeBook +
                '}';
    }
}
