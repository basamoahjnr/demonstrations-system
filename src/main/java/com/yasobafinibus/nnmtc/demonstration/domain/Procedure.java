package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * a procedure is a step performend in a demonstration
 *
 * @author koko
 */

@Entity
@Table(name = "procedures")
@NamedQuery(name = "Procedure.findByName", query = "Select p from Procedure p where p.name=:name")
public class Procedure extends AbstractEntity implements Serializable, Comparable<Procedure> {

    /**
     * position of the procedure in the demonstration
     */
    @Basic
    @NotNull
    @Column(nullable = false)
    private Integer position;

    @Basic
    @NotNull
    @Size(max = 700)
    @Column(nullable = false, columnDefinition = "varchar(700)")
    private String name;


    public Procedure() {
    }

    public Procedure(Integer position, String name) {
        this.position = position;
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Procedure{" +
                "id=" + id +
                ", position=" + position +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Procedure procedure = (Procedure) o;
        return id != null && Objects.equals(id, procedure.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public int compareTo(Procedure procedure) {
        return Integer.compare(this.position, procedure.position);
    }
}