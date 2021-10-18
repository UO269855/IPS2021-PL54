package almacenero;


import java.sql.SQLException;



public class MainPrueba {

	public static void main(String[] args) throws SQLException {
		int numPedido = 1;
		new GenerarDocumentacionAction().execute(numPedido);
		
	}

}
