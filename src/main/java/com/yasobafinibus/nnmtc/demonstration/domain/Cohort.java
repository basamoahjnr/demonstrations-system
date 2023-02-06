package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * a batch of student is a group of students with a common identity
 *
 * @author koko
 */

@Entity
@Table(name = "cohorts", indexes = {
        @Index(name = "idx_cohort_name_code", columnList = "name, code")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_cohort_code_name", columnNames = {"code", "name"}),
        @UniqueConstraint(name = "uc_cohort_name", columnNames = {"name"})
})
@NamedQuery(name = "Cohort.findAll", query = "Select e from Cohort e")
@NamedQuery(name = "Cohort.findByName", query = "Select s from Cohort s where s.name like :name")
public class Cohort extends AbstractEntity implements Serializable {

    @Basic
    @NotNull
    @Column(unique = true, nullable = false)
    private String name;

    @Basic
    @NotNull
    @Column(unique = true, length = 10,nullable = false)
    private String code;

    @OneToMany(mappedBy = "cohort", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Student> students;

    public Cohort() {
    }

    public Cohort(String name) {
        this.name = name;
    }

    public Cohort(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        if (students == null) {
            students = new ArrayList<>();
        }
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void addStudent(Student student) {
        getStudents().add(student);
        student.setCohort(this);
    }

    public void removeStudent(Student student) {
        getStudents().remove(student);
        student.setCohort(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cohort cohort = (Cohort) o;
        return id != null && Objects.equals(id, cohort.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}