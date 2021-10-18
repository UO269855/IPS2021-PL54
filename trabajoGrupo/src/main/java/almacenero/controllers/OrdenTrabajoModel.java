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
		//IMPORTANTE: la historia pide que muestre el tamaño del pedido, pero 
		//aun falta esa parte 
		String sql="SELECT p.idpedido, p.Fecha, p.unidadesTotales " //p.unidadesTotales no está en en bbd pero lo necesito
				+ "FROM pedido p "
				+ "LEFT JOIN ordentrabajo ot "
				+ "ON p.idpedido = ot.fk_idpedido "
				+ "WHERE ot.fk_idpedido IS NULL "
				+ "ORDER BY p.Fecha";
		
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		return rs;
	}
	

	
	/**
	 * Añada a la tabla OrdenTrabajo una fila con una nueva OT, que tendrá un IDOT
	 * generado incremental (por eso no aparece como param, ya que es el primer
	 * atributo), y el IDPedido con el que le acabamos de asociar
	 *
	 * @throws SQLException 
	 */
	public void crearOrden(int idPedido) throws SQLException  {
		PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,incidencia,albaran)"
				+ " VALUES (?,?,?)");
		pstmt.setInt(1, idPedido);
		pstmt.setNString(2, null);
		pstmt.setNString(3, null);
		pstmt.executeUpdate();
	}
	
	
	/**
	 * Asigna al almacenero la orden de trabajo que se acaba de crear
	 * @param idAlmacenero
	 * @throws SQLException 
	 */
	public void asignarOrden(int idAlmacenero) throws SQLException {
		int lastOT = getLastOT();//REVISAR ESTO
		PreparedStatement pstmt=cn.prepareStatement("INSERT INTO Almacenero (idAlmacenero, idOT) VALUES (?,?)");
		pstmt.setInt(1, idAlmacenero);
		pstmt.setInt(2, lastOT);
		pstmt.executeQuery();

		//nota: clave primaria idAlmacenero no se puede repetir: cuando un almacenero tenga mas de un pedido tendré problema
		//NOTA:se asigna la orden ya que la siguiente vez que abrimos la app y listamos, no aparece el pedido
		//consultsta de prueba:
}
	
	/*
	 * Devuelve el último ID de las ordenes de trabajo para saber como llamar
	 * a la siguiente que creamos.
	 * NOTA: Tuya me respondio que esta opción mejor no porque pierde rendimiento
	 * @return lastOT
	 */
	public int getLastOT() throws SQLException {
//		String sql = "SELECT ot.idorden "
//				+ "FROM ordenTrabajo ot "
//				+ "WHERE ot.idorden = (SELECT max(ot.idorden) FROM ordenTrabajo)" ;
		String sql ="SELECT max(idorden) FROM ordenTrabajo"; 
		PreparedStatement pstmt=cn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		return rs.getInt(1);
		
		//ERROR ACTUAL:estado del cursor incorrecto: cursor indicado no está 
		//posicionado en una fila para sentencia UPDATE, DELETE, SET, o GET: ; 
		//Posición actual del resultado de la consulta es antes del primer registro
		
	}
	
	
	//MÉTODOS PARA HISTORIA DE USUARIO 14834
	



	public ResultSet getListaProductosPedidos(int idOrden) throws SQLException {
		String sql="select p.idproducto,p.nombre, pp.unidadespedido, p.unidades, ot.incidencia "
				+ "from producto p, ordentrabajo ot , pedido p "
				+ "left join productoPedido pp "
				+ "on p.idproducto = pp.fk_idproducto "
				+ "where ot.fk_idpedido=p.idpedido AND p.idpedido=pp.fk_idpedido "
				+ "AND ot.idorden=?";
		
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idOrden);
		ResultSet rs = pstmt.executeQuery();
//		while(rs.next()) {
//			
//			System.out.println("IDProducto: " + rs.getInt(1) + " Nombre: "+ rs.getString(2) + " Unidades Pedidas: " + rs.getInt(3) + " Stock: " + rs.getInt(4));
//			//ejemplo formato de salida:
////			IDProducto: 107 Nombre: cocacola Unidades Pedidas: 3 Stock: 4
////			IDProducto: 108 Nombre: pepsi Unidades Pedidas: 6 Stock: 6
//		}
		return rs;
	}


	/** 
	 * Escribe en la incidencia de la orden de trabajo lo que falta para completar
	 * @param nombreProducto  nombre del producto
	 * @param unidadesFaltan cantidad que falta de este
	 * @param idOrden , idOrden de la OT
	 * @param viejaIncidencia, vacio si no hay incidencia, o con el mensaje de la anterior 
	 * @throws SQLException 
	 */
	public String anotarIncidencia(String nombreProducto, int unidadesFaltan, int idOrden, String viejaIncidencia) throws SQLException {
		//guardar la incidencia actual y añadirle el nuevo mensaje
		String incidencia = "De "+ nombreProducto + " se necesita " + unidadesFaltan+ " unidades\n";
		//alterar el campo ot.incidencia que es un varchar
		String sql = "UPDATE ordentrabajo "
				+ "SET incidencia=? "
				+ "WHERE ordentrabajo.idorden=?";
		
		System.out.print(incidencia);
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setNString(1, incidencia);
		pstmt.setInt(2, idOrden);

		pstmt.executeUpdate();
		
		return incidencia;
		//NOTA: si en la misma OT dos productos tienen una incidencia, esta SQL 
		//machaca la variable guardada antes
		
	}
}

