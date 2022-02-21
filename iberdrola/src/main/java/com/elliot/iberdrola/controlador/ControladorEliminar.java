package com.elliot.iberdrola.controlador;

import javax.swing.JOptionPane;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.elliot.iberdrola.conexion.Conexion;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;

public final class ControladorEliminar 
{
	// Nombre de las colecciones
	private final String[] meses = {"periodoEnero",
									"periodoFebrero",
									"periodoMarzo",
									"periodoAbril",
									"periodoMayo",
									"periodoJunio",
									"periodoJulio",
									"periodoAgosto",
									"periodoSeptiembre",
									"periodoOctubre",
									"periodoNoviembre",
									"periodoDiciembre"};
	
	// Elimina un contrato recursivamente (consumos)
	public final void eliminarContrato(String idContrato)
	{				
		for (int i = 0; i < meses.length; i++)
		{
			MongoCollection<Document> coleccionContrato = Conexion.database.getCollection("contrato");	
			MongoCollection<Document> coleccionConsumo = Conexion.database.getCollection(meses[i]);
			
			// Filtros de borrado
			Bson filterContrato = new Document("_id", new ObjectId(idContrato));		
			Bson filterConsumo = new Document("id_contrato", new ObjectId(idContrato));		
			
			// Borrar y guardar los resultados de borrado
			DeleteResult resultCons = coleccionConsumo.deleteMany(filterConsumo);	
			DeleteResult resultContr = coleccionContrato.deleteMany(filterContrato);

			// Diálogo con información
			JOptionPane.showMessageDialog(null, "Docs. contrato borrados: " + 
				resultContr.getDeletedCount() + "\nDocs. consumo borrados: " + 
				resultCons.getDeletedCount(), "Mes: " + meses[i], JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	// Elimina un cliente recursivamente (contratos y consumos)
	public final void eliminarCliente(String idCliente)
	{	
		for (int i = 0; i < meses.length; i++)
		{
			ObjectId idContrato = null;
			
			MongoCollection<Document> coleccionCliente = Conexion.database.getCollection("cliente");
			MongoCollection<Document> coleccionContrato = Conexion.database.getCollection("contrato");
			MongoCollection<Document> coleccionConsumo = Conexion.database.getCollection(meses[i]);
			
			// Filtros de borrado
			Bson filterCliente = new Document("_id", new ObjectId(idCliente));		
			Bson filterContrato = new Document("id_cliente", new ObjectId(idCliente));			
			
			MongoCursor<Document> cursor = coleccionContrato.find(filterContrato).iterator();	
			while (cursor.hasNext())
			{
				idContrato = (ObjectId) cursor.next().get("_id");
			}
			
			// Filtro borrado consumo
			Bson filterConsumo = new Document("id_contrato", idContrato);		
			
			// Borrar y guardar los resultados de borrado
			DeleteResult resultCons = coleccionConsumo.deleteMany(filterConsumo);
			DeleteResult resultContr = coleccionContrato.deleteMany(filterContrato);
			DeleteResult resultCli = coleccionCliente.deleteMany(filterCliente);
			
			
			// Diálogo con información
			JOptionPane.showMessageDialog(null, "Docs. contrato borrados: " + 
				resultContr.getDeletedCount() + "\nDocs. consumo borrados: " + 
				resultCons.getDeletedCount() + "\nDocs. cliente borrados: " + 
				resultCli.getDeletedCount(), "Mes: " + meses[i], JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
