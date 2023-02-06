package com.yasobafinibus.nnmtc.demonstration.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * a schedule is a system for setting up an activity for a batch of students to
 * perform a specific demonstration
 *
 * @author koko
 */

@Entity
@Table(name = "schedules")
@NamedQuery(name = "Schedule.findAll", query = "Select e from Schedule e")
@NamedQuery(name = "Schedule.findByCode", query = "Select s from Schedule s where s.passcode=:code")
@NamedQuery(name = "Schedule.findByActivityType", query = "Select s from Schedule s where s.ActivityType=:ActivityType")
@NamedQuery(name = "Schedule.findByStartDate", query = "Select s from Schedule s where s.startDate=:startDate")
@NamedQuery(name = "Schedule.findByStartDateAndTutor", query = "Select s from Schedule s,s.examiners se where s.startDate>=:startDate and se.id=:id")
public class Schedule extends AbstractEntity implements Serializable {
    /**
     * unique code for the schedule
     */
    @Basic
    @Column(nullable = false, updatable = false)
    @NotNull
    @Size(min = 4, max = 6)
    private String passcode;
    /**
     * what type of activity does this schedule belong to eg. exams, quiz,
     * mid-semester
     */
    @Basic
//  @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityType ActivityType;
    /**
     * start date of the scheduled activity
     */
    @Basic
    @Column(nullable = false)
    @FutureOrPresent
    private LocalDateTime startDate;
    /**
     * expected end date of scheduled activity
     */
    @Basic
    @Future
    private LocalDateTime endDate;

    @ManyToOne
    private Tutor tutor;
    /**
     * tutors responsible for invigilating this schedule
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedule_examiners")
    private Set<Tutor> examiners;
    /**
     * demonstration associated with this schedule
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedule_demonstrations")
    private Set<Demonstration> demonstrations;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schedule_cohorts")
    private Set<Cohort> cohorts;

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String code) {
        this.passcode = code;
    }

    public ActivityType getActivityType() {
        return ActivityType;
    }

    public void setActivityType(ActivityType ActivityType) {
        this.ActivityType = ActivityType;
    }


    public @FutureOrPresent LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(@FutureOrPresent LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public @Future LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(@Future LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Set<Tutor> getExaminers() {
        if (examiners == null) {
            examiners = new HashSet<>();
        }
        return examiners;
    }

    public void setExaminers(Set<Tutor> examiners) {
        this.examiners = examiners;
    }

    public void addExaminer(Tutor examiner) {
        getExaminers().add(examiner);
    }

    public void removeExaminer(Tutor examiner) {
        getExaminers().remove(examiner);
    }

    public Set<Cohort> getCohorts() {
        if (cohorts == null) {
            cohorts = new HashSet<>();
        }
        return cohorts;
    }

    public void setCohorts(Set<Cohort> cohorts) {
        this.cohorts = cohorts;
    }

    public void addCohort(Cohort cohort) {
        getCohorts().add(cohort);
    }

    public void removeCohort(Cohort cohort) {
        getCohorts().remove(cohort);
    }

    public Set<Demonstration> getDemonstrations() {
        if (demonstrations == null) {
            demonstrations = new HashSet<>();
        }
        return demonstrations;
    }

    public void setDemonstrations(Set<Demonstration> demonstrations) {
        this.demonstrations = demonstrations;
    }

    public void addDemonstration(Demonstration demonstration) {
        getDemonstrations().add(demonstration);
    }

    public void removeDemonstration(Demonstration demonstration) {
        getDemonstrations().remove(demonstration);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (!startDate.equals(schedule.startDate)) return false;
        if (!examiners.equals(schedule.examiners)) return false;
        if (!demonstrations.equals(schedule.demonstrations)) return false;
        return cohorts.equals(schedule.cohorts);
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + examiners.hashCode();
        result = 31 * result + demonstrations.hashCode();
        result = 31 * result + cohorts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "passcode='" + passcode + '\'' +
                ", ActivityType=" + ActivityType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", tutor=" + tutor +
                ", examiners=" + examiners +
                ", demonstrations=" + demonstrations +
                ", cohorts=" + cohorts +
                '}';
    }
}
