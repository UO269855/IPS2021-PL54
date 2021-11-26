package almacenero.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import com.mchange.v1.db.sql.PSManager;

import common.database.Database;
import common.database.DbUtil;
/**
 * Acceso a los datos de OrdenTrabajo y Pedido
 * (en esta clase se hacen las consultas)
 * @author Alicia FernÃ¡ndez Pushkina
 *
 */
public class OrdenTrabajoModel {
	private String URL ="jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	
	private Connection cn;
	
	private static int TAM_MEDIO = 10;
	
	
	
	
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
	 * @return lista de pedidos que aun estÃ¡n pendientes: con su id, fecha y tamaÃ±o
	 * ordenados por fecha
	 * @throws SQLException 
	 * 
	 * NOVEDAD: LOS PEDIDOS POR TRASNFERENCIA YA NO SE MUESTRAN
	 */
	public ResultSet getListaPedidosArray() throws SQLException {
		//pedido pendiente: hasta ahora comprobaba que ot.fk_idpedido fuera null (ot.fk_idpedido IS NULL)
		//una OT tiene mÃ¡s de un pedido asÃ­ que no me sirve	--> probar con Pedido.FK_IDOrden     
		String sql="SELECT p.idpedido, p.Fecha, p.unidadesTotales " 
				+ "FROM pedido p "
				+ "LEFT JOIN ordentrabajo ot "
				+ "ON p.idpedido = ot.fk_idpedido "
				+ "WHERE p.fk_idorden is null "			//si tiene mÃ¡s de una orden nos sirve igualmente
				+ "AND p.metodopago != 'Transferencia' "
				+ "ORDER BY p.Fecha";
		
		//IMPORTANTE: TRAS FUSIONAR DOS PEDIDOS PEQUEÃ‘OS EN UNA OT, LOS EXTRAS SIGUEN APARECIENDO 
		//COMO PENDIENTES CUANDO NO SE DEBERÃ�A
		PreparedStatement pstmt=cn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		return rs;
	}
	
