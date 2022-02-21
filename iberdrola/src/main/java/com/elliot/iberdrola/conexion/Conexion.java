package com.elliot.iberdrola.conexion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class Conexion 
{
	private static MongoClient client = MongoClients.create();
	public static MongoDatabase database = client.getDatabase("iberdrola_propuesta1"); 
}
