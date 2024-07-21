package com.cafeteria;

import java.util.LinkedList;
import java.util.Queue;

public class SistemaPedidos {
    private Queue<Pedido> colaPedidos;

    public SistemaPedidos() {
        this.colaPedidos = new LinkedList<>();
    }

    public void agregarPedido(Pedido pedido) {
        colaPedidos.offer(pedido);
        System.out.println("Pedido agregado: " + pedido);
    }

    public void procesarPedido() {
        if (!colaPedidos.isEmpty()) {
            Pedido pedido = colaPedidos.poll();
            System.out.println("Procesando pedido: " + pedido);
        } else {
            System.out.println("No hay pedidos para procesar.");
        }
    }

    public void mostrarPedidosPendientes() {
        if (colaPedidos.isEmpty()) {
            System.out.println("No hay pedidos pendientes.");
        } else {
            System.out.println("Pedidos pendientes:");
            for (Pedido pedido : colaPedidos) {
                System.out.println(pedido);
            }
        }
    }
}