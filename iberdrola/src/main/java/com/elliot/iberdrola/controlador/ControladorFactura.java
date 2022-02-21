package com.elliot.iberdrola.controlador;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.elliot.iberdrola.conexion.Conexion;
import com.elliot.iberdrola.vista.FacturaUI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public final class ControladorFactura 
{
	// Devuelve precios aleatorios de kW/h
	public double[] generarPreciosHora()
	{
		Random rnd = new Random();
		
		double[] precios = 
		{			
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
			rnd.nextDouble(0.9),
		};
		
		return precios;
	}
	
	public Object[][] vectorTabla()
	{
		double[] precios = generarPreciosHora();
		
		Object[][] vector = 
		{
				{"00:00", precios[0]},
				{"01:00", precios[1]},
				{"02:00", precios[2]},
				{"03:00", precios[3]},
				{"04:00", precios[4]},
				{"05:00", precios[5]},
				{"06:00", precios[6]},
				{"07:00", precios[7]},
				{"08:00", precios[8]},
				{"09:00", precios[9]},
				{"10:00", precios[10]},
				{"11:00", precios[11]},
				{"12:00", precios[12]},
				{"13:00", precios[13]},
				{"14:00", precios[14]},
				{"15:00", precios[15]},
				{"16:00", precios[16]},
				{"17:00", precios[17]},
				{"18:00", precios[18]},
				{"19:00", precios[19]},
				{"20:00", precios[20]},
				{"21:00", precios[21]},
				{"22:00", precios[22]},
				{"23:00", precios[23]},
		};
		
		return vector;
	}
	
	// Dado un idContrato y valores de precio por hora, devuelve el precio de la factura de un día
	public final void facturaDiaria(String idContrato, String periodo, int dia,JLabel precioTotalLabel, JTable tabla)
	{		
		MongoCollection<Document> coleccionPeriodo = Conexion.database.getCollection(periodo);	
		Bson filter = new Document("id_contrato", new ObjectId(idContrato));
			
		
		// Encontrar el documento consumo por contrato
		MongoCursor<Document> cursor = coleccionPeriodo.find(filter).cursor();
		while (cursor.hasNext())
		{
			Document encontrado = cursor.next();
			
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Integer>> dias = (ArrayList<ArrayList<Integer>>) encontrado.get("consumoDias");
			
			double precioDia = 0;
			
			// Calcular el precio total del día
			// 24 horas
			for (int i = 0; i < dias.get(dia).size(); i++)
			{
				double precioH = (double) tabla.getModel().getValueAt(i, 1);
				
				double consumo = dias.get(dia).get(i);
				double precioConsumo = (consumo / 1000) * precioH;
				precioDia += precioConsumo;
			}
			
			// Precio factura
			DecimalFormat df = new DecimalFormat();	
			precioTotalLabel.setText(String.valueOf(df.format(precioDia)) + "€");
		}
	}
	
	public final void facturaPeriodo(String idContrato, String periodo, JLabel precioTotalLabel ,JTable tabla)
	{	
		MongoCollection<Document> coleccionPeriodo = Conexion.database.getCollection(periodo);	
		Bson filter = new Document("id_contrato", new ObjectId(idContrato));
		
		// Encontrar el documento consumo por contrato
		MongoCursor<Document> cursor = coleccionPeriodo.find(filter).cursor();
		while (cursor.hasNext())
		{
			Document consumoContrato = cursor.next();
			
			@SuppressWarnings("unchecked")
			ArrayList<ArrayList<Integer>> dias = (ArrayList<ArrayList<Integer>>) consumoContrato.get("consumoDias");
			
			double precioDia = 0;
			double precioTotal = 0;
			
			// Calcular el precio total del día
			// Cantidad de días
			for (int i = 0; i < dias.size(); i++)
			{
				// 24 horas
				for (int j = 0; j < dias.get(i).size(); j++)
				{
					double precioH = (double) tabla.getModel().getValueAt(j, 1);
					
					double consumo = dias.get(i).get(j);
					double precioConsumo = (consumo / 1000) * precioH;
					precioDia += precioConsumo;
				}
				
				// Suma el precio del dia al total y vuelve a generar nuevos precios por hora
				precioTotal += precioDia;
				FacturaUI.model.setDataVector(vectorTabla(), new Object[] {"Hora", "Precio kW/h"});
			}	
			
			// Precio factura
			DecimalFormat df = new DecimalFormat();
			precioTotalLabel.setText(String.valueOf(df.format(precioTotal)) + "€");
		}
	}
	
	// Genera todas las facturas de un periodo
	public final void allFacturasPeriodo(MongoCollection<Document> periodoFact, MongoCollection<Document> periodoCons)
	{
		MongoCollection<Document> coleccionFacturasPeriodo = periodoFact;	
		MongoCollection<Document> coleccionConsumosPeriodo = periodoCons;
	
		final int limiteInserciones = 1000000;
		
		int currentPos = 0;
			
		for (int i = 0; i < 1; i++)
		{
			MongoCursor<Document> cursor = coleccionConsumosPeriodo.find().limit(limiteInserciones).skip(currentPos).iterator();
			ArrayList<Document> docsFacturas = new ArrayList<>();
			
			while (cursor.hasNext())
			{			
				// Crea el documento con los datos recogidos del contrato
				for (int j = 0; j < limiteInserciones; j++)
				{
					double precioDia = 0.0;
					double precioTotal = 0.0;
					
					double[] arrayPreciosDia = generarPreciosHora();
					
					Document consumoDoc = cursor.next();
					ObjectId idContr = (ObjectId) consumoDoc.get("id_contrato");
					
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<Integer>> dias = (ArrayList<ArrayList<Integer>>) consumoDoc.get("consumoDias");
					
					// Días del periodo
					for (int dia = 0; dia < dias.size(); dia++)
					{
						// 24 horas
						for (int hora = 0; hora < dias.get(i).size(); hora++)
						{
							double consumo = dias.get(dia).get(hora);
							double precioH = arrayPreciosDia[hora];
							
							double precioConsumo = (consumo / 1000) * precioH;
							precioDia += precioConsumo;
						}
						
						// Suma el precio del dia al total y vuelve a generar nuevos precios por hora
						precioTotal += precioDia;
					}
					
					Document new_factura = new Document();
					new_factura.append("id_contrato", idContr);					
					new_factura.append("precio_total", precioTotal);
					
					docsFacturas.add(new_factura);
					currentPos++;
					
					System.out.println(new_factura);
				}
			}
			
			// Insertar el array con 1M de documentos
			coleccionFacturasPeriodo.insertMany(docsFacturas);
		}
	}
}
