package almacenero.controllers;

import java.util.List;

import giis.demo.util.Database;

/**
 * Acceso a los datos de OrdenTrabajo y Pedido
 * (en esta clase se hacen las consultas)
 * @author Ali
 *
 */
public class OrdenTrabajoModel {

	private Database db=new Database();
	
	
	/**
	 * 
	 * Implementacion usando la utilidad que obtiene una lista de arrays de objetos 
	 * resultado de la ejecucion de una query sql
	 *
	 * @return lista de pedidos que aun están pendientes: con su id, fecha y tamaño
	 */
	private List<Object[]> getListaPedidos() {
		String sql="SELECT p.idpedido, p.Fecha, p.UnidadesTotales "
					+ " FROM pedido p"
					+ "LEFT JOIN ordentrabajo ot"
					+ " ON p.idpedido == ot.idpedido"
					+ "WHERE ot.idpedido IS NULL ";
		return db.executeQueryArray(sql);
	}
}
