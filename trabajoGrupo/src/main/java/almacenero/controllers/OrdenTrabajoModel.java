package almacenero.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.table.TableModel;

import common.database.DbUtil;
import common.modelo.Pedido;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.Util;

/**
 * Acceso a los datos de OrdenTrabajo y Pedido
 * (en esta clase se hacen las consultas)
 * @author Ali
 *
 */
public class OrdenTrabajoModel {

//	private Database db=new Database();
	
	private String URL ="jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	
	private Connection cn;
	
	
	
	public OrdenTrabajoModel() throws SQLException {
		this.cn = DriverManager.getConnection(URL, username, password);
		
	}


	/**
	 * 
	 * Implementacion usando la utilidad que obtiene una lista de arrays de objetos 
	 * resultado de la ejecucion de una query sql
	 *
	 * @return lista de pedidos que aun están pendientes: con su id, fecha y tamaño
	 * ordenados por fecha
	 * @throws SQLException 
	 * 
	 * ANTES "List<Object[]>"
	 */
	public ResultSet getListaPedidosArray() throws SQLException {
//		String sql="SELECT p.idpedido, p.Fecha" //p.unidadesTotales no está en en bbd pero lo necesito
//					+ " FROM pedido p"
//					+ "LEFT JOIN ordentrabajo ot"
//					+ " ON p.idpedido ==ot.idpedido"
//					+ "WHERE ot.idpedido IS NULL "
//					+ "ORDER BY p.Fecha";
		
		//IMPORTANTE: la historia pide que muestre el tamaño del pedido, pero 
		//aun falta esa parte 
		String sql="SELECT p.idpedido, p.Fecha " //p.unidadesTotales no está en en bbd pero lo necesito
				+ "FROM pedido p "
				+ "LEFT JOIN ordentrabajo ot "
				+ "ON p.idpedido = ot.fk_idpedido "
				+ "WHERE ot.fk_idpedido IS NULL "
				+ "ORDER BY p.Fecha";
		
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		return rs;
	}
	
//	
//	/**
//	 * Obtiene la lista de peddios pendientes en forma objetos para una fecha de inscripcion dada
//	 */
//	public List<Pedido> getListaPedidos() {
//		String sql="SELECT p.idpedido, p.Fecha, p.UnidadesTotales "
//				+ " FROM pedido p"
//				+ "LEFT JOIN ordentrabajo ot"
//				+ " ON p.idpedido == ot.idpedido"
//				+ "WHERE ot.idpedido IS NULL "
//				+ "ORDER BY p.Fecha";
//		return db.executeQueryPojo(Pedido.class, sql);
//	}
//	
//	
//	/**
//	 * Devuelve el último ID de las ordenes de trabajo para saber como llamar
//	 * a la siguiente que creamos.
//	 * NOTA: Tuya me respondio que esta opción mejor no porque pierde rendimiento
//	 * @return lastOT
//	 */
//	public int getLastOT() {
//		String sql = "SELECT ot.idot"
//				+ "FROM ordenTrabajo ot"
//				+ "WHERE ot.idot = (SELECT max(ot.idot) FROM ordenTrabajo)" ;
//		
//		List<Object[]>rows=db.executeQueryArray(sql);
//		//determina el valor a devolver o posibles excepciones
//		if (rows.isEmpty())//si no hay ot
//			return 0;
//		else//devuelve el valor
//			return (int)rows.get(0)[0];
//	}
//	
//	/**
//	 * Añada a la tabla OrdenTrabajo una fila con una nueva OT, que tendrá un IDOT
//	 * generado incremental (por eso no aparece como param, ya que es el primer
//	 * atributo), y el IDPedio con el que le acabamos de asociar
//	 */
//	public void crearOrden(int idPedido) {
//		//LOS PARÁMETROS NO SE PONEN ASI
////		db.executeQueryArray(sql, idPedido);
////		db.executeUpdate("INSERT INTO ordenTrabajo (idOt, IDPedido) VALUES ('"+idPedido+"' )");
//		db.executeUpdate("INSERT INTO ordenTrabajo (idOt, IDPedido) VALUES (%d)", idPedido);
//
//
//		//ahora devuelve el valor de la IDOT que acabas de añadir:
//		
//		
//	}
//	
//	public void asignarOrden(String idAlmacenero) {
//		int lastOT = getLastOT();//aunque baje el rendimiento
////		db.executeQueryArray(sql, idAlmacenero, lastOT);
//		db.executeUpdate("INSERT INTO Almacenero (idAlmacenero, idOT) VALUES ('"+idAlmacenero+"','"+lastOT+"' )");
//
//		//nota: clave primaria idAlmacenero no se puede repetir
//		//LOS PARÁMETROS NO SE PONEN ASI
//	}
}
