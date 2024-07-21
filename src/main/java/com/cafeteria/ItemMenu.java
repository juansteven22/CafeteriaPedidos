package com.cafeteria;

public class ItemMenu {
    private String nombre;
    private int tiempoPreparacion;
    private String categoria;

    public ItemMenu(String nombre, int tiempoPreparacion, String categoria) {
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public String getCategoria() {
        return categoria;
    }
}