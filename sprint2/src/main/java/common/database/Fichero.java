package common.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Fichero {
	public static void main(String email,String comprobanteOperacion)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("PagoEmpresa(" + email+").txt");
            pw = new PrintWriter(fichero);

          
             pw.println(comprobanteOperacion);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
	public static void contrarembolso(String direccion,String comprobanteOperacion)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("PagoContrarembolso(" + direccion+").txt");
            pw = new PrintWriter(fichero);

          
             pw.println(comprobanteOperacion);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
	public static void transferencia(String direccion,String comprobanteOperacion)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("PagoTransferencia(" + direccion+").txt");
            pw = new PrintWriter(fichero);

          
             pw.println(comprobanteOperacion);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
	
	 public static double leerTarjeta(String numTarjeta) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      double cantidadDisponible = 0;

	      try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	         archivo = new File ("TarjetasCredito.txt");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         // Lectura del fichero
	         String linea;
	         while((linea=br.readLine())!=null) {
	        	 String[] lineaPartida = linea.split("-");
	        	 if(lineaPartida[0].equals(numTarjeta)) {
	        		 cantidadDisponible = Integer.parseInt(lineaPartida[1]);
	        	 }
	         }
	          
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	      return cantidadDisponible;
	   }
	
}
