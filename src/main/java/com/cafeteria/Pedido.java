package com.cafeteria;

import java.time.LocalDateTime;

public class Pedido {
    private String nombreCliente;
    private String itemPedido;
    private int tiempoPreparacion;
    private LocalDateTime tiempoInicio;

    public Pedido(String nombreCliente, String itemPedido, int tiempoPreparacion) {
        this.nombreCliente = nombreCliente;
        this.itemPedido = itemPedido;
        this.tiempoPreparacion = tiempoPreparacion;
        this.tiempoInicio = LocalDateTime.now();
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getItemPedido() {
        return itemPedido;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public LocalDateTime getTiempoInicio() {
        return tiempoInicio;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "nombreCliente='" + nombreCliente + '\'' +
                ", itemPedido='" + itemPedido + '\'' +
                ", tiempoPreparacion=" + tiempoPreparacion +
                " minutos, iniciado en " + tiempoInicio +
                '}';
    }
}