	public ResultSet getListaOrdenes(int idPedido) throws SQLException {
		String sql="select o.idorden,o.unidadesarecoger "
				+ "from ordentrabajo o "
				+ "where fk_idpedido=?";
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idPedido);
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}
	

	

	
	/**
	 * NUEVO CREA ORDEN: AHORA OPTIMIZADAS
	 * AÃ±ada a la tabla OrdenTrabajo una fila con una nueva OT, que tendrÃ¡ un IDOT
	 * generado incremental (por eso no aparece como param, ya que es el primer
	 * atributo), y el IDPedido con el que le acabamos de asociar
	 * @param idAlmacenero 
	 *
	 * @throws SQLException 
	 */
	public void crearOrden(int idPedido, int idAlmacenero) throws SQLException  {
		int unidadesTotales = cuentaUnidadesPedido(idPedido);//cuenta el tamaÃ±o del pedido
		
	
		if(unidadesTotales < TAM_MEDIO) //PEDIDOS PEQUEÃ‘OS
			ordenPedidoPequeno(idPedido, idAlmacenero, unidadesTotales);
			
		else if(unidadesTotales > TAM_MEDIO) //CREAR DIFERENTES OT
			ordenPedidoGrande(idPedido, idAlmacenero, unidadesTotales);
		
		else if(unidadesTotales == TAM_MEDIO) //tamaÃ±o ideal
			ordenPedidoIdeal(idPedido, idAlmacenero, unidadesTotales);
		
	}


	
	/**
	 * Crea tantas ordenes como hagan falta para dividir el tamaÃ±o del pedido grande en ordenes 
	 * con un tamaÃ±o parecido al TAM_MEDIO
	 * 
	 * 
	 * V 0:  crea una orden para cada producto del pedido. EstÃ¡ bien si es el caso 8/5. Pero no sirve si 
	 * fuera 8/1/1/2, porque tener 4 ordenes de trabajo no es lo mejor
	 * V1: divide en ProductoOrden bien las diferentes unidades del producto como se explica en el redkanban.Faltan asignar al pedido
	 * para que no se muestre en pendiente y que se pueddan listar los productos
	 * @param idPedido
	 * @param idAlmacenero
	 * @param unidadesTotales
	 * @throws SQLException 
	 */
	private void ordenPedidoGrande(int idPedido, int idAlmacenero, int unidadesTotales) throws SQLException {
		
		ResultSet productosPedidos = productosPedidosParaIterar(idPedido);
		
		
		int idProductoIter = 0;
		int unidadesIter = 0;
		int idOrdenActual = 0;
		int contador = 0;
		int contadorProd = 0;
		
		
		while(productosPedidos.next() ) { //&& O || productosPedidos.next()  RECORRE HASTA QUE NO SE LLENE A OT
			idProductoIter = productosPedidos.getInt(1);
			unidadesIter = productosPedidos.getInt(2);
			contadorProd = 0; //se resetea al cambiar de producto;
			
			if(contador == 0) {//crea la orden
				creaOrdenInicial(idPedido, idAlmacenero, unidadesIter);//en realidad si el primer producto tiene mÃ¡s unidades no serÃ­a correcto pero despuÃ©s se modifica
				idOrdenActual = getLastOT();
			}
			
			
			for (int i = 0; i < unidadesIter; i++) {//recorre el producto tantas veces como unidades tenga
				//comprobar el tamaÃ±o de la orden: si llegÃ³ a TAM_MEDIO inlcuido, crear otra orden. Si no, seguir con la misma
				if(contador == TAM_MEDIO){//0...10, si llega a 10 ya es el unde
					creaOrdenInicial(idPedido, idAlmacenero, unidadesIter);
					idOrdenActual = getLastOT();
					contador = 0;
					contadorProd = 0;
				}
				
				//aÃ±adir una unidad en cada iteracion a la ot
				anadirUnaUnidadAOT(idPedido,idProductoIter,idOrdenActual,contadorProd);//crea una fila de productoOrden ()antes usaba i
				asignarOrdenAPedido(idPedido,idOrdenActual);
				sumarTamanoPedidos(idOrdenActual,contador+1);//le da el tamaÃ±o  a la orden
				
				
				contador++;
				contadorProd++;
			}
			
			
		}
			
			
		
}

	
	/**
	 * 
	 * Para los pedidos grandes
	 * Asocia los productospedidos con la orden de trabajo y sus unidades correspondientes
	 * 
	 * @param idPedido
	 * @param idProductoIter
	 * @param idOrdenActual
	 * @param i, el nÃºmero de unidades que tendremos de ese producto en la orden
	 * @throws SQLException 
	 */
	private void anadirUnaUnidadAOT(int idPedido, int idProductoIter, int idOrdenActual, int i) throws SQLException {
		int aux = i+1;
		boolean res = compruebaProductoOrdenExistente( idPedido,  idProductoIter,  idOrdenActual);
		if(!res) {//si no existe el producto --> se cre la tabla
			anadeProductoOrden(idPedido, idProductoIter, idOrdenActual, aux);
		}
		else {
			incrementaProductoOrden(idPedido, idProductoIter, idOrdenActual, aux);

		}
	}


	private void anadeProductoOrden(int idPedido, int idProductoIter, int idOrdenActual, int unidades) throws SQLException {
		String sql = "INSERT INTO productoOrden(fk_idproducto, fk_idpedido,idorden, unidadesEnOrden, unidadesPorRecoger)"
				+ " VALUES(?,?,?,?,?)";
		
		PreparedStatement pstmt = cn.prepareStatement(sql);
		pstmt.setInt(1, idProductoIter);
		pstmt.setInt(2, idPedido);
		pstmt.setInt(3, idOrdenActual);
		pstmt.setInt(4, unidades);
		pstmt.setInt(5, unidades);

		
		pstmt.executeUpdate();
	}
	
	private void incrementaProductoOrden(int idPedido, int idProductoIter, int idOrdenActual, int unidades) throws SQLException {
		String sql = "UPDATE productoOrden "
				+ "SET unidadesEnOrden = ? ,"
				+ "unidadesPorRecoger = ? "
				+ "WHERE fk_idproducto = ? "
				+ "AND fk_idpedido = ? "
				+ "AND idorden = ? ";
		
		PreparedStatement pstmt = cn.prepareStatement(sql);
		pstmt.setInt(1, unidades);
		pstmt.setInt(2, unidades);

		pstmt.setInt(3, idProductoIter);
		pstmt.setInt(4, idPedido);
		pstmt.setInt(5, idOrdenActual);
		
		pstmt.executeUpdate();
	}


	/** SI devuelve true, ya existe un producto como este en la tabla, luego no creara otra fila igual
	 * @param idPedido
	 * @param idProductoIter
	 * @param idOrdenActual
	 * @return
	 * @throws SQLException 
	 */
	private boolean compruebaProductoOrdenExistente(int idPedido, int idProductoIter, int idOrdenActual) throws SQLException {
		String sql = "SELECT * "
				+ "from productoOrden "
				+ "WHERE fk_idproducto = ? "
				+ "and fk_idpedido = ? "
				+ "and idorden = ? "
				;
		
		PreparedStatement pstmt = cn.prepareStatement(sql);
		pstmt.setInt(1, idProductoIter);
		pstmt.setInt(2, idPedido);
		pstmt.setInt(3, idOrdenActual);
		ResultSet rs = pstmt.executeQuery();

		if(!rs.next())//si no hay resultado --> no exsite
			return false;
		else 
			return true;
	}


	
	
	


	/**
	 * Devuelve los productos del pedido con formato fk_idproducto, unidadespedido
	 * @param idPedido
	 * @return
	 * @throws SQLException
	 */
	private ResultSet productosPedidosParaIterar(int idPedido) throws SQLException {
		String sql = "SELECT fk_idproducto,unidadespedido "
				+ "FROM productopedido "
				+ "WHERE fk_idpedido = ?";
		PreparedStatement pstmt = cn.prepareStatement(sql);
		pstmt.setInt(1, idPedido);
		ResultSet productosPedidos = pstmt.executeQuery();
		return productosPedidos;
	}


	/**
	 * Crea la orden tan grande como sea posible segÃºn el TAM_MEDIO cuando se seleccionÃ³ un pedido pequeÃ±o.
	 * Fusiona distintos pedidos.
	 * @param idPedido
	 * @param idAlmacenero
	 * @param unidadesTotales
	 * @throws SQLException
	 */
	private void ordenPedidoPequeno(int idPedido, int idAlmacenero, int unidadesTotales) throws SQLException {
		creaOrdenInicial(idPedido, idAlmacenero, unidadesTotales);
		int idOrden =  getLastOT();
		asignarOrdenAPedido(idPedido,idOrden);//asigna la orden creada al pedido que acaba de seleccionar
		asignarProductosAOT(idOrden,idPedido);//asocia los productos del pedido a esa orden
		//tengo que asignarle a idOrden todos los productos de idPedido
		
		
		ResultSet pedidosAIterar = pedidosPendientesParaIterar(idPedido);//lista con los pedidos pendientes (saltandose a sÃ­ mismo)
		

		//inicializa los valores de los pedidos a iterar
		int tamPedIter = 0;
		int idPedidoIter = 0;
		int tamActual= unidadesTotales;//partimos del tamaÃ±o de la OT creada con el primer pedido
		
		

		while(pedidosAIterar.next()) {
			idPedidoIter = pedidosAIterar.getInt(1);//idPedido iterado
			tamPedIter = pedidosAIterar.getInt(2);//tamaÃ±o pedido iterado
			
			//si la suma del tamaÃ±o actual + el pedido pendiente iterando es <TAM_MEDIO, se AÃ‘ADE ese pedido entero
			if(tamActual + tamPedIter <= TAM_MEDIO) {
				sumarTamanoPedidos(idOrden,tamActual+tamPedIter);//suma el tamaÃ±o
				asignarProductosAOT(idOrden, idPedidoIter );//aÃ±ade los productos de este pedido a la Ãºltima orden creada
				tamActual = tamActual+tamPedIter;//incrementa el tamaÃ±o de la ot actual
				asignarOrdenAPedido(idPedidoIter,idOrden);//marca este pedido como no pendiente para que no se muestre despuÃ©s
				
			}
		}
	}


	/**
	 * Crea la orden de trabajo para los pedidos con el tamaÃ±o exacto al TAM_MEDIO preeestablecido.
	 * @param idPedido
	 * @param idAlmacenero
	 * @param unidadesTotales
	 * @throws SQLException
	 */
	private void ordenPedidoIdeal(int idPedido, int idAlmacenero, int unidadesTotales) throws SQLException {
		creaOrdenInicial(idPedido, idAlmacenero, unidadesTotales);
		int idOrden =  getLastOT();
		asignarOrdenAPedido(idPedido,idOrden);//asigna la orden creada al pedido que acaba de seleccionar
		asignarProductosAOT(idOrden,idPedido);//asocia los productos del pedido a esa orden
	}


	/**
	 * 
	 * Itera sobre los pedidos pendientes por orden de fecha. Salta aquellos que tengan asociada una OT y al pedido
	 * cuyo id se pasa por parÃ¡metro, para no iterar sobre sÃ­ mismo. 
	 * @param idPedido
	 * @return
	 * @throws SQLException
	 */
	private ResultSet pedidosPendientesParaIterar(int idPedido) throws SQLException {
		String sql = "SELECT p.idpedido, p.unidadesTotales "
				+ "FROM pedido p "
				+ "LEFT JOIN ordentrabajo ot "
				+ "ON p.idpedido = ot.fk_idpedido "
				+ "WHERE p.fk_idorden is null "
				+ "AND p.metodopago != 'Transferencia'"
				+ "AND p.idpedido != ? "
				+ "ORDER BY p.Fecha";
		
		PreparedStatement pstmt2=cn.prepareStatement(sql);
		pstmt2.setInt(1, idPedido);//evita que itere sobre sÃ­ mismo
		ResultSet rs = pstmt2.executeQuery();
		return rs;
	}


	/**
	 * Para los pedidos pequeÃ±os crea la orden inicial a partir del pedido que haya seleccionado el cliente.
	 * Segun se itere despues, si se fusiona con otros pedidos se modificarÃ¡ esta OT
	 * @param idPedido
	 * @param idAlmacenero
	 * @param unidadesTotales, unidades a recoger de la OT
	 * @throws SQLException
	 */
	private void creaOrdenInicial(int idPedido, int idAlmacenero, int unidadesTotales) throws SQLException {
		PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,fk_idalmacenero,incidencia,albaran,unidadesARecoger)"
				+ " VALUES (?,?,?,?,?)");
		pstmt.setInt(1, idPedido);
		pstmt.setInt(2, idAlmacenero);
		pstmt.setNString(3, null);
		pstmt.setNString(4, null);
		pstmt.setInt(5, unidadesTotales); //aÃ±ade a la OT el nÃºmero de unidadesTotales, que son el mismo que las uniadesTotales de pedido
		pstmt.executeUpdate();
	}
	
	
	/**
	 * Asigna en la base de datos "idOrden" al campo Pedido.fk_idorden del pedido cuyo id se le pasa por param
	 * Al actualizar este dato, ya no aparece listado como pedido pendiente
	 * @param idPedido
	 * @param idOrden
	 * @throws SQLException 
	 */
	private void asignarOrdenAPedido(int idPedido, int idOrden) throws SQLException {
		String sql = "UPDATE pedido "
				+ "SET fk_idorden =  ? "
				+ "WHERE idPedido = ?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idOrden);
		pstmt.setInt(2, idPedido);
		pstmt.executeUpdate();
	}


