package com.cafeteria;

import java.util.*;
import java.util.concurrent.*;

public class SistemaPedidos {
    private Queue<Pedido> colaPedidos;
    private Map<String, ItemMenu> menu;
    private ScheduledExecutorService scheduler;

    public SistemaPedidos() {
        this.colaPedidos = new ConcurrentLinkedQueue<>();
        this.menu = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        inicializarMenu();
    }

    private void inicializarMenu() {
        menu.put("Café", new ItemMenu("Café", 5));
        menu.put("Té", new ItemMenu("Té", 3));
        menu.put("Latte", new ItemMenu("Latte", 7));
        menu.put("Capuchino", new ItemMenu("Capuchino", 8));
        menu.put("Muffin", new ItemMenu("Muffin", 10));
    }

    public void agregarPedido(Pedido pedido) {
        colaPedidos.offer(pedido);
        System.out.println("Pedido agregado: " + pedido);

        // Programar el procesamiento automático del pedido
        scheduler.schedule(() -> {
            procesarPedidoAutomaticamente(pedido);
        }, pedido.getTiempoPreparacion(), TimeUnit.MINUTES);
    }

    private void procesarPedidoAutomaticamente(Pedido pedido) {
        if (colaPedidos.remove(pedido)) {
            System.out.println("Pedido procesado automáticamente: " + pedido);
        }
    }

    public Pedido procesarPedido() {
        Pedido pedido = colaPedidos.poll();
        if (pedido != null) {
            System.out.println("Procesando pedido manualmente: " + pedido);
        } else {
            System.out.println("No hay pedidos para procesar.");
        }
        return pedido;
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

    public boolean estaVacio() {
        return colaPedidos.isEmpty();
    }

    public Queue<Pedido> obtenerPedidosPendientes() {
        return new LinkedList<>(colaPedidos);
    }

    public int getTiempoPreparacion(String itemNombre) {
        ItemMenu item = menu.get(itemNombre);
        return item != null ? item.getTiempoPreparacion() : 0;
    }

    public String[] getItemsMenu() {
        return menu.keySet().toArray(new String[0]);
    }

    public void cerrar() {
        scheduler.shutdown();
    }
}