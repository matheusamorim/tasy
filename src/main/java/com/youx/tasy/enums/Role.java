package com.youx.tasy.enums;

public enum Role {
    ENFERMEIRO("ENFERMEIRO"),
    MEDICO("MEDICO");

    String value;

    Role(String value) {
         this.value = value;
    }

    public String getValue() {
        return value;
    }
}
