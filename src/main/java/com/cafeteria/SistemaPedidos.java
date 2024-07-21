package com.cafeteria;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

public class SistemaPedidos {
    private Queue<Pedido> colaPedidos;
    private Map<String, ItemMenu> menu;
    private Map<String, List<String>> categorias;
    private ScheduledExecutorService scheduler;

    public SistemaPedidos() {
        this.colaPedidos = new ConcurrentLinkedQueue<>();
        this.menu = new HashMap<>();
        this.categorias = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        inicializarMenu();
    }

    private void inicializarMenu() {
        agregarItem("Café Americano", 5, "Bebidas Calientes");
        agregarItem("Espresso", 3, "Bebidas Calientes");
        agregarItem("Latte", 7, "Bebidas Calientes");
        agregarItem("Capuchino", 8, "Bebidas Calientes");
        agregarItem("Té Verde", 4, "Bebidas Calientes");
        agregarItem("Frappé", 10, "Bebidas Frías");
        agregarItem("Smoothie de Frutas", 8, "Bebidas Frías");
        agregarItem("Limonada", 5, "Bebidas Frías");
        agregarItem("Muffin", 10, "Panadería");
        agregarItem("Croissant", 12, "Panadería");
        agregarItem("Galletas", 6, "Panadería");
        agregarItem("Sándwich", 15, "Comida Rápida");
        agregarItem("Ensalada", 10, "Comida Rápida");
    }

    private void agregarItem(String nombre, int tiempoPreparacion, String categoria) {
        ItemMenu item = new ItemMenu(nombre, tiempoPreparacion, categoria);
        menu.put(nombre, item);
        categorias.computeIfAbsent(categoria, k -> new ArrayList<>()).add(nombre);
    }

    public void agregarPedido(Pedido pedido) {
        colaPedidos.offer(pedido);
        System.out.println("Pedido agregado: " + pedido);

        scheduler.schedule(() -> {
            procesarPedidoAutomaticamente(pedido);
        }, pedido.getTiempoPreparacion(), TimeUnit.MINUTES);
    }

    private void procesarPedidoAutomaticamente(Pedido pedido) {
        if (colaPedidos.remove(pedido)) {
            System.out.println("Pedido procesado automáticamente: " + pedido);
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "Pedido procesado automáticamente: " + pedido);
            });
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

    public Set<String> getCategorias() {
        return categorias.keySet();
    }

    public List<String> getItemsPorCategoria(String categoria) {
        return categorias.getOrDefault(categoria, new ArrayList<>());
    }

    public String getCategoriaPorItem(String itemNombre) {
        ItemMenu item = menu.get(itemNombre);
        return item != null ? item.getCategoria() : "";
    }

    public void cerrar() {
        scheduler.shutdown();
    }
}