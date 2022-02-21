package com.elliot.iberdrola.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.elliot.iberdrola.controlador.ControladorFactura;
import javax.swing.SwingConstants;

public class FacturaUI extends JDialog 
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	
	private static final ControladorFactura CONTR = new ControladorFactura();
	private JTextField mes;
	private JLabel precioFactura;
	private JTable tabla;
	private JTextField diaField;
	public static JLabel precioLabel;
	public static DefaultTableModel model;

	public FacturaUI() 
	{
		setTitle("Factura");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 566, 512);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(204, 255, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel BG = new JPanel();
		BG.setBackground(new Color(143, 188, 143));
		BG.setBounds(0, 439, 562, 44);
		contentPanel.add(BG);
		BG.setLayout(null);
		
		precioLabel = new JLabel("Precio factura:");
		precioLabel.setBounds(359, 13, 109, 14);
		BG.add(precioLabel);
		precioLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
		
		precioFactura = new JLabel("-");
		precioFactura.setHorizontalAlignment(SwingConstants.TRAILING);
		precioFactura.setBounds(412, -2, 129, 42);
		BG.add(precioFactura);
		precioFactura.setFont(new Font("Segoe UI", Font.BOLD, 20));
		
		final JLabel calculando = new JLabel("Calculando...");
		calculando.setVisible(false);
		calculando.setFont(new Font("Segoe UI", Font.BOLD, 13));
		calculando.setBounds(10, 16, 109, 14);
		BG.add(calculando);
		
		JLabel titleFactura = new JLabel("Factura");
		titleFactura.setFont(new Font("Segoe UI", Font.BOLD, 32));
		titleFactura.setBounds(18, 17, 145, 49);
		contentPanel.add(titleFactura);
		
		JLabel idLabel = new JLabel("ID Contrato:");
		idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		idLabel.setBounds(254, 31, 101, 14);
		contentPanel.add(idLabel);
		
		idField = new JTextField();
		idField.setBounds(348, 26, 193, 26);
		contentPanel.add(idField);
		idField.setColumns(10);
		
		JLabel precioHoraLabel = new JLabel("Precio por hora:");
		precioHoraLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		precioHoraLabel.setBounds(18, 105, 110, 14);
		contentPanel.add(precioHoraLabel);
		
		JButton diaBtn = new JButton("Generar factura del día");
		diaBtn.setFocusPainted(false);
		diaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				calculando.setVisible(true);
				CONTR.facturaDiaria(idField.getText(), mes.getText(), Integer.parseInt(diaField.getText()), precioFactura, tabla);
				calculando.setVisible(false);
				
				JOptionPane.showMessageDialog(null, "Factura generada!", "Factura", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		diaBtn.setForeground(new Color(255, 255, 255));
		diaBtn.setBorderPainted(false);
		diaBtn.setBackground(new Color(46, 139, 87));
		diaBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		diaBtn.setBounds(18, 353, 201, 50);
		contentPanel.add(diaBtn);
		
		JButton mesBtn = new JButton("Generar factura del periodo");
		mesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				calculando.setVisible(true);
				CONTR.facturaPeriodo(idField.getText(), mes.getText(), precioFactura, tabla);
				calculando.setVisible(false);
				
				JOptionPane.showMessageDialog(null, "Factura generada!", "Factura", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mesBtn.setFocusPainted(false);
		mesBtn.setForeground(Color.WHITE);
		mesBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		mesBtn.setBorderPainted(false);
		mesBtn.setBackground(new Color(46, 139, 87));
		mesBtn.setBounds(243, 354, 221, 50);
		contentPanel.add(mesBtn);
		
		JLabel mesLabel = new JLabel("Mes:");
		mesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		mesLabel.setBounds(254, 71, 101, 14);
		contentPanel.add(mesLabel);
		
		mes = new JTextField();
		mes.setText("periodoEnero");
		mes.setColumns(10);
		mes.setBounds(348, 71, 193, 26);
		contentPanel.add(mes);
		
		model = new DefaultTableModel();
		model.setColumnIdentifiers(new Object[] {"Hora", "Precio"});
		model.setDataVector(CONTR.vectorTabla(), new Object[] {"Hora", "Precio kW/h"});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(18, 129, 327, 194);
		contentPanel.add(scrollPane);
		
		tabla = new JTable();
		scrollPane.setViewportView(tabla);
		tabla.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		tabla.setCellSelectionEnabled(true);
		tabla.setModel(model);
		
		JButton btnGenerarValores = new JButton("Generar valores");
		btnGenerarValores.setFocusPainted(false);
		btnGenerarValores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{			
				model.setDataVector(CONTR.vectorTabla(), new Object[] {"Hora", "Precio kW/h"});
			}
		});
		btnGenerarValores.setForeground(Color.WHITE);
		btnGenerarValores.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnGenerarValores.setBorderPainted(false);
		btnGenerarValores.setBackground(new Color(46, 139, 87));
		btnGenerarValores.setBounds(374, 129, 156, 44);
		contentPanel.add(btnGenerarValores);
		
		diaField = new JTextField();
		diaField.setText("0");
		diaField.setBounds(374, 220, 32, 26);
		contentPanel.add(diaField);
		diaField.setColumns(10);
		
		JLabel lblndiceDa = new JLabel("Índice día:");
		lblndiceDa.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblndiceDa.setBounds(373, 198, 101, 14);
		contentPanel.add(lblndiceDa);
	}
}
