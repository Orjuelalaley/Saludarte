package com.example.saludarteapp;

public class Sound {
    private String name;
    private boolean status;

    // Constructor vacío requerido para Firebase
    public Sound() {
    }

    public Sound(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    // Getters y setters
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
