package com.codingshuttle.SecurityApp.SecurityApplication.entities.enums;

public enum PlanName {
    FREE(0),
    BASIC(1),
    PREMIUM(2);

    private final int level;

    PlanName(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
