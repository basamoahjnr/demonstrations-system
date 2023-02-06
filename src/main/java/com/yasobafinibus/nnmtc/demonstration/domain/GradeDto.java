package com.yasobafinibus.nnmtc.demonstration.domain;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDateTime;

public class GradeDto {


    @CsvBindByName
    private LocalDateTime scheduleStartDate;
    @CsvBindByName
    private String studentNumber;
    @CsvBindByName
    private String studentSurname;
    @CsvBindByName
    private String studentOthernames;
    @CsvBindByName
    private String studentEmail;
    @CsvBindByName
    private String cohortCode;
    @CsvBindByName
    private String demonstrationCode;
    @CsvBindByName
    private Integer totalScore;

    public GradeDto(LocalDateTime scheduleStartDate,
                    String studentNumber,
                    String studentSurname,
                    String studentOthernames,
                    String studentEmail,
                    String cohortCode,
                    String demonstrationCode,
                    Integer totalScore) {
        this.scheduleStartDate = scheduleStartDate;
        this.studentNumber = studentNumber;
        this.studentSurname = studentSurname;
        this.studentOthernames = studentOthernames;
        this.studentEmail = studentEmail;
        this.cohortCode = cohortCode;
        this.demonstrationCode = demonstrationCode;
        this.totalScore = totalScore;
    }

    public LocalDateTime getScheduleStartDate() {
        return scheduleStartDate;
    }

    public void setScheduleStartDate(LocalDateTime scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getStudentOthernames() {
        return studentOthernames;
    }

    public void setStudentOthernames(String studentOthernames) {
        this.studentOthernames = studentOthernames;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getCohortCode() {
        return cohortCode;
    }

    public void setCohortCode(String cohortCode) {
        this.cohortCode = cohortCode;
    }

    public String getDemonstrationCode() {
        return demonstrationCode;
    }

    public void setDemonstrationCode(String demonstrationCode) {
        this.demonstrationCode = demonstrationCode;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

}
