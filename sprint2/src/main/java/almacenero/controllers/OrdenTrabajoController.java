package almacenero.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import common.database.DbUtil;
import common.modelo.Almacenero;
import common.modelo.ProductoDisplayEscaner;
import common.database.SwingUtil;
import giis.demo.util.ApplicationException;

/**
 * 
 * Controlador para la funcionalidad de visualizar los pedidos pendientes y obtener
 * ordenes de trabajo.
 * 
 * @author Alicia Fernández Pushkina -UO275727
 *
 */
public class OrdenTrabajoController {

	
	
	private OrdenTrabajoModel model;
	private OrdenTrabajoView view;
	private String lastSelectedKey=""; //recuerda la ultima fila seleccionada para restaurarla cuando cambie la tabla de carreras
	
	private EscanerView escanerView;
	private Almacenero almacenero;//no estoy segura de si deberia estar ene sta clase

	
	/**
	 * Constructor de la clase que inicializa las tres clases
	 * @param m, ventana model que se relaciona con la BBDD
	 * @param v, ventana donde se ven los pedidos pendientes
	 * @param refView, ventana donde se comprueban las OT
	 * @throws SQLException
	 */
	public OrdenTrabajoController(OrdenTrabajoModel m, OrdenTrabajoView v, EscanerView escanerView) throws SQLException {
		this.model = m;
		this.view = v;
		this.escanerView = escanerView;
		//no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
//		this.initRefView(); ahora la inicializamos cuando pulsamos el botón
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
		
			
		
		view.getBtComprobarOrden().addActionListener(e -> SwingUtil.exceptionWrapper(() -> initEscanerView()));
		
		escanerView.getBtIngresar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> mostrarReferencias() ));
		escanerView.getBtEscaner().addActionListener(e -> SwingUtil.exceptionWrapper(() -> escanear()));
	}
	
	
	
	/**
	 * Inicializa la ventana de los pedidos pendientes
	 * @throws SQLException
	 */
	public void initView() throws SQLException {
		view.setVisible(true); 
		view.initialize();
	
	}
	