/**
 * MÃ©todo privado para asignar los productos del pedido a la ot correspondiente
 * actualmente TODOS los productos del pedido se asignan a la misma orden. No estoy teniendon en cuenta divisiones
 * @param idOrden
 * @param idPedido
 * @throws SQLException 
 */
private void asignarProductosAOT(int idOrden, int idPedido) throws SQLException {
	
	//NO SE USARÃ� ESTE MÃ‰TODO PORQUE AHRA TENEMOS LA TABLA PRODUCTOORDEN, CREADA PPOR LOS PEDIDOS GRANDES
	//1) actualizar los productos pedidos de arriba para que el campo fk_idorden sea el parÃ¡metro
//	String sql = "UPDATE productopedido "
//			+ "SET fk_idorden = ? "
//			+ "WHERE fk_idpedido = ?";
//	PreparedStatement pstmt = cn.prepareStatement(sql);
//	pstmt.setInt(1, idOrden);
//	pstmt.setInt(2, idPedido);
//	pstmt.executeUpdate();
	
	//1)saca todos los productos de este pedido
	ResultSet productosPedidos = productosPedidosParaIterar(idPedido);
	//2) itera por ellos
	int idProductoIter = 0;
	int unidadesIter = 0;
	while(productosPedidos.next()) {
		idProductoIter = productosPedidos.getInt(1);
		unidadesIter = productosPedidos.getInt(2);
		//3)cada producto lo aÃ±ade a la misma orden
		anadeProductoOrden(idPedido, idProductoIter, idOrden, unidadesIter);
		
	}
	
	
	
	
}


