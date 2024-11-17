package com.ivory.ivory.domain;

public enum IncomeType {
    A("가형"),
    B("나형"),
    C("다형"),
    D("라형");

    private final String description;

    IncomeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
