package com.cafeteria;

public class Pedido {
    private String nombreCliente;
    private String itemPedido;

    public Pedido(String nombreCliente, String itemPedido) {
        this.nombreCliente = nombreCliente;
        this.itemPedido = itemPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getItemPedido() {
        return itemPedido;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "nombreCliente='" + nombreCliente + '\'' +
                ", itemPedido='" + itemPedido + '\'' +
                '}';
    }
}