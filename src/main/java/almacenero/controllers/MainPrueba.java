package almacenero.controllers;


import java.sql.SQLException;



public class MainPrueba {

	public static void main(String[] args) throws SQLException {
		int numPedido = 4;
		new GenerarDocumentacionAction().execute(numPedido);
		//new algortimoAlmacenero().execute(1);
		
	}

}
