package almacenero.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import common.modelo.Pedido;
import giis.demo.util.Database;
import giis.demo.util.UnexpectedException;
import giis.demo.util.Util;

/**
 * 
 * Controlador para la funcionalidad de visualizar los pedidos pendientes y obtener
 * ordenes de trabajo.
 * 
 * @author Alicia Fernández Pushkina -UO275727
 *
 */
public class OrdenTrabajoController {

	//informacion de conexion a la base de datos utilizada
	private String URL = "hsql://localhost";
	private String username = "SA";
	private String password = "";
	
	private OrdenTrabajoModel model;
	private OrdenTrabajoView view;
	
	
	public OrdenTrabajoController(OrdenTrabajoModel m, OrdenTrabajoView v) {
		this.model = m;
		this.view = v;
		//no hay inicializacion especifica del modelo, solo de la vista
//		this.initView();
	}
	
	/**
	 * Cuando lo solicite un almacenero, visualizará en su terminal portátil todos
	 * los pedidos realizados por orden de fecha que están pendientes, con indicación 
	 * del tamaño cada uno. Seleccionando uno de ellos obtendrá la orden de trabajo
	 * de recogida que quedará asignada al almacenero que la solicitó. 
	 * De momento no se contemplarán OTs de recogida que junten o dividan un pedido.
	 * 
	 * @param pedidos
	 * @throws SQLException
	 */
	public void visualizarPedidos ( )throws SQLException{
		
		/* Hacer una consulta para sacar de todos los pedidos,aquellos que tras hacer un JOIN
		 * entre Pedido y OT, el campo FK de OT (IdPedido) esté a null. Porque significará
		 * que NO tiene asignado pedido*/
		
		try (Connection cn=DriverManager.getConnection(URL, username, password); 
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT p.idpedido, p.Fecha, p.UnidadesTotales "
											+ " FROM pedido p"
											+ "LEFT JOIN ordentrabajo ot"
											+ " ON p.idpedido == ot.idpedido"
											+ "WHERE ot.idpedido IS NULL ")//incluir aquí o al iterar?
						) {

			
		
		} catch (SQLException e) {
			throw new UnexpectedException(e);
		}
	
		
	}
	
	


//	/**
//	 * Ordena los pedidos pendientes por fecha
//	 * página de referencia:
//	 * https://ozenero.com/how-to-sort-java-list-objects-by-date-property-with-examples
//	 * @param pedidos
//	 */
//	private List<Pedido>  ordenarPedidos(List<Pedido> pedidos) {
//		Collections.sort(pedidos, (c1, c2) -> {
//			return Long.valueOf(c1.getFecha().getTime()).compareTo(c2.getFecha().getTime());
//		});
//		
//		return pedidos;
//	}
	
	
	/**
	 * Tras seleccionar un pedido, se le quedará asignado como OT al almacenero que clicó
	 * NOT: un almacenero tendrá varias OT
	 * habrá una clase Almacenero que tendrá una FK IDOT
	 */
	private void asignarAlmacenero() {
		//¿Cómo sé qué almacenero esta esocgiendo para asignarle la OT?
		
	}
	
}