//	/**
//	 * Inicializa la ventana que comprueba las ordenes de trabajo
//	 */
//	public void initRefView()  {
//		refView.setVisible(true); 
//		refView.initialize();
//	
//	}
	
	/**
	 * Inicializa la ventana que comprueba las ordenes de trabajo
	 */
	public void initEscanerView()  {
		escanerView.setVisible(true); 
		escanerView.initialize();
	
	}
	
	
	/**
	 * Al hacer clic en el boton tras añadir el ID del usuario, si es válido
	 * lo guardará para asignarle a él la ot que elija
	 */
	public void confirmarAlmacenero() {
		if(view.getTfAlmacenero().getText() != ""){
			almacenero = new Almacenero(view.getTfAlmacenero().getText());
		}
		else//avisar de que debe ser un ID valido y pedir que reescriba
			JOptionPane.showInputDialog("El ID no puede ser vacío");
		
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
	
	/**
	 * Llama al modelo para traer la lista de pedidos pendientes y ponerla en la interfaz
	 * @throws SQLException
	 */
	public void getListaPedidosPendientes() throws SQLException {
		ResultSet pedidos =model.getListaPedidosArray(); // 
		TableModel tmodel = DbUtil.resultSetToTableModel(pedidos);
		view.getTabPedidos().setModel(tmodel);
//		SwingUtil.autoAdjustColumns(view.getTabPedidos());
		
		//Como se guarda la clave del ultimo elemento seleccionado, restaura la seleccion de los detalles
//		this.restoreDetail(); //errores al compilar
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
	 */
	public void asignarAlmacenero() {
		//Obtiene la clave del pedido seleccinada y la guarda para recordar la seleccion en futuras interacciones
		int idPedido =SwingUtil.getSelectedKeyInt(view.getTabPedidos());
		
		try { 
			
			if(almacenero == null)//significa que aun no se ha "iniciado sesion"
				JOptionPane.showMessageDialog(view,"Ingresa un IDAlmacenero válido");
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
	 * Al ingresar una orden, mostramos en lista los productos de la OT. Se debe de ver la cantidad de productos pedidos
	 * y la cantidad de productos que faltan por escanear.
	 * A medida que se escanean correctamente, este último campo irá disminuyendo hasta cero. 
	 * Cuando llegue a cero, significa que no queda nada por escanear.
	 * @throws SQLException 
	 */
	public void mostrarReferencias()  {
		 List<ProductoDisplayEscaner> pojo = null;
		try {
			pojo = model.getListaProductosEscaner(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String[] campos = new String[3];
		campos[0] = "idProducto";
		campos[1] = "nombre";
		campos[2] = "unidadesPedido";
		campos[3] = "unidadesPorRecoger";
		escanerView.getTabEscaner().setModel(SwingUtil.getRecordModelFromPojo(pojo, campos));
	}
	
//	public void mostrarReferencias() {
//		//consigue la orden que acabamos de introducir en el campo de texto
//		int idOrden = Integer.parseInt(escanerView.getTaIdOrden().getText());
//		//necesito la orden que acabo de crear
//		
//		ResultSet escaner = null;
//		try {
//			escaner = model.getListaProductosEscaner(idOrden);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} 
//		
//		TableModel tmodel = DbUtil.resultSetToTableModel(escaner);
//		escanerView.getTabEscaner().setModel(tmodel);
//	}
	
	public void escanear() {
		
		
	}
	
	/**
	 * Crea una nueva orden de trabajo asociada con el IDPedido seleccionada
	 * Depsues asigna esta OT al almacenero
	 * @param idPedido, pedido que queremos asignar
	 * @param idAlmacenero , almacenero que se va a quedar con la OT
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
	 * Commprueba que el número de unidades que hay de stock sean suficientes para completar la OT.
	 * Si hubiera alguna incidencia, la anota en la ot y no se lleva a empaquetado
	 * @throws SQLException
	 */
//	public boolean comprobarUnidades() throws SQLException {
//		int idOrden=0;
//		if(refView.getTfIDOrden().getText().equals(""))
//			JOptionPane.showMessageDialog(refView, "La orden de trabajo no puede ser vacío");
//		else {
//			idOrden = Integer.parseInt(refView.getTfIDOrden().getText());
//			
//			//contiene: p.idproducto, p.nombre, pp.unidadespedido, p.unidades, ot.incidencia
//			ResultSet productosPedidos = model.getListaProductosPedidos(idOrden);
//			boolean sinIncidencias = true;
//			refView.getTaIncidencias().setText("REFERENCIAS QUE FALTAN PARA COMPLETAR LA ORDEN "+ idOrden + ":\n");
//			while(productosPedidos.next()) {//iterar en cada producto de la lista
//			
//				int unidadesPedido = productosPedidos.getInt(3);
//				int unidadesProducto = productosPedidos.getInt(4);//el stock del producto
//				String viejaIncidencia = productosPedidos.getString(5);
//
//				
//				int res = unidadesPedido- unidadesProducto;
//				if(res >0) {//significa que falta stock
//
//					System.out.println("REFERENCIAS QUE FALTAN PARA IDORDEN: " + idOrden);
//					String incidencia = model.anotarIncidencia(productosPedidos.getString(2) ,res, idOrden,viejaIncidencia);//escribir en "incidencia" dentro de OrdenTrabajo la cantidad que falta y de qué
//					refView.getTaIncidencias().setText(refView.getTaIncidencias().getText() + incidencia);
//					sinIncidencias = false;
//				}
//			}
//			
//			if(sinIncidencias== true) {
//				refView.getTaIncidencias().setText("\n Referencias suficientes para completar.");
//				return true;
//			}else {
//				return false;
//			}
//			
//		
//		
//		
//		}
//		return true;
//	
//	}

	
	
	
}
