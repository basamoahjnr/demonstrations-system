package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;
import java.util.TreeSet;

/**
 * a demonstration is a collection of steps (procedures)
 */
@Entity
@Table(name = "demonstrations", indexes = {
        @Index(name = "idx_demonstration_name", columnList = "name, createdOn")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_demonstration_name", columnNames = {"name"})
})
@NamedQuery(name = "Demonstration.findAll", query = "Select e from Demonstration e")
@NamedQuery(name = "Demonstration.findProcedures", query = "Select e.procedures from Demonstration e")
public class Demonstration extends AbstractEntity implements Serializable {

    @Basic
    @Column(unique = true, length = 10)
    private String code;

    @Basic
    @NotNull
    @Size(max = 600)
    @Column(unique = true, nullable = false, length = 600)
    private String name;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "demonstration_procedures",
            joinColumns = @JoinColumn(name = "demonstration_id"),
            inverseJoinColumns = @JoinColumn(name = "procedure_id"),
            uniqueConstraints = @UniqueConstraint(name = "uc_demonstration_procedures", columnNames = {"demonstration_id, procedure_id"}))
    private TreeSet<Procedure> procedures;

    public Demonstration(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Demonstration() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TreeSet<Procedure> getProcedures() {
        if (procedures == null) {
            procedures = new TreeSet<>();
        }
        return procedures;
    }

    public void setProcedures(TreeSet<Procedure> procedures) {
        this.procedures = procedures;
    }

    public void addProcedure(Procedure procedure) {
        getProcedures().add(procedure);
    }

    public void removeProcedure(Procedure procedure) {
        getProcedures().remove(procedure);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Demonstration that = (Demonstration) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Demonstration{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}