package com.yasobafinibus.nnmtc.demonstration.domain;

import com.opencsv.bean.CsvBindByName;

import java.io.Serializable;

public class ProcedureDto implements Serializable {


    @CsvBindByName(column = "POSITION", required = true)
    public Integer position;

    @CsvBindByName(column = "NAME", required = true)
    public String name;

    public ProcedureDto(Integer position, String name) {
        this.position = position;
        this.name = name;
    }

    public ProcedureDto() {
    }

    public Procedure createProcedure(Integer position, String name) {
        return new Procedure(position, name);
    }

}
