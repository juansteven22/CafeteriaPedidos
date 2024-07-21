package com.cafeteria;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaPedidos sistemaPedidos = new SistemaPedidos();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Sistema de Pedidos de Cafetería ---");
            System.out.println("1. Agregar pedido");
            System.out.println("2. Procesar pedido");
            System.out.println("3. Mostrar pedidos pendientes");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del cliente: ");
                    String nombreCliente = scanner.nextLine();
                    System.out.print("Item pedido: ");
                    String itemPedido = scanner.nextLine();
                    sistemaPedidos.agregarPedido(new Pedido(nombreCliente, itemPedido));
                    break;
                case 2:
                    sistemaPedidos.procesarPedido();
                    break;
                case 3:
                    sistemaPedidos.mostrarPedidosPendientes();
                    break;
                case 4:
                    System.out.println("Gracias por usar el Sistema de Pedidos de Cafetería.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
            }
        }
    }
}