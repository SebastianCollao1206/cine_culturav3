package com.pe.cine_culturav3.model;

public enum RoleName {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USUARIO"),
    ROLE_CLIENT("CLIENTE");

    private String value;

    private RoleName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
