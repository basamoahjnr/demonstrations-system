package com.yasobafinibus.nnmtc.demonstration.domain;

import com.opencsv.bean.CsvBindByName;

public class StudentDto {

    @CsvBindByName(column = "NUMBER",required = true)
    public String number;
    @CsvBindByName(column = "SURNAME",required = true)
    public String surname;
    @CsvBindByName(column = "OTHER NAMES",required = true)
    public String otherName;
    @CsvBindByName(column = "EMAIL",required = true)
    public String email;

    public StudentDto() {
    }

}
