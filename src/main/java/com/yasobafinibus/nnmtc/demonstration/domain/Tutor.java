package com.yasobafinibus.nnmtc.demonstration.domain;

import com.yasobafinibus.nnmtc.demonstration.infra.security.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * represents tutors of a program and some are able to create schedules
 * @author koko
 */
@Entity
@Table(name = "tutors", indexes = {
        @Index(name = "idx_tutor_createdon_email", columnList = "email")
})
@NamedQuery(name = "Tutor.findAll", query = "Select t from Tutor t")
@NamedQuery(name = "Tutor.findByEmail", query = "Select t from Tutor t where t.email=:email")
@NamedQuery(name = "Tutor.findSchedulesById", query = "Select t.schedules from Tutor t where t.id= :id")
public class Tutor extends AbstractEntity implements Serializable {


    /**
     * email of the tutor
     */
    @Basic
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @OneToOne
    private Program program;

    @OrderBy("startDate DESC")
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    @Basic
    @Column(nullable = false)
    @NotNull
    private String surname;
    @Basic
    @Column(nullable = false)
    @NotNull
    private String otherNames;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Tutor(String email, Program program, String surname, String otherNames) {
        this.email = email;
        this.program = program;
        this.surname = surname;
        this.otherNames = otherNames;
    }

    public Tutor(String surname, String otherNames, String email, User user) {
        this.surname = surname;
        this.otherNames = otherNames;
        this.email = email;
        this.user = user;
    }

    public Tutor() {
    }

    public Tutor(String surname, String otherNames, String email) {
        this.surname = surname;
        this.otherNames = otherNames;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public Optional<Program> getProgram() {
        return Optional.ofNullable(program);
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public User getUser() {
        if (Objects.isNull(user)) this.user = new User();
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String name) {
        this.surname = name;
    }

    public List<Schedule> getSchedules() {
        if (schedules == null) {
            schedules = new ArrayList<>();
        }
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public void addSchedule(Schedule schedule) {
        getSchedules().add(schedule);
        schedule.setTutor(this);
    }

    public void removeSchedule(Schedule schedule) {
        getSchedules().remove(schedule);
        schedule.setTutor(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tutor tutor = (Tutor) o;

        return email.equals(tutor.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "Tutor{" + " id=" + id + ", name=" + otherNames + " " + surname + " " + email + '}';
    }

}