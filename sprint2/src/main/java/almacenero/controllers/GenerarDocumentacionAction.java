package almacenero.controllers;






import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import common.database.Fichero;
import common.modelo.Pedido;
import common.modelo.Producto;



public class GenerarDocumentacionAction {
	
	private String URL = "jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	
	
	private String sql =  "update Pedido set albaran = ? where IDPedido = ?";
	private String sqlObtenerProductoPedidos =  "select productopedido.fk_idpedido,producto.idproducto,producto.descripcion,producto.precio from productopedido , producto where (productopedido.fk_idproducto=producto.idproducto and fk_idpedido = ?)";
	//En teoria no necesitaria una consulta puesto que ya tendria los datos de los productos dentro de cada paquete
	//La consulta necesaria seria para introducir los datos de los paquetes que se van a enviar
	
	public String execute(int numPedido) throws SQLException {
		
		Connection c = DriverManager.getConnection(URL,username,password);
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		int idPedido = numPedido;
		
		
		String albaran = generarAlbaran(numPedido);
		 
		
		pst = c.prepareStatement(sql);
		pst.setString(1, albaran);
		pst.setInt(2, idPedido);
		pst.executeUpdate();
		return albaran;
		
		
		
		
		
		
		
	}
	
	
	/*
	 * Metodo que se encarga de generar el albaran correspondiente al pedido y ademas añade el paquete resultante a la base de datos
	 */
	private String generarAlbaran(int numPedido) throws SQLException {
		
		
		
		
		String albaran = ""; 
		int precioTotal = 0;
		Connection c = DriverManager.getConnection(URL,username,password);
		PreparedStatement pst = c.prepareStatement(sqlObtenerProductoPedidos);
		pst.setInt(1, numPedido);
		ResultSet rs = pst.executeQuery();
		
	
		while(rs.next()) {
			//Completar alabran
			albaran += "idProducto: " + rs.getString(2) + "-" + " Descripcion: " + rs.getString(3) + "\n";
			precioTotal += rs.getInt(4);
			
		
		}
		
		return albaran;
	}
	
	
	
	
	
	
	

}
