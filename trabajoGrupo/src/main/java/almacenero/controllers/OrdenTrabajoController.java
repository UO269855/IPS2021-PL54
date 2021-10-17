package almacenero.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import common.database.DbUtil;
import common.modelo.Almacenero;
import common.modelo.Pedido;
import giis.demo.util.ApplicationException;
import giis.demo.util.Database;
import giis.demo.util.SwingUtil;
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
//	private String URL = "hsql://localhost";
	private String URL ="jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	
	private OrdenTrabajoModel model;
	private OrdenTrabajoView view;
	private String lastSelectedKey=""; //recuerda la ultima fila seleccionada para restaurarla cuando cambie la tabla de carreras
	
	private Almacenero almacenero;//no estoy segura de si deberia estar ene sta clase

	
	
	public OrdenTrabajoController(OrdenTrabajoModel m, OrdenTrabajoView v) throws SQLException {
		this.model = m;
		this.view = v;
		//no hay inicializacion especifica del modelo, solo de la vista
//		this.initView();
	}
	/**
	 * Inicializacion del controlador: anyade los manejadores de eventos a los objetos del UI.
	 * Cada manejador de eventos se instancia de la misma forma, para que invoque un metodo privado
	 * de este controlador, encerrado en un manejador de excepciones generico para mostrar ventanas
	 * emergentes cuando ocurra algun problema o excepcion controlada.
	 */
	public void initController() {
		view.getBtMostrarPendientes().addActionListener(e -> SwingUtil.exceptionWrapper(() -> {
			try {
				getListaPedidosPendientes();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}));
	
		view.getTabPedidos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//no usa mouseClicked porque al establecer seleccion simple en la tabla de carreras
				//el usuario podria arrastrar el raton por varias filas e interesa solo la ultima
				SwingUtil.exceptionWrapper(() -> asignarAlmacenero());//en lugar de updateDetail, 
				//al seleccionar un pedido de la OT, lo asignaria al almacenero
			}
		});
		
		view.getBtAlmacenero().addActionListener(e -> SwingUtil.exceptionWrapper(() -> confirmarAlmacenero()));
	}
	
	
	
	public void initView() throws SQLException {
//		this.getListaPedidosPendientes();
		//Abre la ventana (sustituye al main generado por WindowBuilder)
		view.setVisible(true); 
		view.initialize();
	
	}
	
	
	/**
	 * Al hacer clic en el boton tras añadir el ID del usuario, si es válido
	 * lo guardará para asignarle a él la ot que elija
	 */
	public void confirmarAlmacenero() {
		//comprobar que el ID del almacenero seavalido:
		if(view.getTfAlmacenero().getText() != ""){//por ahora solo compruebo que no sea vacío
			almacenero = new Almacenero(view.getTfAlmacenero().getText());//por ahora suponemos que ingreso un ID ya existente en la BBDD
			//si no existe el almacenero --> crearlo en la BBDD
		}
		else//avisar de que debe ser un ID valido y pedir que reescriba
			JOptionPane.showInputDialog("El ID no puede ser vacío");
		//COMPLETAR
		
	}
	
	/**
	 * La obtencion de la lista de pedidos pendientes solo necesita obtener la lista de objetos del modelo 
	 * y usar metodo de SwingUtil para crear un tablemodel que se asigna finalmente a la tabla.
	 * @throws SQLException 
	 */
//	public void getListaPedidosPendientes() throws SQLException {
//		List<Object[]> pedidos =model.getListaPedidosArray(); // igual tengo que usar el otro método para tener lista POJO
//		TableModel tmodel=SwingUtil.getTableModelFromPojos(pedidos, new String[] {"id", "fecha", "tamaño"});
//		view.getTabPedidos().setModel(tmodel);
//		SwingUtil.autoAdjustColumns(view.getTabPedidos());
//		
//		
//		//Como se guarda la clave del ultimo elemento seleccionado, restaura la seleccion de los detalles
////		this.restoreDetail(); //errores al compilar por no entender para qué sirve
//
//	}
	
	public void getListaPedidosPendientes() throws SQLException {
		ResultSet pedidos =model.getListaPedidosArray(); // 
		TableModel tmodel = DbUtil.resultSetToTableModel(pedidos);
		view.getTabPedidos().setModel(tmodel);
//		SwingUtil.autoAdjustColumns(view.getTabPedidos());
		
		
		//Como se guarda la clave del ultimo elemento seleccionado, restaura la seleccion de los detalles
//		this.restoreDetail(); //errores al compilar por no entender para qué sirve

	}
	
	
	

	/**
	 * Restaura la informacion del detalle del pedido para visualizar los valores correspondientes
	 * a la ultima clave almacenada.
	 */
