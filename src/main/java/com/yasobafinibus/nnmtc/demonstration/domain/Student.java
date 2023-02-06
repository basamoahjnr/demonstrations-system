package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * students of the facility
 *
 * @author koko
 */
@Entity
@Table(name = "students")
@NamedQuery(name = "Student.findAll", query = "Select e from Student e")
@NamedQuery(name = "Student.findByNumber", query = "Select s from Student s where s.number=:number")
@NamedQuery(name = "Student.findByName", query = "Select s from Student s where s.surname=:name or s.otherNames=:name")
@NamedQuery(name = "Student.findByProgram", query = "Select s from Student s where s.program=:program")
public class Student extends AbstractEntity implements Serializable {

    /**
     * unique number of student
     */
    @Basic
    @Column(unique = true, nullable = false)
    @NotNull
    private String number;
    @Basic
    @Column(nullable = false)
    @NotNull
    private String surname;

    @Basic
    @NotNull
    @Column(nullable = false)
    private String otherNames;

    @OneToOne
    private Program program;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Cohort cohort;

    @Basic
    @Email
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    public Student(String number) {
        this.number = number;
    }

    public Student(String number, String surname, String otherNames, String email) {
        this.number = number;
        this.surname = surname;
        this.otherNames = otherNames;
        this.email = email;
    }

    public Student(String number, String surname, String otherNames, String email, Program program) {
        this.number = number;
        this.surname = surname;
        this.otherNames = otherNames;
        this.email = email;
        this.program = program;
    }

    public Student(String number, String surname, String otherNames, String email, Cohort cohort) {
        this.number = number;
        this.surname = surname;
        this.otherNames = otherNames;
        this.email = email;
        this.cohort = cohort;
    }


    public Student() {
    }

    public Student(String number, String surname, String otherName, String email, Program program, Cohort cohort) {
        this.number = number;
        this.surname = surname;
        this.otherNames = otherName;
        this.email = email;
        this.program = program;
        this.cohort = cohort;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Cohort getCohort() {
        return cohort;
    }

    public void setCohort(Cohort batch) {
        this.cohort = batch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (!number.equals(student.number)) return false;
        return email.equals(student.email);
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", surname='" + surname + '\'' +
                ", otherNames='" + otherNames + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    public static final class StudentBuilder {
        private @NotNull String number;
        private String otherNames;
        private String surname;
        private @NotNull @Valid Program program;
        private @NotNull @Valid Cohort batch;
        private @NotNull @Email String email;

        private StudentBuilder() {
        }

        public static StudentBuilder aStudent() {
            return new StudentBuilder();
        }

        public StudentBuilder withNumber(String number) {
            this.number = number;
            return this;
        }

        public StudentBuilder withOtherNames(String otherNames) {
            this.otherNames = otherNames;
            return this;
        }

        public StudentBuilder withSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public StudentBuilder withProgram(Program program) {
            this.program = program;
            return this;
        }

        public StudentBuilder withBatch(Cohort batch) {
            this.batch = batch;
            return this;
        }

        public StudentBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Student build() {
            Student student = new Student();
            student.setNumber(number);
            student.setOtherNames(otherNames);
            student.setSurname(surname);
            student.setProgram(program);
            student.setCohort(batch);
            student.setEmail(email);
            return student;
        }
    }
}