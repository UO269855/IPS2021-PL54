package almacenero;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.modelo.Producto;

public class algortimoAlmacenero {
	
	private String URL = "jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	private String sqlObtenerProductoPedidos =  "select producto.pos_almacen,producto.columna,producto.idproducto,producto.descripcion,producto.precio from productopedido , producto  where (productopedido.fk_idproducto = producto.idproducto and fk_idpedido = ? ) order by producto.pos_almacen,producto.columna,producto.idproducto,producto.descripcion,producto.precio";
	/**
	 * Metodo principal que nos devuelve la informacion a seguir por el almacenero para recoger los productos
	 * @throws SQLException 
	 */
	public void execute(int numPedido ) throws SQLException {
		Connection c = DriverManager.getConnection(URL,username,password);
		PreparedStatement pst = c.prepareStatement(sqlObtenerProductoPedidos);
		pst.setInt(1,4);
		ResultSet rs = pst.executeQuery();
		
		List<Producto> lp = new ArrayList<Producto>();
		while(rs.next()) {
			System.out.println( "Pasillo: " + rs.getInt(1) + "-"  + "Columna:"+ rs.getInt(2) + "- Id: " + rs.getInt(3) + "-Descripccion:" + rs.getString(4) );
			lp.add(new Producto(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
		}
		
	
	}
	

}