//	public void restoreDetail() {
//		//Utiliza la ultimo valor de la clave (que se reiniciara si ya no existe en la tabla)
//		this.lastSelectedKey=SwingUtil.selectAndGetSelectedKey(view.getTabPedidos(), this.lastSelectedKey);
//		//Si hay clave para seleccionar en la tabla muestra el detalle, si no, lo reinicia
//		if ("".equals(this.lastSelectedKey)) { 
//			view.getDetalleCarrera().setModel(new DefaultTableModel());		
//		} else {
//			this.updateDetail();
//		}
//	}

	/**
	 * Tras seleccionar un pedido, se le quedará asignado como OT al almacenero que clicó
	 * NOT: un almacenero tendrá varias OT
	 * habrá una clase Almacenero que tendrá una FK IDOT
	 */
	public void asignarAlmacenero() {
		//Obtiene la clave seleccinada y la guarda para recordar la seleccion en futuras interacciones
//		this.lastSelectedKey=SwingUtil.getSelectedKey(view.getTabPedidos());
		int idPedido =SwingUtil.getSelectedKeyInt(view.getTabPedidos());
		
		try { 
			//COMPLETAR
			
			if(almacenero == null)//significa que aun no se ha "iniciado sesion"
				JOptionPane.showInputDialog("Ingresa un IDAlmacenero válido");
			else {
				//almacenero.addOT(model.getLastOT() + 1); recorriendo las Ot el rendimiento abja
				//almacenero.addOT(crearOrden(idPedido)); por ahora no se devolver el valor y tampoco hace falta, lo consulto en la BBDD, no en el almacenero
				String id = almacenero.getIDAlmacenero();
				crearYAsigna(idPedido,Integer.parseInt(id) );
			}
		} catch (ApplicationException e) { 
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Crea una nueva orden de trabajo asociada con el IDPedido seleccionada
	 * Depsues asigna esta OT al almacenero
	 * @param idPedido
	 * @param idAlmacenero , idAlamacenero
	 */
	private void crearYAsigna(int idPedido, int idAlmacenero) {
		try {
			model.crearOrden(idPedido);
			model.asignarOrden(idAlmacenero);
		} catch (SQLException e) {
		}
		//NOTA: en la interfaz no se ve nigun cambio tras asignar el almacenero
	}
	
	
	
	/**
	 * @throws SQLException
	 */
	public void comprobarUnidades(int idOrden) throws SQLException {
//		int idOrden = 1; //para probar le paso la idOrden directamente
		
		//contiene: p.idproducto, p.nombre, pp.unidadespedido, p.unidades, ot.incidencia
		ResultSet productosPedidos = model.getListaProductosPedidos(idOrden);
		
		while(productosPedidos.next()) {//iterar en cada producto de la lista
		
			int unidadesPedido = productosPedidos.getInt(3);
			int unidadesProducto = productosPedidos.getInt(4);//el stock del producto
			String incidencia = productosPedidos.getString(5);

			
			int res = unidadesPedido- unidadesProducto;
			if(res >0) {//significa que falta stock

				System.out.println("REFERENCIAS QUE FALTAN PARA IDORDEN: " + idOrden);
				model.anotarIncidencia(productosPedidos.getString(2) ,res, idOrden,incidencia);//escribir en "incidencia" dentro de OrdenTrabajo la cantidad que falta y de qué
				
			}
		}
		
		//visualizar referencias que faltan
	}
	
	
	

	
}
