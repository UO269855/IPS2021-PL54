package almacenero.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import common.database.Database;
import common.database.DbUtil;
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
	 * @return lista de pedidos que aun están pendientes: con su id, fecha y tamaño
	 * ordenados por fecha
	 * @throws SQLException 
	 * 
	 * NOVEDAD: LOS PEDIDOS POR TRASNFERENCIA YA NO SE MUESTRAN
	 */
	public ResultSet getListaPedidosArray() throws SQLException {
		//pedido pendiente: hasta ahora comprobaba que ot.fk_idpedido fuera null (ot.fk_idpedido IS NULL)
		//una OT tiene más de un pedido así que no me sirve	--> probar con Pedido.FK_IDOrden     
		String sql="SELECT p.idpedido, p.Fecha, p.unidadesTotales " 
				+ "FROM pedido p "
				+ "LEFT JOIN ordentrabajo ot "
				+ "ON p.idpedido = ot.fk_idpedido "
				+ "WHERE p.fk_idorden is null "			
				+ "AND p.metodopago != 'Transferencia' "
				+ "ORDER BY p.Fecha";
		
		//IMPORTANTE: TRAS FUSIONAR DOS PEDIDOS PEQUEÑOS EN UNA OT, LOS EXTRAS SIGUEN APARECIENDO 
		//COMO PENDIENTES CUANDO NO SE DEBERÍA
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
	

	
//	/**
//	 * Añada a la tabla OrdenTrabajo una fila con una nueva OT, que tendrá un IDOT
//	 * generado incremental (por eso no aparece como param, ya que es el primer
//	 * atributo), y el IDPedido con el que le acabamos de asociar
//	 *
//	 * @throws SQLException 
//	 */
//	public void crearOrden(int idPedido) throws SQLException  {
//		int unidadesARecoger = cuentaUnidadesPedido(idPedido);
//		System.out.println("Unidades por recoger: "+unidadesARecoger);
//		PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,incidencia,albaran,unidadesARecoger)"
//				+ " VALUES (?,?,?,?)");
//		pstmt.setInt(1, idPedido);
//		pstmt.setNString(2, null);
//		pstmt.setNString(3, null);
//		pstmt.setInt(4, unidadesARecoger); //añade a la OT el número de unidadesARecoger, que son el mismo que las uniadesTotales de pedido
//		pstmt.executeUpdate();
//	}
	
	/**
	 * NUEVO CREA ORDEN: AHORA OPTIMIZADAS
	 * Añada a la tabla OrdenTrabajo una fila con una nueva OT, que tendrá un IDOT
	 * generado incremental (por eso no aparece como param, ya que es el primer
	 * atributo), y el IDPedido con el que le acabamos de asociar
	 * @param idAlmacenero 
	 *
	 * @throws SQLException 
	 */
	public void crearOrden(int idPedido, int idAlmacenero) throws SQLException  {
		//ahora se crearán tantas OT como hagan falta
		int unidadesTotales = cuentaUnidadesPedido(idPedido);//cuenta el tamaño del pedido
		
		//cuando un pedido no tiene tamaño suficiente para dividirse en varias OT: 
		//el sistema buscará ente los pedidos pendientes otro/s más apra juntarlo. NO es 
		if(unidadesTotales < TAM_MEDIO) {
			//CREA la orden inicial
			PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,fk_idalmacenero,incidencia,albaran,unidadesARecoger)"
					+ " VALUES (?,?,?,?,?)");
			pstmt.setInt(1, idPedido);
			pstmt.setInt(2, idAlmacenero);
			pstmt.setNString(3, null);
			pstmt.setNString(4, null);
			pstmt.setInt(5, unidadesTotales); //añade a la OT el número de unidadesTotales, que son el mismo que las uniadesTotales de pedido
			pstmt.executeUpdate();
			
			int idOrden =  getLastOT();
			System.out.println("la orden que acaba de crear es: "+idOrden);
			asignarProductosAOT(idOrden,idPedido);
			
			//itera sobre los PEDIDOS PENDIENTES por orden de fecha
			//si la suma del tamaño actual + el pedido pendiente iterando es <TAM_MEDIO, se AÑADE ese pedido entero
			//si la suma es mayor, no se escoge
			//al llegar al final de la iteracion termina
			String sql = "SELECT p.idpedido, p.unidadesTotales "
					+ "FROM pedido p "
					+ "LEFT JOIN ordentrabajo ot "
					+ "ON p.idpedido = ot.fk_idpedido "
					+ "WHERE ot.fk_idpedido IS NuLL "
					+ "AND p.metodopago != 'Transferencia'"
					+ "AND p.idpedido != ? "
					+ "ORDER BY p.Fecha";
			
			PreparedStatement pstmt2=cn.prepareStatement(sql);
			pstmt2.setInt(1, idPedido);//evita que itere sobre sí mismo
			ResultSet rs = pstmt2.executeQuery();
			
			
			//MARCARLO COMO YA NO PENDIENTE!!!!
			PreparedStatement pstmt4=cn.prepareStatement("UPDATE pedido SET fk_idorden = ? WHERE idpedido = ? ");
			pstmt4.setInt(1, idOrden);
			pstmt4.setInt(2, idPedido);
			pstmt4.executeUpdate();
			
			
			int tamPedIter = 0;
			int idPedidoIter = 0;
			int tamActual= unidadesTotales;
			
			while(rs.next()) {
				//itera sobre los pedidos pendientes
				idPedidoIter = rs.getInt(1);//idPedido iterado
				tamPedIter = rs.getInt(2);//coge el tamaño de cada pedido
				
				if(tamActual + tamPedIter <= TAM_MEDIO) {//se añaden esos pedidos a la misma OT
					fusionarPedido(idOrden,tamActual+tamPedIter);//fusiona el tamaño, solo cambia el tamaño total
					asignarProductosAOT(idOrden, idPedidoIter );//añade el bloque entero de los productos pedidos de este pedido iterandose a la última orden creada
					tamActual = tamActual+tamPedIter;
					
					
					
					//MARCARLO COMO YA NO PENDIENTE!!!!
					PreparedStatement pstmt3=cn.prepareStatement("UPDATE pedido SET fk_idorden = ? WHERE idpedido = ? ");
					pstmt3.setInt(1, idOrden);
					pstmt3.setInt(2, idPedidoIter);
					
					//supuestamente hasta aquí lo que funcionaría es:
//						-que si tengo dos o más pedidos pequeños, los fusiona en la misma OT
					
				}
			}
			
			
			
		} else if(unidadesTotales > TAM_MEDIO){ //CREAR DIFERENTES OT
			
		}
		else if(unidadesTotales == TAM_MEDIO) {//si es del tamaño ideal--> crea una OT
			PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,fk_idalmacenero,incidencia,albaran,unidadesARecoger)"
					+ " VALUES (?,?,?,?,?)");
			pstmt.setInt(1, idPedido);
			pstmt.setInt(2, idAlmacenero);
			pstmt.setNString(3, null);
			pstmt.setNString(4, null);
			pstmt.setInt(5, unidadesTotales); //añade a la OT el número de unidadesARecoger, que son el mismo que las uniadesTotales de pedido
			pstmt.executeUpdate();
		}
	}
	
	
