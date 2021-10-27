package giis.demo.util;
import java.io.FileWriter;
import java.io.PrintWriter;

import common.modelo.Pedido;

public class EscribirFichero
{
    public static void main(String[] args,Pedido pedido)
    {
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("albaran.txt:" + pedido.getIdPedido());
            pw = new PrintWriter(fichero);
            pw.println(pedido.getAlbaran());

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

