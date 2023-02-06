package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author koko
 */

@Entity
@Table(name = "programs")
@NamedQuery(name = "Program.findAll", query = "Select e from Program e")
@NamedQuery(name = "Program.findByName", query = "Select p from Program p where p.name=:name")
public class Program extends AbstractEntity implements Serializable {

    @Basic
    @NotEmpty
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String name;

    public Program(String programName) {
        this.name = programName;
    }

    public Program() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program program = (Program) o;
        return id != null && Objects.equals(id, program.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Program{" + "name='" + name + '\'' + '}';
    }

}