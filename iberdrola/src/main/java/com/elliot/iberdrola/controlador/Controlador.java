package com.elliot.iberdrola.controlador;

import static com.mongodb.client.model.Updates.set;
import static com.mongodb.client.model.Updates.push;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.elliot.iberdrola.conexion.Conexion;
import com.elliot.iberdrola.vista.MainUI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.UpdateResult;

public final class Controlador 
{
	// Coleccion seleccionada en la UI
	public MongoCollection<Document> currentCol;
	public MongoCollection<Document> currentFactCol;
	
	// Muestra el nombre de las colecciones en la interfaz
	public final void mostrarColecciones(JPanel parent)
	{
		int positionUI = 11;
		int increment = 55;
		
		MongoIterable<String> list = Conexion.database.listCollectionNames();
		
		for (final String collectionName: list)
		{		
			JPanel panelColeccion = new JPanel();
			panelColeccion.setBounds(10, positionUI, 177, 44);
			panelColeccion.setBackground(new Color(107, 142, 35));
			parent.add(panelColeccion);
			panelColeccion.setLayout(null);
			
			JLabel nombreLabel = new JLabel(collectionName);
			nombreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
			nombreLabel.setForeground(Color.WHITE);
			nombreLabel.setBounds(16, 15, 155, 14);
			panelColeccion.add(nombreLabel);						
			
			panelColeccion.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					String[] mesName = collectionName.split("(?=\\p{Upper})");
							
					currentCol = getColeccion(collectionName);
					currentFactCol = getColeccion("facturas" + mesName[1]);
					MainUI.labelColeccion.setText(collectionName);
					
					MainUI.deleteDoc.setVisible(true);
					MainUI.findDoc.setVisible(true);
					MainUI.insertDoc.setVisible(true);
					MainUI.updateDoc.setVisible(true);
					MainUI.btnSiguienteDia.setVisible(true);		
					MainUI.btnSimular.setVisible(true);
					MainUI.btnGenerarFacturas.setVisible(true);
				}
			});
			
			positionUI += increment;
		}
		
		parent.revalidate();
		parent.repaint();
		parent.setPreferredSize(new Dimension(0, positionUI));
	}
	
	// Recoge todos los documentos de una colección
	public final void getDocumentos(MongoCollection<Document> coleccion, String id)
	{
		boolean hasDocs = false;
		Document filter = new Document("_id", new ObjectId(id));
		
		MongoCursor<Document> cursor = coleccion.find(filter).cursor();
		
		while (cursor.hasNext()) 
		{
			hasDocs = true;
			JOptionPane.showMessageDialog(null, cursor.next(), "Documento", JOptionPane.INFORMATION_MESSAGE);   
		}
		
		if (hasDocs == false)
		{
			JOptionPane.showMessageDialog(null, "No hay documentos en esta colección", "Documentos", JOptionPane.INFORMATION_MESSAGE);   
		}
	}
	
	// Inserta un documento en una colección
	public final void insertDocumento(MongoCollection<Document> coleccion)
	{
		boolean campoRemaining = true;
		
		Document doc = new Document();
		while (campoRemaining)
		{
			String campo = JOptionPane.showInputDialog(null, "Nuevo campo: ", "Nuevo", JOptionPane.QUESTION_MESSAGE);
			String valor = JOptionPane.showInputDialog(null, "Valor del campo: ", "Valor", JOptionPane.QUESTION_MESSAGE);
			
			doc.append(campo, valor);
			
			int opcion = JOptionPane.showConfirmDialog(null, "¿Quieres añadir más campos?", "Añadir", JOptionPane.YES_NO_OPTION);
			
			if (opcion == 1)
			{
				campoRemaining = false;
			}
		}
		
		coleccion.insertOne(doc);
		JOptionPane.showMessageDialog(null, "Documento insertado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);   
		
	}
	
	// Edita el documento de una coleccion por id, campo y el nuevo valor de ese campo
		public final void updateDocumento(MongoCollection<Document> coleccion, String id, String key, String nuevoValor)
		{ 
			Bson filter = new Document("_id", new ObjectId(id));
			Bson update = set(key, nuevoValor);
			
			UpdateResult result = coleccion.updateOne(filter, update);
			JOptionPane.showMessageDialog(null, "Documentos encontrados: "+ result.getMatchedCount()
												+ "\nDocumentos actualizados: " + result.getModifiedCount(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
		}
			
		// Borra un documento de una colección
		public final void deleteDocument(MongoCollection<Document> coleccion, String id)
		{
			coleccion.deleteOne(new Document("_id", new ObjectId(id)));
			JOptionPane.showMessageDialog(null, "Documento borrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);  
		}
		
		// Devuelve una colección
		public final MongoCollection<Document> getColeccion(String nombre)
		{
			return Conexion.database.getCollection(nombre);	
		}
		
		// Crea una colección
		public final void addColeccion(String nombre)
		{
			Conexion.database.createCollection(nombre);
			JOptionPane.showMessageDialog(null, "Colección añadida a la base de datos!", "Creado", JOptionPane.INFORMATION_MESSAGE);  
		}
	

		//
		// 	SIMULACIONES
		//		
		
		
		// Inserta 10M de clientes
		public final void simularClientes()
		{
			MongoCollection<Document> coleccion = Conexion.database.getCollection("cliente");
			int clientCount = 1;
			
			for (int i = 0; i < 10; i++)
			{
				ArrayList<Document> docsCliente = new ArrayList<>();
				
				// Bucle para crear 1M de documentos
				for (int j = 0; j < 1000000; j++)
				{
					Document new_cliente = new Document();
					new_cliente.append("nombre", "Cliente " + clientCount);
					
					// Telefono random
					Random rnd = new Random();
					int randomTlf = rnd.nextInt(699999999 - 600000000) + 600000000;			 
					new_cliente.append("telefono", randomTlf);
					
					docsCliente.add(new_cliente);
					clientCount++;
					
					System.out.println(new_cliente);
				}
						
				// Inserta el array con 1M de clientes
				coleccion.insertMany(docsCliente);
			}
		}
		
		// Inserta 10M de contratos, uno para cada cliente
		public final void simularContratos()
		{
			MongoCollection<Document> coleccionClientes = Conexion.database.getCollection("cliente");
			MongoCollection<Document> coleccionContratos = Conexion.database.getCollection("contrato");
				
			final String[] tiposContrato = {"Particular simple", "Business", "Plan verano", "Plan 3 periodos", "Plan estable", "Plan solar"};
			final int limiteInserciones = 1000000;
			
			int currentPos = 0;	
				
			for (int i = 0; i < 10; i++)
			{
				MongoCursor<Document> cursor = coleccionClientes.find().limit(limiteInserciones).skip(currentPos).iterator();
				ArrayList<Document> docsContrato = new ArrayList<>();
				
				while (cursor.hasNext()) 
				{			
					// Crea el documento contrato con los datos recogidos de cliente
					for (int j = 0; j < limiteInserciones; j++)
					{						
						Document cliente = cursor.next();
						ObjectId id_cli =  (ObjectId) cliente.get("_id");
						
						Document new_contrato = new Document();
						new_contrato.append("direccion", "Dirección " + currentPos);
						new_contrato.append("id_cliente", id_cli);
						
						Random rnd = new Random();
						int randomIndex = rnd.nextInt(tiposContrato.length);
						new_contrato.append("tipo", tiposContrato[randomIndex]);
						
						docsContrato.add(new_contrato);
						currentPos++;
						
						System.out.println(new_contrato);
					}
				}	
		
				// Insertar array con 1M de documentos
				coleccionContratos.insertMany(docsContrato);
			}	
		}
		
		public final void simularInsertPrimerConsumo(MongoCollection<Document> periodo, int numDias)
		{
			MongoCollection<Document> coleccionContratos = Conexion.database.getCollection("contrato");
			MongoCollection<Document> coleccionPeriodo = periodo;
			
			final int limiteInserciones = 1000000;
			
			int currentPos = 0;	
	
			for (int i = 0; i < 10; i++)
			{
				MongoCursor<Document> cursor = coleccionContratos.find().limit(limiteInserciones).skip(currentPos).iterator();
				ArrayList<Document> docsPeriodo = new ArrayList<>();
				while (cursor.hasNext()) 
				{		
					// Crea el documento contrato con los datos recogidos de cliente
					for (int j = 0; j < limiteInserciones; j++)
					{						
						Document contrato = cursor.next();
						ObjectId id_contrato =  (ObjectId) contrato.get("_id");
						
						Document new_consumoPeriodo = new Document();
						new_consumoPeriodo.append("id_contrato", id_contrato);
						
						Random rndB = new Random();
						boolean randomBoolean = rndB.nextBoolean();
						new_consumoPeriodo.append("inv/ver", randomBoolean);
						
						// Consumo de dias con valores random
						ArrayList<ArrayList<Integer>> consumoDias = new ArrayList<>();
						for (int k = 0; k < numDias; k++)
						{
							ArrayList<Integer> horas = new ArrayList<>();
							
							Random rndZeroMulti = new Random();
							for (int h = 0; h < 24; h++)
							{
								int zeroMultiply = rndZeroMulti.nextInt(5);
								
								// 2/5 horas no tendrán consumo
								if (zeroMultiply < 2)
								{
									horas.add(0);
								}
								else
								{
									Random rndCons = new Random();
									int randomConsHora = rndCons.nextInt(499);
									horas.add(randomConsHora);
								}	
							}
							
							consumoDias.add(horas);
							
						}
						new_consumoPeriodo.append("consumoDias", consumoDias);
						
						
						// Probabilidad de que haya contratos sin generación
						Random rndHasGen = new Random();
						boolean hasGeneracion = rndHasGen.nextBoolean();
						
						if (hasGeneracion)
						{
							// Generacion por dias con valores random
							ArrayList<ArrayList<Integer>> generacionDias = new ArrayList<>();
							for (int k2 = 0; k2 < numDias; k2++)
							{			
								Random rndZeroMulti = new Random();
								ArrayList<Integer> horas = new ArrayList<>();
								for (int h = 0; h < 24; h++)
								{					
									// 4/5 de las horas no tendrán generación
									int zeroMultiply = rndZeroMulti.nextInt(5);
									
									if (zeroMultiply < 4)
									{
										horas.add(0);
									}
									else
									{
										Random rndGen = new Random();
										int randomGenHora = rndGen.nextInt(39);
										horas.add(randomGenHora);
									}
								}
								
								generacionDias.add(horas);
								
							}
							new_consumoPeriodo.append("generacionDias", generacionDias);
						}
						
						docsPeriodo.add(new_consumoPeriodo);
						currentPos++;
						
						System.out.println(new_consumoPeriodo);
					}
					
					
				}	
		
				// Insertar array con 1M de documentos
				coleccionPeriodo.insertMany(docsPeriodo);
			}
		}
		
		public final void simularUpdateConsumo(MongoCollection<Document> periodo)
		{
			MongoCollection<Document> coleccionPeriodo = periodo;
			
			// Nuevo día - consumo
			//
			// Consumo de dias con valores random		
			ArrayList<Integer> horasCons = new ArrayList<>();
			Random rndZeroMultiCons = new Random();
			for (int h = 0; h < 24; h++)
			{
				int zeroMultiply = rndZeroMultiCons.nextInt(5);
				
				// 2/5 horas no tendrán consumo
				if (zeroMultiply < 2)
				{
					horasCons.add(0);
				}
				else
				{
					Random rndCons = new Random();
					int randomConsHora = rndCons.nextInt(499);
					horasCons.add(randomConsHora);
				}	
			}	
			
			
			// Nuevo día - generación
			//
			// Probabilidad de que haya contratos sin generación
			Random rndHasGen = new Random();
			boolean hasGeneracion = rndHasGen.nextBoolean();
			
			if (hasGeneracion)
			{						
				// Generación de dias con valores random
				Random rndZeroMultiGen = new Random();
				ArrayList<Integer> horasGen = new ArrayList<>();
				for (int h = 0; h < 24; h++)
				{					
					// 4/5 de las horas no tendrán generación
					int zeroMultiply = rndZeroMultiGen.nextInt(5);
					
					if (zeroMultiply < 4)
					{
						horasGen.add(0);
					}
					else
					{
						Random rndGen = new Random();
						int randomGenHora = rndGen.nextInt(39);
						horasGen.add(randomGenHora);
					}
				}
	
				// Actualiza los documentos previamente creados, añadiendo valores de la generación en el siguiente día
				Bson updateGen = push("generacionDias", horasGen);
				Document filter = new Document();
				coleccionPeriodo.updateMany(filter, updateGen);		
			}		
			
			// Actualiza los documentos previamente creados, añadiendo valores del consumo en el siguiente día
			Bson updateCons = push("consumoDias", horasCons);	
			Document filter = new Document();
			coleccionPeriodo.updateMany(filter, updateCons);	
		}
		
		/*
		 * Prueba para probar el rendimiento de insertar uno a uno los documentos
		public final void insertOneClientes()
		{
	
			MongoCollection<Document> coleccion = Conexion.database.getCollection("cliente");
			for (int i = 0; i < 1000000; i++)
			{
				Document docCliente = new Document();
				docCliente.append("nombre", "Cliente"+i);
				Random rnd = new Random();
				int number = rnd.nextInt(999999);
				docCliente.append("telefono", number);
				coleccion.insertOne(docCliente);
			}
		}*/
}