package com.cafeteria;

public class ItemMenu {
    private String nombre;
    private int tiempoPreparacion;

    public ItemMenu(String nombre, int tiempoPreparacion) {
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }
}