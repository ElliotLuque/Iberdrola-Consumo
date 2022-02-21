package com.elliot.iberdrola.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.elliot.iberdrola.controlador.ControladorEliminar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class EliminarUI extends JDialog 
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private final ControladorEliminar CONTR = new ControladorEliminar();
	private JTextField contratoField;
	private JTextField clienteField;

	public EliminarUI() {
		setTitle("Eliminar documentos");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(204, 255, 204));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		contratoField = new JTextField();
		contratoField.setBounds(16, 53, 171, 20);
		contentPanel.add(contratoField);
		contratoField.setColumns(10);
		
		JLabel contratoLabel = new JLabel("ID Contrato:");
		contratoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		contratoLabel.setBounds(16, 28, 114, 14);
		contentPanel.add(contratoLabel);
		
		JLabel clienteLabel = new JLabel("ID Cliente:");
		clienteLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		clienteLabel.setBounds(16, 104, 114, 14);
		contentPanel.add(clienteLabel);
		
		clienteField = new JTextField();
		clienteField.setColumns(10);
		clienteField.setBounds(16, 127, 160, 20);
		contentPanel.add(clienteField);
		
		JButton eliminarCliente = new JButton("Eliminar por cliente");
		eliminarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				CONTR.eliminarCliente(clienteField.getText());
			}
		});
		eliminarCliente.setForeground(Color.WHITE);
		eliminarCliente.setFont(new Font("Segoe UI", Font.BOLD, 14));
		eliminarCliente.setFocusPainted(false);
		eliminarCliente.setBorderPainted(false);
		eliminarCliente.setBackground(new Color(46, 139, 87));
		eliminarCliente.setBounds(16, 193, 183, 42);
		contentPanel.add(eliminarCliente);
		
		JButton eliminarContrato = new JButton("Eliminar por contrato");
		eliminarContrato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				CONTR.eliminarContrato(contratoField.getText());
			}
		});
		eliminarContrato.setForeground(Color.WHITE);
		eliminarContrato.setFont(new Font("Segoe UI", Font.BOLD, 14));
		eliminarContrato.setFocusPainted(false);
		eliminarContrato.setBorderPainted(false);
		eliminarContrato.setBackground(new Color(46, 139, 87));
		eliminarContrato.setBounds(231, 193, 183, 42);
		contentPanel.add(eliminarContrato);
		
		JLabel descLabel = new JLabel("<html>Este borrado es recursivo, al borrar de cliente, borrará sus contratos y sus consumos. \nAl borrar contrato, borrará sus consumos</html>");
		descLabel.setVerticalAlignment(SwingConstants.TOP);
		descLabel.setHorizontalAlignment(SwingConstants.LEFT);
		descLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		descLabel.setBounds(259, 38, 154, 118);
		contentPanel.add(descLabel);
	}
}
