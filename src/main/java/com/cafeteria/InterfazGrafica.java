package com.cafeteria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class InterfazGrafica extends JFrame {
    private SistemaPedidos sistemaPedidos;
    private JTextField nombreClienteField;
    private JComboBox<String> categoriaComboBox;
    private JComboBox<String> itemPedidoComboBox;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    private Timer actualizacionTimer;

    public InterfazGrafica() {
        sistemaPedidos = new SistemaPedidos();
        inicializarComponentes();
        iniciarTimerActualizacion();
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Pedidos de Cafetería");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelEntrada = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelEntrada.add(new JLabel("Nombre del cliente:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        nombreClienteField = new JTextField(20);
        panelEntrada.add(nombreClienteField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelEntrada.add(new JLabel("Categoría:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        categoriaComboBox = new JComboBox<>(sistemaPedidos.getCategorias().toArray(new String[0]));
        categoriaComboBox.addActionListener(e -> actualizarItemsPorCategoria());
        panelEntrada.add(categoriaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelEntrada.add(new JLabel("Item pedido:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        itemPedidoComboBox = new JComboBox<>();
        panelEntrada.add(itemPedidoComboBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JButton agregarButton = new JButton("Agregar Pedido");
        agregarButton.addActionListener(e -> agregarPedido());
        panelEntrada.add(agregarButton, gbc);

        add(panelEntrada, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"Cliente", "Categoría", "Item", "Tiempo Restante"}, 0);
        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Restaurar el color por defecto para todas las celdas
                c.setForeground(table.getForeground());

                // Cambiar a rojo solo la celda de tiempo restante si es menos de un minuto
                if (column == 3 && value != null) {
                    String tiempo = value.toString();
                    if (tiempo.startsWith("0:")) {
                        c.setForeground(Color.RED);
                    }
                }
                return c;
            }
        });
        JScrollPane scrollPane = new JScrollPane(tablaPedidos);
        add(scrollPane, BorderLayout.CENTER);

        JButton procesarButton = new JButton("Procesar Pedido");
        procesarButton.addActionListener(e -> procesarPedido());
        add(procesarButton, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sistemaPedidos.cerrar();
                actualizacionTimer.stop();
                dispose();
            }
        });

        actualizarItemsPorCategoria();
    }

    private void actualizarItemsPorCategoria() {
        String categoriaSeleccionada = (String) categoriaComboBox.getSelectedItem();
        itemPedidoComboBox.removeAllItems();
        for (String item : sistemaPedidos.getItemsPorCategoria(categoriaSeleccionada)) {
            itemPedidoComboBox.addItem(item);
        }
    }

    private void iniciarTimerActualizacion() {
        actualizacionTimer = new Timer(1000, e -> actualizarTablaPedidos());
        actualizacionTimer.start();
    }

    private void agregarPedido() {
        String nombreCliente = nombreClienteField.getText();
        String itemPedido = (String) itemPedidoComboBox.getSelectedItem();
        if (!nombreCliente.isEmpty() && itemPedido != null) {
            int tiempoPreparacion = sistemaPedidos.getTiempoPreparacion(itemPedido);
            Pedido pedido = new Pedido(nombreCliente, itemPedido, tiempoPreparacion);
            sistemaPedidos.agregarPedido(pedido);
            nombreClienteField.setText("");
            actualizarTablaPedidos();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
        }
    }

    private void procesarPedido() {
        Pedido pedido = sistemaPedidos.procesarPedido();
        if (pedido != null) {
            JOptionPane.showMessageDialog(this, "Pedido procesado manualmente: " + pedido);
            actualizarTablaPedidos();
        } else {
            JOptionPane.showMessageDialog(this, "No hay pedidos para procesar.");
        }
    }

    private void actualizarTablaPedidos() {
        modeloTabla.setRowCount(0);
        for (Pedido pedido : sistemaPedidos.obtenerPedidosPendientes()) {
            long minutosRestantes = ChronoUnit.MINUTES.between(LocalDateTime.now(), pedido.getTiempoInicio().plusMinutes(pedido.getTiempoPreparacion()));
            long segundosRestantes = ChronoUnit.SECONDS.between(LocalDateTime.now(), pedido.getTiempoInicio().plusMinutes(pedido.getTiempoPreparacion())) % 60;
            String tiempoRestante = String.format("%d:%02d", minutosRestantes, segundosRestantes);
            String categoria = sistemaPedidos.getCategoriaPorItem(pedido.getItemPedido());
            modeloTabla.addRow(new Object[]{pedido.getNombreCliente(), categoria, pedido.getItemPedido(), tiempoRestante});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazGrafica().setVisible(true));
    }
}