/**
 * Método privado para asignar los productos del pedido a la ot correspondiente
 * actualmente TODOS los productos del pedido se asignan a la misma orden. No estoy teniendon en cuenta divisiones
 * @param idOrden
 * @param idPedido
 * @throws SQLException 
 */
private void asignarProductosAOT(int idOrden, int idPedido) throws SQLException {
	
	//1) actualizar los productos pedidos de arriba para que el campo fk_idorden sea el parámetro
	String sql = "UPDATE productopedido "
			+ "SET fk_idorden = ? "
			+ "WHERE fk_idpedido = ?";
	PreparedStatement pstmt = cn.prepareStatement(sql);
	pstmt.setInt(1, idOrden);
	pstmt.setInt(2, idPedido);
	pstmt.executeUpdate();
}


private void fusionarPedido(int idOrden, int tamOT) throws SQLException {
	//CON ASIGNGAR LA PRIMERA VEZ EL ALMACENERO YA VALE
	String sql = "UPDATE ordenTrabajo "
			+ "SET ordentrabajo.unidadesarecoger = ? "//añadir los productos
			+ "WHERE ordentrabajo.idorden = ?";
	
	PreparedStatement pstmt=cn.prepareStatement(sql);
	pstmt.setInt(1, tamOT);
	pstmt.setInt(2, idOrden);
	pstmt.executeUpdate();

	//ESTA PRIMERA PARTE SOLO MODIFICA EL TAMAÑO DE LA ORDEN CREADa, sumandole el tamaño de los pedidos fusionados

	
}


