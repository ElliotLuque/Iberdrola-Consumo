package com.elliot.iberdrola;

import java.util.logging.Level;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.elliot.iberdrola.vista.MainUI;

public class App 
{
	public static void main( String[] args )
    {
		// Deshabilitar logging de MongoDB
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
		
		MainUI main = new MainUI();
		main.setVisible(true);
		
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());	
		} 		
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) 
		{
			System.out.println("Error al definir el LookAndFeel");
			
		}
    }
}
