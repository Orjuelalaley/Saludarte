package com.example.saludarteapp;

public class Sound {
    private String id; // Nuevo campo para el identificador único
    private String name;
    private boolean status;

    // Constructor vacío requerido para Firebase
    public Sound() {
    }

    public Sound(String id, String name, boolean status) {
        this.id = id; // Inicializa el nuevo campo
        this.name = name;
        this.status = status;
    }

    // Nuevo getter y setter para el identificador
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}