//	/**
//	 * @param tam_inicial
//	 * @param idPedido 
//	 * @return tam_orden, tamaño en el que se dividirá cada tarea
//	 * OJO: si hacemos 11/2 = 5. Para calcular el 6, sumamos al 5 el restante (11-5)
//	 * @throws SQLException 
//	 */
//	private int divideOrden(int tam_inicial, int idPedido, int idAlmacenero) throws SQLException {
//		int aux = tam_inicial;
//		int divisor = 2; // será el número de ordenes también que se crean
//		int tam_orden = -1;
//		
//		if(tam_inicial > TAM_MEDIO) {
//			while( (aux/divisor) > TAM_MEDIO) //irá incrementando el divisor hasta encontrar en cuantas unidades dividir
//					divisor++;                // para que las resultantes no se pasen del TAM_MEDIO
//			tam_orden = aux / divisor;
//		}
//		
//		System.out.println("el divisor es: " + divisor);
//		for (int i = 0; i < divisor; i++) {
//			if(i == divisor-1) {//si es el último elemento, le añade el resto de l adivision
//				int resto = tam_inicial % divisor;//usando el módulo
//				PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,fk_idalmacenero,incidencia,albaran,unidadesARecoger)"
//						+ " VALUES (?,?,?,?,?)");
//				pstmt.setInt(1, idPedido);
//				pstmt.setInt(2, idAlmacenero);
//				pstmt.setNString(3, null);
//				pstmt.setNString(4, null);
//				pstmt.setInt(5, tam_orden + resto); 
//				pstmt.executeUpdate();
//			}
//			else {
//				PreparedStatement pstmt=cn.prepareStatement("INSERT INTO ordenTrabajo (fk_idpedido,fk_idalmacenero,incidencia,albaran,unidadesARecoger)"
//						+ " VALUES (?,?,?,?,?)");
//				pstmt.setInt(1, idPedido);
//				pstmt.setInt(2, idAlmacenero);
//				pstmt.setNString(3, null);
//				pstmt.setNString(4, null);
//				pstmt.setInt(5, tam_orden); //no tiene en cuenta si la división no es exacta poor ahora
//				pstmt.executeUpdate();
//			}
//		}
//		return tam_orden;
//	
//}


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
		//cuando hay más de una OT por pedido, ya no sirve mostrar así
		//ESTO MUESTRA TODOS LOS PRODUCTOS DE UN PEDIDO
		String sql = "select p.idproducto,p.nombre, pp.unidadespedido, pp.unidadesPorRecoger " 
				+ "from producto p, ordentrabajo ot , pedido p "
				+ "left join productoPedido pp "
				+ "on p.idproducto = pp.fk_idproducto "
				+ "where ot.fk_idpedido=p.idpedido AND p.idpedido=pp.fk_idpedido AND ot.idorden=?";
		//Y SI LE AÑADO UN WHERE=PP.FK_IDORDEN = ?
		//A LO MEJOR SOBRA ENTONCES 
		
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
	 * cada vez que se escanea un producto, se decrementan las unidades del stock actual
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
			descontarUnidadesProductoPedido(idProducto,idOrden, nuevasUnidades);
			descontarUnidadesOrden(idOrden,unidadesSpinner);
			//novedad historia #16188
			descontarStock(idProducto,unidadesSpinner);
			return 0;
		}
		//si uPorRecoger > unidadesSpinerr --> se escanea y sigue disponible para escanear
		int nuevasUnidades = uPorRecoger-unidadesSpinner;
		descontarUnidadesProductoPedido(idProducto,idOrden, nuevasUnidades);
		descontarUnidadesOrden(idOrden, unidadesSpinner);
		
		descontarStock(idProducto,unidadesSpinner);
		return 1;
	}
	
	
	/**
	 * historia #16188
	 * Decrementa "unidadesSpinner" veces las unidades actuales del producto en la tienda.
	 * @param idProducto, producto a actualizar
	 * @param unidadesSpinner, unidades que tenemos que decrementar
	 * @throws SQLException 
	 */
	private void descontarStock(int idProducto, int unidadesSpinner) throws SQLException {
		//1) obtenemos el stock actual:
		int stockActual = getStockActual(idProducto);
		
		//2)si intentamos escanear unidades existentes en la tienda, es decir, que no queden números negativos
		if(stockActual- unidadesSpinner >= 0) {
			System.out.println("El stock sin decrementar el producto "+ idProducto + " es : " + stockActual);
			
			String sql = "update producto "
					+ "set unidades = ? "
					+ "where idproducto=? ";
			
			
			
			PreparedStatement pstmt=cn.prepareStatement(sql);
			pstmt.setInt(1, stockActual-unidadesSpinner);//nuevoStock = Producto.unidades - unidadesSpinner
			pstmt.setInt(2, idProducto);
			pstmt.executeUpdate();
			
			int stockFinal = getStockActual(idProducto);
	
			System.out.println("El stock tras decrementar el producto "+ idProducto + " es : " + stockFinal);
		}
		else {
			//si intento escanear una cifra que no existe en el almacen, me avisa
			JOptionPane.showMessageDialog(null,"No hay unidades suficientes de este producto en el almacen", "Falta de stock", JOptionPane.INFORMATION_MESSAGE);
		}
		//NOTA: COMO EN LA TIENDA SE IMPEDIRÁ COMPRAR MÁS UNIDADES DE LAS QUE EXISTEN EN LA TIENDA,
		//NUNCA LLEGARÉ A TENER MÁS UNIDADES A RECOGER DE LAS QUE HAY EN STOCK
		//pero mejor poner uhn filtro por si acaso...
	}


	/**
	 * Devuelve el stock actual del producto buscado
	 * @param idProducto, producto a buscar
	 * @return unidades disponibles en la tienda
	 * @throws SQLException 
	 */
	private int getStockActual(int idProducto) throws SQLException {
		String sql = "SELECT unidades "
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
	private void descontarUnidadesProductoPedido(int idProducto, int idOrden, int nuevasUnidades) throws SQLException {
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

