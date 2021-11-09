package common.database;

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
}
