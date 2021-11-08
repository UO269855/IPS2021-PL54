package almacenero.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import common.database.Database;
import common.database.DbUtil;
import common.modelo.ProductoDisplayEscaner;
import giis.demo.util.Util;
/**
 * Acceso a los datos de OrdenTrabajo y Pedido
 * (en esta clase se hacen las consultas)
 * @author Alicia Fernández Pushkina
 *
 */
public class OrdenTrabajoModel {
	private String URL ="jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	
	private Connection cn;
	
	
	
	/**
	 * Cosntructor de la clase model que maneja las consultas.
	 * @throws SQLException
	 */
	public OrdenTrabajoModel() throws SQLException {
		this.cn = DriverManager.getConnection(URL, username, password);//inicia la conexion
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
		String sql="SELECT p.idpedido, p.Fecha, p.unidadesTotales " 
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
		int unidadesARecoger = cuentaUnidadesPedido(idPedido);
		System.out.println("Unidades por recoger: "+unidadesARecoger);
		PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,incidencia,albaran,unidadesARecoger)"
				+ " VALUES (?,?,?,?)");
		pstmt.setInt(1, idPedido);
		pstmt.setNString(2, null);
		pstmt.setNString(3, null);
		pstmt.setInt(4, unidadesARecoger); //añade a la OT el número de unidadesARecoger, que son el mismo que las uniadesTotales de pedido
		pstmt.executeUpdate();
	}
	
	
	/**
	 * Método privado que devuelve el número de unidadesTotales del pedido
	 * @param idPedido
	 * @return
	 * @throws SQLException 
	 */
	private int cuentaUnidadesPedido(int idPedido) throws SQLException {
		//cuando se divida un pedido en varias ot, quizá esto cambie
		String sql = "select unidadesTotales "
				+ "from pedido "
				+ "where idpedido = ?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idPedido);
		ResultSet rs = pstmt.executeQuery();
		System.out.println("llega hasta aqui");
		while(rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}


	/**
	 * Asigna al almacenero la orden de trabajo que se acaba de crear
	 * @param idAlmacenero
	 * @throws SQLException 
	 */
	public void asignarOrden(int idAlmacenero) throws SQLException {
		int lastOT = getLastOT();
		PreparedStatement pstmt=cn.prepareStatement("INSERT INTO Almacenero (idAlmacenero, fk_idorden) VALUES (?,?)");
		pstmt.setInt(1, idAlmacenero);
		pstmt.setInt(2, lastOT);
		pstmt.executeUpdate();
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
//		String sql ="SELECT max(idorden) FROM ordenTrabajo"; 
		String sql = "select idorden "
				+ "from ordenTrabajo "
				+ "order by idorden desc";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int lastOt = rs.getInt(1);
		System.out.println(lastOt);
		return lastOt;
		
		//ERROR ACTUAL:estado del cursor incorrecto: cursor indicado no está 
		//posicionado en una fila para sentencia UPDATE, DELETE, SET, o GET: ; 
		//Posición actual del resultado de la consulta es antes del primer registro
		//SOLUCION DEL ERRO--> NEXT()
		
	}
	


	/**
	 * Devuelve una lista con los productos pedidos a partir de una idOrden.
	 * @param idOrden, orden de la que queremos sacar sus productos pedidos
	 * @return rs, resultSet con sus productos pedidos
	 * @throws SQLException
	 */
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

//POJOS NO ME FUNCIONÓ
//	public List<ProductoDisplayEscaner> getListaProductosEscaner(int idOrden) throws SQLException{
//		String sql = "select p.idproducto,p.nombre, pp.unidadespedido "
//				+ "from producto p, ordentrabajo ot , pedido p "
//				+ "left join productoPedido pp "
//				+ "on p.idproducto = pp.fk_idproducto "
//				+ "where ot.fk_idpedido=p.idpedido AND p.idpedido=pp.fk_idpedido AND ot.idorden=?";
//		return DbUtil.executeQueryPojo(ProductoDisplayEscaner.class, sql);
//	}
	
	/**
	 * Muestra la lista de productos de la ot seleccionada para poder escanearlo
	 * @param idOrden
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getListaProductosEscaner(int idOrden) throws SQLException{
		String sql = "select p.idproducto,p.nombre, pp.unidadespedido, pp.unidadesPorRecoger " //AÑADIR pp.unidadesPorRecoger
				+ "from producto p, ordentrabajo ot , pedido p "
				+ "left join productoPedido pp "
				+ "on p.idproducto = pp.fk_idproducto "
				+ "where ot.fk_idpedido=p.idpedido AND p.idpedido=pp.fk_idpedido AND ot.idorden=?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idOrden);
		ResultSet rs = pstmt.executeQuery();
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
	public String comprobarStock(String nombreProducto, int unidadesFaltan, int idOrden, String viejaIncidencia) throws SQLException {
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
	}


	public  int getIdPedido(int idOrden) throws SQLException {
		String sql = "select fk_idpedido from ordentrabajo where idorden = ?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idOrden);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			return rs.getInt(1);
		}
		return -1;
		
	}


	/**
	 * Comprueba si las unidadesPorRecoger del producto pedido son coherentes con las que intento escanear.
	 * Si hay suficientes, se descuenta "unidadesSpinner" veces el campo unidadesPorRecoger. (return 0)
	 * Si intento escanear más de las que faltan, no se puede --> muestra un mensaje de error (return -1)
	 * Si tras escanear ese producto ya no quedan unidades por recoger, (return 1)
	 * 
	 * @param idProducto
	 * @param idOrden
	 * @param unidadesSpinner
	 * @return -1:escaneo fallido	0: escaneó bien pero ya no queda más de ese producto	1:escaneó bien y aun queda por recoger
	 * @throws SQLException 
	 */
	public int escanear(int idProducto,int idOrden, int unidadesSpinner) throws SQLException {
		int uPorRecoger = unidadesPorRecoger(idProducto, idOrden);
		//si uPorRecoger < unidadesSpinner --> no se puede escanear, muestra mensaje de error
		if(uPorRecoger < unidadesSpinner) {
			return -1;
		}
		//si uPorRecoger = unidadesSpiner --> tras escanear, ya no quedará más que recoger para este producto
		if(uPorRecoger == unidadesSpinner) {
			int nuevasUnidades = uPorRecoger-unidadesSpinner;
			descontarUnidadesProducto(idProducto,idOrden, nuevasUnidades);
			descontarUnidadesOrden(idOrden,unidadesSpinner);
			return 0;
		}
		//si uPorRecoger > unidadesSpinerr --> se escanea y sigue disponible para escanear
		int nuevasUnidades = uPorRecoger-unidadesSpinner;
		descontarUnidadesProducto(idProducto,idOrden, nuevasUnidades);
		descontarUnidadesOrden(idOrden, unidadesSpinner);
		return 1;
	}
	
	
	/**
	 * Método privado para descontar las unidades que recoger de la OT, sin especificar para qué producto
	 * @param idOrden 
	 * @param unidadesSpinner, unidades que descontamos
	 * @throws SQLException 
	 */
	private void descontarUnidadesOrden(int idOrden, int unidadesSpinner) throws SQLException {
		int unidadesARecoger = unidadesARecoger(idOrden);
		int nuevasUnidades = unidadesARecoger - unidadesSpinner;
		
		String sql = "UPDATE ordentrabajo "
				+"SET unidadesarecoger = ? "
				+ "where idorden = ?";
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, nuevasUnidades);
		pstmt.setInt(2, idOrden);
		pstmt.executeUpdate();
	
		
	}

	/**
	 * Método  que devuelve las unidades totales a recoger en la OT que le pasemos
	 * @param idOrden
	 * @return unidades totales a recoger, o -1 si no hay resultados
	 * @throws SQLException
	 */
	public int unidadesARecoger(int idOrden) throws SQLException {
		String sql ="select unidadesarecoger "
			+ "from ordentrabajo ot "
			+ "where ot.idorden = ?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idOrden);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}


	/**
	 * Método privado que descuenta las unidades correspondientes a "unidadesPorRecoger" en la base de datos, para 
	 * la tabla ProductoPedido. Támbien descuenta las "unidadesTotalesPorRecoger" de OT
	 * @param idProducto
	 * @param idOrden
	 * @param unidadesSpinner
	 * @param nuevasUnidades , valor actualizado
	 * @throws SQLException 
	 */
	private void descontarUnidadesProducto(int idProducto, int idOrden, int nuevasUnidades) throws SQLException {
		//actualiza unidadesPorRecoger de productoPedido NO FUNCIONA
//		String sql = "UPDATE productoPedido "
//				+ "SET unidadesporrecoger=? "
//				+ "where  ot.idorden = ? and pp.fk_idproducto=? "
//				+ "and ot.fk_idpedido = pp.fk_idpedido	";
		String sql = "update productopedido "
				+ "set unidadesporrecoger = ? "
				+ "where fk_idproducto=? and fk_idpedido=?";
		
		int idPedido = getIdPedido(idOrden);//a través de idorden llegamos al idpedido para actualizar el producto
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, nuevasUnidades);
		pstmt.setInt(2, idProducto);
		pstmt.setInt(3, idPedido);
		pstmt.executeUpdate();
		
		//FALTA ACTUALIZAR EL TOTAL DE LA OT
	}


	/**
	 * Método privado que devuelve las unidades que quedan por recoger del producto correspondiente a la orden
	 * @param idProducto
	 * @param idOrden
	 * @return devuelve unidadesPorRecoger, o -1 si ha habido algún error(si no hay resultados)
	 * @throws SQLException
	 */
	private int unidadesPorRecoger(int idProducto,int idOrden) throws SQLException {
		String sql = "select pp.unidadesporrecoger "
				+ "from productoPedido pp, ordenTrabajo ot "
				+ "where  ot.idorden = ? and pp.fk_idproducto=? "
				+ "and ot.fk_idpedido = pp.fk_idpedido";
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idOrden);
		pstmt.setInt(2, idProducto);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}


	/**
	 * Anota en la OT seleccionada, la incidencia escrita por el almacenero
	 * @param idOrden
	 * @param incidencia
	 * @throws SQLException 
	 */
	public void anotarIncidencia(int idOrden, String incidencia) throws SQLException {
		String sql = "UPDATE ordentrabajo "
				+ "SET incidencia=? "
				+ "WHERE ordentrabajo.idorden=?";
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setNString(1, incidencia);
		pstmt.setInt(2, idOrden);
		pstmt.executeUpdate();
		System.out.println("la incidencia fue: " + incidencia);
	}
	
	
	
}

