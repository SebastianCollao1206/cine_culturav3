package com.pe.cine_culturav3.model;

public enum EstadoSuscripcion {
    ACTIVA("activa"),
    VENCIDA("vencida"),
    CANCELADA("cancelada");

    private String value;

    EstadoSuscripcion(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