private void sumarTamanoPedidos(int idOrden, int tamOT) throws SQLException {
	String sql = "UPDATE ordenTrabajo "
			+ "SET ordentrabajo.unidadesarecoger = ? "//aÃ±adir los productos
			+ "WHERE ordentrabajo.idorden = ?";
	
	PreparedStatement pstmt=cn.prepareStatement(sql);
	pstmt.setInt(1, tamOT);
	pstmt.setInt(2, idOrden);
	pstmt.executeUpdate();


	
}




	/**
	 * MÃ©todo privado que devuelve el nÃºmero de unidadesTotales del pedido
	 * @param idPedido
	 * @return
	 * @throws SQLException 
	 */
	private int cuentaUnidadesPedido(int idPedido) throws SQLException {
		//cuando se divida un pedido en varias ot, quizÃ¡ esto cambie
		String sql = "select unidadesTotales "
				+ "from pedido "
				+ "where idpedido = ?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idPedido);
		ResultSet rs = pstmt.executeQuery();
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
		int idOrden = getLastOT();
//		PreparedStatement pstmt=cn.prepareStatement("INSERT INTO Almacenero (idAlmacenero, fk_idorden) VALUES (?,?)");
//		pstmt.setInt(1, idAlmacenero);
//		pstmt.setInt(2, lastOT);
//		pstmt.executeUpdate();
		String sql = "UPDATE ordentrabajo "
		+ "SET fk_idalmacenero=? "
		+ "WHERE ordentrabajo.idorden=?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idAlmacenero);
		pstmt.setInt(2, idOrden);
		pstmt.executeUpdate();
		asignarAlmacenero(idAlmacenero);
}
	
	private void asignarAlmacenero(int idAlmacenero) throws SQLException {
		//COMPROBAR QUE NO EXISTE EL ALMACENERO ANTES DE CREARLO
		if(!existsAlmacenero(idAlmacenero)) {
			PreparedStatement pstmt=cn.prepareStatement("INSERT INTO Almacenero (idAlmacenero) VALUES (?)");
			pstmt.setInt(1, idAlmacenero);
			pstmt.executeUpdate();
		}
		
	}


	/**
	 * Comprueba si el almacenero ya existe o no en la base de datos
	 * @param idAlmacenero
	 * @return
	 * @throws SQLException 
	 */
	private boolean existsAlmacenero(int idAlmacenero) throws SQLException {
		String sql = "select idalmacenero "
				+ "from almacenero "
				+ "where almacenero.idalmacenero=?";
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idAlmacenero);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) //si no hay filas --> no existe el almacenero
			return false;
		else
			return true;
	}


	/*
	 * Devuelve el Ãºltimo ID de las ordenes de trabajo para saber como llamar
	 * a la siguiente que creamos.
	 * NOTA: Tuya me respondio que esta opciÃ³n mejor no porque pierde rendimiento
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
		return lastOt;
		
		//ERROR ACTUAL:estado del cursor incorrecto: cursor indicado no estÃ¡ 
		//posicionado en una fila para sentencia UPDATE, DELETE, SET, o GET: ; 
		//PosiciÃ³n actual del resultado de la consulta es antes del primer registro
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

//POJOS NO ME FUNCIONÃ“
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
		//cuando hay mÃ¡s de una OT por pedido, ya no sirve mostrar asÃ­
		//ESTO MUESTRA TODOS LOS PRODUCTOS DE UN PEDIDO

		
		
		String sql ="select p.idproducto, p.nombre, po.unidadesenorden, po.unidadesporrecoger "
				+ "from  productoorden po "
				+ "left join producto p "
				+ "on p.idproducto = po.fk_idproducto "
				+ "where idorden = ? "
				+ "order by p.idproducto"; //aÃ±adido nuevo
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
		//guardar la incidencia actual y aÃ±adirle el nuevo mensaje
		String incidencia = "De "+ nombreProducto + " se necesita " + unidadesFaltan+ " unidades\n";
		//alterar el campo ot.incidencia que es un varchar
		String sql = "UPDATE ordentrabajo "
				+ "SET incidencia=? "
				+ "WHERE ordentrabajo.idorden=?";
		
		
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
	 * Si intento escanear mÃ¡s de las que faltan, no se puede --> muestra un mensaje de error (return -1)
	 * Si tras escanear ese producto ya no quedan unidades por recoger, (return 1)
	 * 
	 * cada vez que se escanea un producto, se decrementan las unidades del stock actual
	 * 
	 * @param idProducto
	 * @param idOrden
	 * @param unidadesSpinner
	 * @return -1: escaneo fallido, intenta escanear mÃ¡s unidades del producto de las que quedan por recoger
	 * 			1: escaneo con exito
	 * 		   -2: escaneo fallido, el producto no existe en la orden 
	 * @throws SQLException 
	 */
	public int escanear(int idProducto,int idOrden, int unidadesSpinner) throws SQLException {
		int uPorRecoger = unidadesPorRecoger(idProducto, idOrden);//unidades por recoger de ese producto para esa orden
		if(uPorRecoger == -1)
			return -2;
		System.out.println("unidadesPorRecoger de "+ idProducto + " son: "+ uPorRecoger);
		if(uPorRecoger < unidadesSpinner) {//
			return -1;
		}
		//si uPorRecoger = unidadesSpiner --> tras escanear, ya no quedarÃ¡ mÃ¡s que recoger para este producto
	
		//si uPorRecoger > unidadesSpinerr --> se escanea y sigue disponible para escanear
		int nuevasUnidades = uPorRecoger-unidadesSpinner;
		
		if(descontarStock(idProducto,unidadesSpinner)) {//mientras haya stock, se descuenta
			descontarUnidadesProductoPedido(idProducto,idOrden, nuevasUnidades);
			descontarUnidadesOrden(idOrden, unidadesSpinner);
		}
		
		return 1;
	}
	
	
	/**
	 * historia #16188
	 * Decrementa "unidadesSpinner" veces las unidades actuales del producto en la tienda.
	 * @param idProducto, producto a actualizar
	 * @param unidadesSpinner, unidades que tenemos que decrementar
	 * @return true si se descontÃ³ bien, false si no hay stock suficiente
	 * @throws SQLException 
	 */
	private boolean descontarStock(int idProducto, int unidadesSpinner) throws SQLException {
		//1) obtenemos el stock actual:
		int stockActual = getStockActual(idProducto);
		
		//2)si intentamos escanear unidades existentes en la tienda, es decir, que no queden nÃºmeros negativos
		if(stockActual- unidadesSpinner >= 0) {
			
			String sql = "update producto "
					+ "set stock = ? "
					+ "where idproducto=? ";
			
			
			
			PreparedStatement pstmt=cn.prepareStatement(sql);
			pstmt.setInt(1, stockActual-unidadesSpinner);//nuevoStock = Producto.unidades - unidadesSpinner
			pstmt.setInt(2, idProducto);
			pstmt.executeUpdate();
			
			int stockFinal = getStockActual(idProducto);
			return true;
	
		}
		else {
			//si intento escanear una cifra que no existe en el almacen, me avisa
			JOptionPane.showMessageDialog(null,"No hay unidades suficientes de este producto en el almacen", "Falta de stock", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		//NOTA: COMO EN LA TIENDA SE IMPEDIRÃ� COMPRAR MÃ�S UNIDADES DE LAS QUE EXISTEN EN LA TIENDA,
		//NUNCA LLEGARÃ‰ A TENER MÃ�S UNIDADES A RECOGER DE LAS QUE HAY EN STOCK
		//pero mejor poner uhn filtro por si acaso...
	}


	/**
	 * Devuelve el stock actual del producto buscado
	 * @param idProducto, producto a buscar
	 * @return unidades disponibles en la tienda
	 * @throws SQLException 
	 */
	private int getStockActual(int idProducto) throws SQLException {
		String sql = "SELECT stock "
				+ "FROM producto "
				+ "WHERE idproducto = ?";
		
		PreparedStatement pstmt = cn.prepareStatement(sql);
		pstmt.setInt(1, idProducto);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			return rs.getInt(1);
		}
		return -1;
		
	}


	/**
	 * MÃ©todo privado para descontar las unidades que recoger de la OT, sin especificar para quÃ© producto
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
	 * MÃ©todo  que devuelve las unidades totales a recoger en la OT que le pasemos
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
	 * MÃ©todo privado que descuenta las unidades correspondientes a "unidadesPorRecoger" en la base de datos, para 
	 * la tabla ProductoPedido. TÃ¡mbien descuenta las "unidadesTotalesPorRecoger" de OT
	 * @param idProducto
	 * @param idOrden
	 * @param unidadesSpinner
	 * @param nuevasUnidades , valor actualizado
	 * @throws SQLException 
	 */
	private void descontarUnidadesProductoPedido(int idProducto, int idOrden, int nuevasUnidades) throws SQLException {
	
//		String sql = "update productopedido "
//				+ "set unidadesporrecoger = ? "
//				+ "where fk_idproducto=? and fk_idpedido=?";
		String sql = "update productoorden  "
				+ "set unidadesporrecoger = ? "
				+ "where fk_idproducto=? and fk_idpedido=? "
				+ "and idorden = ?";
		
		int idPedido = getIdPedido(idOrden);//a travÃ©s de idorden llegamos al idpedido para actualizar el producto
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, nuevasUnidades);
		pstmt.setInt(2, idProducto);
		pstmt.setInt(3, idPedido);
		pstmt.setInt(4, idOrden);

		pstmt.executeUpdate();
		
	}


	/**
	 * MÃ©todo privado que devuelve las unidades que quedan por recoger del producto correspondiente a la orden
	 * @param idProducto
	 * @param idOrden
	 * @return devuelve unidadesPorRecoger, o -1 si ha habido algÃºn error(si no hay resultados)
	 * 
	 * @throws SQLException
	 */
	private int unidadesPorRecoger(int idProducto,int idOrden) throws SQLException {
		String sql = "select po.unidadesporrecoger "
				+ "from productoorden po " 
				+ "where  po.fk_idproducto=? "
				+ "and po.idorden = ?";
		
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idProducto);
		pstmt.setInt(2, idOrden);
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
	}
	
	
	
}

