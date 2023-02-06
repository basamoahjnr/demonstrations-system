package com.yasobafinibus.nnmtc.demonstration.controller.util;


public enum AppSortOrder {

    ASCENDING, DESCENDING, UNSORTED;

    public boolean isAscending() {
        return ASCENDING.equals(this);
    }
}
