package com.cafeteria;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InterfazGrafica extends JFrame {
    private SistemaPedidos sistemaPedidos;
    private JTextField nombreClienteField;
    private JComboBox<String> itemPedidoComboBox;
    private JTextArea areaResultados;
    private Timer actualizacionTimer;

    public InterfazGrafica() {
        sistemaPedidos = new SistemaPedidos();
        inicializarComponentes();
        iniciarTimerActualizacion();
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Pedidos de Cafetería");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelEntrada = new JPanel(new GridLayout(3, 2));
        panelEntrada.add(new JLabel("Nombre del cliente:"));
        nombreClienteField = new JTextField();
        panelEntrada.add(nombreClienteField);
        panelEntrada.add(new JLabel("Item pedido:"));
        itemPedidoComboBox = new JComboBox<>(sistemaPedidos.getItemsMenu());
        panelEntrada.add(itemPedidoComboBox);

        JButton agregarButton = new JButton("Agregar Pedido");
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarPedido();
            }
        });
        panelEntrada.add(agregarButton);

        JButton procesarButton = new JButton("Procesar Pedido");
        procesarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarPedido();
            }
        });
        panelEntrada.add(procesarButton);

        add(panelEntrada, BorderLayout.NORTH);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaResultados);
        add(scrollPane, BorderLayout.CENTER);

        JButton mostrarButton = new JButton("Mostrar Pedidos Pendientes");
        mostrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPedidosPendientes();
            }
        });
        add(mostrarButton, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sistemaPedidos.cerrar();
                actualizacionTimer.stop();
                dispose();
            }
        });
    }

    private void iniciarTimerActualizacion() {
        actualizacionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPedidosPendientes();
            }
        });
        actualizacionTimer.start();
    }

    private void agregarPedido() {
        String nombreCliente = nombreClienteField.getText();
        String itemPedido = (String) itemPedidoComboBox.getSelectedItem();
        if (!nombreCliente.isEmpty() && itemPedido != null) {
            int tiempoPreparacion = sistemaPedidos.getTiempoPreparacion(itemPedido);
            Pedido pedido = new Pedido(nombreCliente, itemPedido, tiempoPreparacion);
            sistemaPedidos.agregarPedido(pedido);
            areaResultados.append("Pedido agregado: " + pedido + "\n");
            nombreClienteField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
        }
    }

    private void procesarPedido() {
        Pedido pedido = sistemaPedidos.procesarPedido();
        if (pedido != null) {
            areaResultados.append("Pedido procesado manualmente: " + pedido + "\n");
        } else {
            areaResultados.append("No hay pedidos para procesar.\n");
        }
    }

    private void mostrarPedidosPendientes() {
        areaResultados.setText("");
        if (sistemaPedidos.estaVacio()) {
            areaResultados.append("No hay pedidos pendientes.\n");
        } else {
            areaResultados.append("Pedidos pendientes:\n");
            for (Pedido pedido : sistemaPedidos.obtenerPedidosPendientes()) {
                areaResultados.append(pedido + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazGrafica().setVisible(true);
            }
        });
    }
}