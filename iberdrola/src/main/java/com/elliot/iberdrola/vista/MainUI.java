package com.elliot.iberdrola.vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.elliot.iberdrola.controlador.Controlador;
import com.elliot.iberdrola.controlador.ControladorFactura;

public class MainUI extends JFrame 
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private final static Controlador CONTR = new Controlador();
	private final static ControladorFactura CONTR_FACT = new ControladorFactura();
	
	private static JPanel menubar;
	public static JLabel labelColeccion;
	public static JButton insertDoc;
	public static JButton updateDoc;
	public static JButton findDoc;
	public static JButton deleteDoc;
	private JButton btnFactura;
	public static JButton btnSiguienteDia;
	public static JButton btnSimular;
	public static JButton btnGenerarFacturas;

	public MainUI() 
	{
		setTitle("Iberdrola");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 464);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(204, 255, 204));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menubar = new JPanel();
		menubar.setBackground(new Color(204, 255, 204));
		menubar.setBounds(0, 0, 180, 433);
		
		menubar.setLayout(null);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(menubar);
		scroll.setSize(218, 391);
		scroll.setLocation(0, 44);
		scroll.setViewportBorder(null);
		
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(5);
		contentPane.add(scroll);
		
		JButton addCol = new JButton("AÑADIR COLECCIÓN");
		addCol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String colNombre = JOptionPane.showInputDialog(null, "Nombre de la nueva colección: ", "Nuevo", JOptionPane.QUESTION_MESSAGE);
				CONTR.addColeccion(colNombre);
				resetUI();
			}
		});
		addCol.setFocusPainted(false);
		addCol.setRequestFocusEnabled(false);
		addCol.setForeground(new Color(0, 0, 0));
		addCol.setBorderPainted(false);
		addCol.setBackground(new Color(154, 205, 50));
		addCol.setFont(new Font("Segoe UI", Font.BOLD, 13));
		addCol.setBounds(10, 11, 186, 22);
		contentPane.add(addCol);
		
		deleteDoc = new JButton("Eliminar documento");
		deleteDoc.setVisible(false);
		deleteDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String id = JOptionPane.showInputDialog(null, "ID del documento para borrar: ", "Borrar", JOptionPane.QUESTION_MESSAGE);
				CONTR.deleteDocument(CONTR.currentCol, id);
			}
		});
		deleteDoc.setFocusPainted(false);
		deleteDoc.setBorderPainted(false);
		deleteDoc.setBackground(new Color(154, 205, 50));
		deleteDoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
		deleteDoc.setBounds(286, 88, 175, 46);
		contentPane.add(deleteDoc);
		
		findDoc = new JButton("Ver documentos");
		findDoc.setVisible(false);
		findDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String id = JOptionPane.showInputDialog(null, "ID del documento para buscar: ", "Buscar", JOptionPane.QUESTION_MESSAGE);

				CONTR.getDocumentos(CONTR.currentCol, id);
			}
		});
		findDoc.setFocusPainted(false);
		findDoc.setBorderPainted(false);
		findDoc.setBackground(new Color(154, 205, 50));
		findDoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
		findDoc.setBounds(286, 169, 175, 46);
		contentPane.add(findDoc);
		
		updateDoc = new JButton("Editar documento");
		updateDoc.setVisible(false);
		updateDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String id = JOptionPane.showInputDialog(null, "ID del documento para editar: ", "Editar", JOptionPane.QUESTION_MESSAGE);
				String campo = JOptionPane.showInputDialog(null, "Campo del documento a editar: ", "Editar", JOptionPane.QUESTION_MESSAGE);
				String valor = JOptionPane.showInputDialog(null, "Nuevo valor: ", "Editar", JOptionPane.QUESTION_MESSAGE);
				
				CONTR.updateDocumento(CONTR.currentCol, id, campo, valor);
			}
		});
		updateDoc.setFocusPainted(false);
		updateDoc.setBorderPainted(false);
		updateDoc.setBackground(new Color(154, 205, 50));
		updateDoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
		updateDoc.setBounds(286, 259, 175, 46);
		contentPane.add(updateDoc);
		
		insertDoc = new JButton("Insertar documento");
		insertDoc.setVisible(false);
		insertDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				CONTR.insertDocumento(CONTR.currentCol);
			}
		});
		insertDoc.setFocusPainted(false);
		insertDoc.setBorderPainted(false);
		insertDoc.setBackground(new Color(154, 205, 50));
		insertDoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
		insertDoc.setBounds(286, 343, 175, 46);
		contentPane.add(insertDoc);
		
		labelColeccion = new JLabel("");
		labelColeccion.setHorizontalAlignment(SwingConstants.CENTER);
		labelColeccion.setFont(new Font("Segoe UI", Font.BOLD, 22));
		labelColeccion.setBounds(283, 24, 167, 22);
		contentPane.add(labelColeccion);
		
		btnSimular = new JButton("Simular datos");
		btnSimular.setVisible(false);
		btnSimular.setForeground(new Color(255, 255, 255));
		btnSimular.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnSimular.setBorderPainted(false);
		btnSimular.setBackground(new Color(46, 139, 87));
		btnSimular.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{		
				simulacionInserts();
			}
			
		});
		btnSimular.setBounds(571, 257, 149, 37);
		contentPane.add(btnSimular);
		
		btnFactura = new JButton("Facturas cliente");
		btnFactura.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				FacturaUI window = new FacturaUI();
				window.setVisible(true);
			}
		});
		btnFactura.setForeground(Color.WHITE);
		btnFactura.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnFactura.setBorderPainted(false);
		btnFactura.setBackground(new Color(46, 139, 87));
		btnFactura.setBounds(571, 321, 148, 37);
		contentPane.add(btnFactura);
		
		JButton btnEliminar = new JButton("Eliminar docs.");
		btnEliminar.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				EliminarUI window = new EliminarUI();
				window.setVisible(true);
			}
		});
		btnEliminar.setForeground(Color.WHITE);
		btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnEliminar.setBorderPainted(false);
		btnEliminar.setBackground(new Color(46, 139, 87));
		btnEliminar.setBounds(571, 385, 149, 37);
		contentPane.add(btnEliminar);
		
		btnSiguienteDia = new JButton("Siguiente día");
		btnSiguienteDia.setVisible(false);
		btnSiguienteDia.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				simulacionUpdate();
			}
		});
		btnSiguienteDia.setForeground(Color.WHITE);
		btnSiguienteDia.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnSiguienteDia.setBorderPainted(false);
		btnSiguienteDia.setBackground(new Color(46, 139, 87));
		btnSiguienteDia.setBounds(571, 194, 150, 37);
		contentPane.add(btnSiguienteDia);
		
		btnGenerarFacturas = new JButton("Generar facturas");
		btnGenerarFacturas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Instant inicio = Instant.now();
				CONTR_FACT.allFacturasPeriodo(CONTR.currentFactCol,CONTR.currentCol);
				Instant fin = Instant.now();
				
				Duration timeElapsed = Duration.between(inicio, fin);
				
				JOptionPane.showMessageDialog(null, "Facturas de todos los contratos generadas! \nTiempo transcurrido: " + timeElapsed.getSeconds() + "s", "Facturación", JOptionPane.INFORMATION_MESSAGE);

			}
		});
		btnGenerarFacturas.setVisible(false);
		btnGenerarFacturas.setForeground(Color.WHITE);
		btnGenerarFacturas.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnGenerarFacturas.setFocusPainted(false);
		btnGenerarFacturas.setBorderPainted(false);
		btnGenerarFacturas.setBackground(new Color(46, 139, 87));
		btnGenerarFacturas.setBounds(571, 140, 149, 37);
		contentPane.add(btnGenerarFacturas);
		
		init();
	}
	
	// Simula las actualizaciones del siguiente día
	public final void simulacionUpdate()
	{
		// Actualiza los 10M de consumos previamente insertados
		Instant inicio = Instant.now();
		CONTR.simularUpdateConsumo(CONTR.currentCol);
		Instant fin = Instant.now();
		
		Duration timeElapsed = Duration.between(inicio, fin);
		
		JOptionPane.showMessageDialog(null, "Siguiente día completado, 10M de contratos actualizados! \nTiempo transcurrido: " + timeElapsed.getSeconds() + "s", "Actualización", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// Simula las inserciones en la base de datos de iberdrola
	public final void simulacionInserts()
	{
		Instant inicio = Instant.now();
		
		// Insertar 10M de clientes
		Instant startCli = Instant.now();	
		CONTR.simularClientes();
		Instant endCli = Instant.now();
		Duration timeElapsedCli = Duration.between(startCli, endCli);
		
		JOptionPane.showMessageDialog(null, "10M de clientes insertados! \nTiempo transcurrido: " + timeElapsedCli.getSeconds() + "s", "Inserción", JOptionPane.INFORMATION_MESSAGE);
		
		
		// Insertar 10M de contratos
		Instant startContr = Instant.now();
		CONTR.simularContratos();
		Instant endContr = Instant.now();
		Duration timeElapsedContr = Duration.between(startContr, endContr);

		JOptionPane.showMessageDialog(null, "10M de contratos insertados! \nTiempo transcurrido: " + timeElapsedContr.getSeconds() + "s", "Inserción", JOptionPane.INFORMATION_MESSAGE);
		
		
		// Insertar 10M de consumos mensuales
		Instant startConsumo = Instant.now();
		CONTR.simularInsertPrimerConsumo(CONTR.currentCol, 1);
		Instant endConsumo = Instant.now();
		Duration timeElapsedConsumo = Duration.between(startConsumo, endConsumo);
		
		JOptionPane.showMessageDialog(null, "10M de consumos insertados! \nTiempo transcurrido: " + timeElapsedConsumo.getSeconds() + "s", "Inserción", JOptionPane.INFORMATION_MESSAGE);
		
		
		// Muestra el total de tiempo transcurrido en segundos
		Instant finalTime = Instant.now();
		Duration total = Duration.between(inicio, finalTime);
		JOptionPane.showMessageDialog(null, "Total de tiempo transcurrido: " + total.getSeconds() + "s", "Simulación completada", JOptionPane.INFORMATION_MESSAGE);

		
	}
	
	public static final void resetUI()
	{
		menubar.removeAll();
		CONTR.mostrarColecciones(menubar);
		menubar.revalidate();
		menubar.repaint();
	}
	
	private final void init()
	{
		CONTR.mostrarColecciones(menubar);
	}
}
