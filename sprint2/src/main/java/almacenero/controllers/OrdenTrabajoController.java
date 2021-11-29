package almacenero.controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import common.database.DbUtil;
import common.database.Fichero;
import common.modelo.Almacenero;
import common.modelo.Producto;
import giis.demo.util.ApplicationException;
import giis.demo.util.SwingUtil;

/**
 * 
 * Controlador para la funcionalidad de visualizar los pedidos pendientess y obtener
 * ordenes de trabajo.
 * 
 * @author Alicia FernÃ¡ndez Pushkina -UO275727
 *
 */
/**
 * @author Ali
 *
 */
public class OrdenTrabajoController {

	
	
	private OrdenTrabajoModel model;
	private OrdenTrabajoView view;//escoge el pedido
	private AlmacenView almacenView;//gestiona cada orden y muestra lis productos
	
	private Almacenero almacenero;//no estoy segura de si deberia estar ene sta clase
	private List<Almacenero> almaceneros;
	private VentanaInformeOrden ordenView;
	
	/**
	 * Constructor de la clase que inicializa las tres clases
	 * @param m, ventana model que se relaciona con la BBDD
	 * @param v, ventana donde se ven los pedidos pendientes
	 * @param refView, ventana donde se comprueban las OT
	 * @throws SQLException
	 * @wbp.parser.entryPoint
	 */
	public OrdenTrabajoController(OrdenTrabajoModel m, OrdenTrabajoView v, AlmacenView a) throws SQLException {
		this.model = m;
		this.view = v;
		this.almacenView = a;
		
		this.almaceneros = new ArrayList<Almacenero>();
		//no hay inicializacion especifica del modelo, solo de la vista
		this.initView();
	}
	
	public OrdenTrabajoController(OrdenTrabajoModel m, OrdenTrabajoView v, AlmacenView a, VentanaInformeOrden o) throws SQLException {
		this.model = m;
		this.view = v;
		this.almacenView = a;
		this.ordenView = o;
		//no hay inicializacion especifica del modelo, solo de la vista
//		this.initView();
		
		ordenView.setVisible(true); 
//		ordenView.initialize();
	}
	/**
	 * Inicializacion del controlador: anyade los manejadores de eventos a los objetos del UI.
	 * Cada manejador de eventos se instancia de la misma forma, para que invoque un metodo privado
	 * de este controlador, encerrado en un manejador de excepciones generico para mostrar ventanas
	 * emergentes cuando ocurra algun problema o excepcion controlada.
	 * @wbp.parser.entryPoint
	 */
	public void initController() {

		almacenView.getTabPedidos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//no usa mouseClicked porque al establecer seleccion simple en la tabla de carreras
				//el usuario podria arrastrar el raton por varias filas e interesa solo la ultima
				SwingUtil.exceptionWrapper(() -> asignarAlmacenero());//en lugar de updateDetail, 
				//al seleccionar un pedido de la OT, lo asignaria al almacenero
				initOrdenView();//ABRE LA NUEVA VENTANA
				getListaOrdenes();//muestra las ordenes creadas (necesito el idPedido)
				ayudaOrdenes();
				
			}
		});
		
		almacenView.getBtAlmacenero().addActionListener(e -> SwingUtil.exceptionWrapper(() -> confirmarAlmacenero() ));

		
		//ESTAS TRES VAN PARA LA SEGUNDA VENTANA
		view.getBtIncidencia().addActionListener(e -> SwingUtil.exceptionWrapper(() -> anotarIncidencia() ));
		view.getBtEscaner().addActionListener(e -> SwingUtil.exceptionWrapper(() -> escanear()));
		
		//seleccionar la orden que queremos gestionar y que muestre su contenido
		view.getTabOrden().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int idOrden =SwingUtil.getSelectedKeyInt(view.getTabOrden());
				System.out.println("la orden que acabas de seleccionar es la "+ idOrden);
				SwingUtil.exceptionWrapper(() -> mostrarReferencias(idOrden));
//				//habilitar el botÃ³n de  EscribirIncidencia cuando se cree y visualice la OT
				SwingUtil.exceptionWrapper(() -> view.getBtIncidencia().setEnabled(true));
				SwingUtil.exceptionWrapper(() -> view.getTaIncidencia().setEditable(true));
				SwingUtil.exceptionWrapper(() -> mostrarIncidencia());
			}
		});
		
		view.getBtFinalizar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> generarAlbaran()));
		}
	
	
//	private void obtenerReferencias() {
//		int idOrden = 0 ;
//		 System.out.println("Introduce el id de la orden a revisar:");
//		  Scanner entradaEscaner = new Scanner (System.in);
//		  idOrden = Integer.parseInt(entradaEscaner.nextLine()); 
//		  
//		int idPedido = -1;
//		try {
//			idPedido = new OrdenTrabajoModel().getIdPedido(idOrden);
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		if(idPedido == -1) {
//			System.out.println("no se encuetra ningun pedido asociado a dicha orden");
//			return;
//		}
//		
//		  try {
//			new algortimoAlmacenero().execute(idPedido);
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}
	/**
	 * Muestra en la interfaz la incidencia que contenga la OT, por si el al
	 */
	private void mostrarIncidencia() {
		//NO RELLENAMOS PORQUE DECIDÃ� QUE NO S ESOBREESCRIBE LA INCIDENCIA
	}
	
	/**
	 * Inicializa la ventana de los pedidos pendientes, ES DECIR, el almacen
	 * @throws SQLException
	 */
	public void initView() throws SQLException {
		almacenView.setVisible(true); 
		almacenView.initialize();
	
	}
	
	/**
	 * Inicializa la ventana que comprueba las ordenes de trabajo
	 */
	public void initOrdenView()  {
		
		try {
			view.setVisible(true); 
			view.initialize();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	
	
	
	/**
	 * Al hacer clic en el boton tras aÃ±adir el ID del usuario, si es vÃ¡lido
	 * lo guardarÃ¡ para asignarle a Ã©l la ot que elija
	 */
	public void confirmarAlmacenero() {
		try{
			almacenero = new Almacenero(Integer.parseInt(almacenView.getTfAlmacenero().getText()));
			almacenView.getBtAlmacenero().setEnabled(false);
			almacenView.getTfAlmacenero().setEditable(false);
			
			//mostrar los pedidos pendientes
			getListaPedidosPendientes();
//			almacenView.getBtObtenerReferencias().setEnabled(true);
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null,"El IdAlmacenero solo estÃ¡ formado por nÃºmeros", "IdAlmacenero invÃ¡lido", JOptionPane.INFORMATION_MESSAGE);

		}catch( SQLException e1 ){
			e1.printStackTrace();
		}
		
	}
	

	
	/**
	 * Llama al modelo para traer la lista de pedidos pendientes y ponerla en la interfaz
	 * @throws SQLException
	 */
	public void getListaPedidosPendientes() throws SQLException {
		ResultSet pedidos =model.getListaPedidosArray(); // 
		TableModel tmodel = DbUtil.resultSetToTableModel(pedidos);
		almacenView.getTabPedidos().setModel(tmodel);
//		SwingUtil.autoAdjustColumns(view.getTabPedidos());
		
		//Como se guarda la clave del ultimo elemento seleccionado, restaura la seleccion de los detalles
//		this.restoreDetail(); //errores al compilar
	}
	
	/**
	 * 8 noviembre
	 * @throws SQLException 
	 */
	public void getListaOrdenes()  {
		
		try {
			int idPedido =SwingUtil.getSelectedKeyInt(almacenView.getTabPedidos());
			ResultSet ordenes;
			ordenes = model.getListaOrdenes(idPedido);
			TableModel tmodel = DbUtil.resultSetToTableModel(ordenes);
			view.getTabOrden().setModel(tmodel);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} // 
		
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
	 * Tras seleccionar un pedido, se le quedarÃ¡ asignado como OT al almacenero que clicÃ³
	 * NOT: un almacenero tendrÃ¡ varias OT
	 */
	public void asignarAlmacenero() {
		//Obtiene la clave del pedido seleccinada y la guarda para recordar la seleccion en futuras interacciones
		int idPedido =SwingUtil.getSelectedKeyInt(almacenView.getTabPedidos());
		
		try { 
			
			if(almacenero == null)//significa que aun no se ha "iniciado sesion"
				JOptionPane.showMessageDialog(view,"Ingresa un IDAlmacenero valido");
			else {
				int id = almacenero.getIDAlmacenero();
				crearYAsigna(idPedido,id );
			}
		} catch (ApplicationException e) { 
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Al ingresar una orden, mostramos en lista los productos de la OT. Se debe de ver la cantidad de productos pedidos
	 * y la cantidad de productos que faltan por escanear.
	 * A medida que se escanean correctamente, este Ãºltimo campo irÃ¡ disminuyendo hasta cero. 
	 * Cuando llegue a cero, significa que no queda nada por escanear.
	 * @param idOrden 
	 * @throws SQLException 
	 */
	public void mostrarReferencias(int idOrden)  {
		
		
		try {
			int aux = model.unidadesARecoger(idOrden);
			//mientrad haya productos que recoger en esa orden: botón habilitado
			if(aux != 0) {
				view.getBtEscaner().setEnabled(true);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} 
		//consigue los productos de la OT
		ResultSet listPEscaner = null;
		try {
			listPEscaner = model.getListaProductosEscaner(idOrden);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		TableModel tmodel = DbUtil.resultSetToTableModel(listPEscaner);
		view.getTabEscaner().setModel(tmodel);
	}
	
	
	/**
	 * Cada vez que se escanea un producto:
	 * 	-se decrementa las unidades que quedan por recoger
	 * 	-el stock actual se decrementará
	 */
	public void escanear() {
		
		int idProducto = 0;
		try{
			//1 obtener el idProducto del campo de texto
			 idProducto = Integer.parseInt(view.getTxEscaner().getText());
			 int idOt =SwingUtil.getSelectedKeyInt(view.getTabOrden());
			 
			//2 obtener las unidades del scroll
			int uSpinner = (int) view.getSpUnidadesEscaner().getValue();
			System.out.println("uspinner: "+ uSpinner);
			
			//3 comprobar si es posible escanear esas unidades: si no hay suficientes--> mostrar error.		si hay suficientes, se decrementa el unidadesPorRecoger
			int res = model.escanear(idProducto,idOt,uSpinner);
			
			
			if(res == -2) {
				 JOptionPane.showMessageDialog(null,"El producto "+ idProducto+ " no existe en la orden "+ idOt, "Producto inexistente", JOptionPane.INFORMATION_MESSAGE);

			}
			if(res == -1)
				JOptionPane.showMessageDialog(null,"El numero de unidades que intentas escanear supera al numero que queda por recoger", "Unidades insuficientes", JOptionPane.INFORMATION_MESSAGE);
			else {
				//mostrar de nuevo la lista de productos para que se actualicen las unidadesPorRecoger
				//4 si se termino de recoger los productos de la OT, se avisa y deshabilita el boton
				 if(model.unidadesARecoger(idOt) == 0) {
					 view.getBtEscaner().setEnabled(false);
					 JOptionPane.showMessageDialog(null,"Ya no quedan productos por escanear en la orden "+ idOt, "Escaneado listo", JOptionPane.INFORMATION_MESSAGE);
				 }
				mostrarReferencias(idOt);
				
			}
			 
			 
		//manejo de excepciones
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,"El IdProducto solo estaformado por numeros", "IdProducto invalido", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		
	
		
	}
	
	/**
	 * Crea una nueva orden de trabajo asociada con el IDPedido seleccionada
	 * Depsues asigna esta OT al almacenero
	 * @param idPedido, pedido que queremos asignar
	 * @param idAlmacenero , almacenero que se va a quedar con la OT
	 */
	private void crearYAsigna(int idPedido, int idAlmacenero) {
		try {
			model.crearOrden(idPedido,idAlmacenero);
			model.asignarOrden(idAlmacenero);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Escribe una incidencia en la orden de trabajo cuando el almacenero quiera y con el mensaje que 
	 * Ã©l decida
	 */
	private void anotarIncidencia() {
		
//		int idOrden = -1;
//		try {
//			 idOrden = model.getLastOT();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		int idOrden =SwingUtil.getSelectedKeyInt(view.getTabOrden());

//		
		String incidencia = view.getTaIncidencia().getText();
		
		try {
			model.anotarIncidencia(idOrden,incidencia);
			view.getTaIncidenciaVieja().setText(incidencia);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void ayudaOrdenes() {
		int idPedido =SwingUtil.getSelectedKeyInt(almacenView.getTabPedidos());
		 String URL = "jdbc:hsqldb:hsql://localhost";
		 String username = "SA";
		 String password = "";
		 String sqlObtenerProductoPedidos =  "select producto.pos_almacen,producto.columna,producto.idproducto,producto.descripcion,producto.precio from productopedido , producto  where (productopedido.fk_idproducto = producto.idproducto and fk_idpedido = ? ) order by producto.pos_almacen,producto.columna,producto.idproducto,producto.descripcion,producto.precio";
		
		 ResultSet rs = null;
			Connection c;
			try {
				c = DriverManager.getConnection(URL,username,password);
				PreparedStatement pst = c.prepareStatement(sqlObtenerProductoPedidos);
				pst.setInt(1,idPedido);
				  rs = pst.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			List<Producto> lp = new ArrayList<Producto>();
			try {
				while(rs.next()) {
					//System.out.println( "Pasillo: " + rs.getInt(1) + "-"  + "Columna:"+ rs.getInt(2) + "- Id: " + rs.getInt(3) + "-Descripccion:" + rs.getString(4) +"\n");
					lp.add(new Producto(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4)));
				}
			for(Producto p:lp) {
				view.getTaAyudasOrden().setText(view.getTaAyudasOrden().getText() + "Pasillo: " + p.getPasillo() + "-"  + "Columna:"+ p.getColumna()  + "- Id: " + p.getIdProducto()  + "-Descripccion:" + p.getDescripcion()  +"\n");
				System.out.println("Pasillo: " + p.getPasillo() + "-"  + "Columna:"+ p.getColumna()  + "- Id: " + p.getIdProducto()  + "-Descripccion:" + p.getDescripcion()  +"\n");
			}
				


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	private void generarAlbaran() {
		int idPedido =SwingUtil.getSelectedKeyInt(almacenView.getTabPedidos());
		
		if(view.getTaIncidenciaVieja().getText().isEmpty()) {
			try {
				String albaran = "ALBARAN\n";
				albaran += "---------------------\n";
			 albaran +=	new GenerarDocumentacionAction().execute(idPedido);
			Fichero.albaran(albaran, "" + idPedido);
			JOptionPane.showMessageDialog(this.view, "ImprimiendoAlbaran");
			view.setVisible(false);
			almacenView.setVisible(false);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else {
			JOptionPane.showMessageDialog(this.view, "Hay una incidencia y no se puede llevar a empaquetado.");
			view.setVisible(false);
			
		}
		
		
	}
	
	
	/**
	 * Actualiza la base de datos diciendo las unidades a Pedir de cada producto y genera la tabla para el informe
	 * @return el modelo para la tabla del informe
	 * @throws SQLException
	 */
	public TableModel generarInformeStock() throws SQLException{
		model.generarInformeStock();
		
		ResultSet productosStock = model.getListaProductosStock();
		TableModel tmodel = DbUtil.resultSetToTableModel(productosStock);
//		JTable tab = new JTable(tmodel); SI LE DEVUELVO UN JTABLE
		return tmodel; //RETURN TAB
		
	}

	
//	public TableOrdenTrabajo generarInformeOTEmpleadoDia() throws SQLException {
//		this.almaceneros = new ArrayList<Almacenero>();
//		TableOrdenTrabajo  tmodel = model.generarInformeOTEmpleadoDia(almaceneros);
//		return tmodel;
//	}
//	
	 public JTable tablaInformeOT() throws SQLException {
		  DefaultTableModel tableModel = new DefaultTableModel();
	      JTable table = new JTable(tableModel);
	      
	      //gaurdamos los almaceneros de la base de datos
	      ResultSet listAlmaceneros = model.getListaAlmaceneros();
	      this.almaceneros = new ArrayList<Almacenero>();
	      this.almaceneros = model.arrayListAlmaceneros(almaceneros, listAlmaceneros) ;
	      
	      //en cada almacenero guardamos sus ordenes correspondientes
	      model.listarOrdenesPorAlmacenero(almaceneros);
	      
	      //guardamos las distintas fechas en las que existen OT
	      ResultSet fechas = model.getFechasOT();

	      
	      //aádimos las columnas: "FECHA", "ALMACENERO1","ALMACENERO2",...
	      anadirColunmnas(tableModel,this.almaceneros);
	      
	      
	     
	      anadirFilasOT(tableModel,fechas);
	      return table;

		 
	 }
	 
	 private  void anadirColunmnas(DefaultTableModel tableModel, List<Almacenero> almaceneros) throws SQLException {
		 tableModel.addColumn("FECHA");
//		 while(rs.next()){
//			String id = String.valueOf(rs.getInt(1));
//			tableModel.addColumn(id);
////			tableModel.addColumn("FECHA");
////			tableModel.addColumn("ALMACENERO 1");
////			tableModel.addColumn("ALMACENERO 2");
//		}
		 
		 for (int i = 0; i < almaceneros.size(); i++) {
			 String id = String.valueOf(  almaceneros.get(i).getIDAlmacenero());
			tableModel.addColumn(id);
		}
	 }
	 
		private  void anadirFilasOT(DefaultTableModel tableModel, ResultSet rs) throws SQLException {
//			tableModel.insertRow(0, new Object[] { "19/11/2021",25, 23 });
//			  tableModel.insertRow(1, new Object[] { "20/11/2021",15, 22 });
//			  tableModel.insertRow(2, new Object[] { "21/11/2021",14, 20 });
//			  tableModel.insertRow(2, new Object[] { fecha,almaceneros.get(1).getNumOTFecha(fecha), almaceneros.get(2).getNumOTFecha(fecha)});
			  
			//itera por cada fecha con formato '2021-11-27'
			String fecha = "";
			
			
			Object [] o = new Object[almaceneros.size()]; //se crea del tamaño: numAlmacenero + 1 para la fecha
			int[] numOtFecha = new int[almaceneros.size() ];
			int cont = 0;
			while(rs.next()) {
				fecha = rs.getString(1);
				for (int i = 0; i < almaceneros.size(); i++) {
					numOtFecha[i] = model.numOtPorAlmaceneroYFecha(almaceneros.get(i).getIDAlmacenero(), fecha);
					System.out.println(almaceneros.get(i).getIDAlmacenero() + ": " + numOtFecha[i]);

				}
				//tras iterar los almaceneros de esa fecha:
				//PROBLEMA: con más de dos almaceneros no me funciona..
				o = new Object[] { fecha,numOtFecha[0], numOtFecha[1] };
				tableModel.insertRow(cont, o);
				cont++;
			}
			 
			  
		}

		/**
		 * Crea la tabla que contendrá el informe:numero total de productos en una OT, por fecha y cliente
		 * @return
		 * @throws SQLException 
		 */
		public JTable tablaInformeProductosOT() throws SQLException {
			 DefaultTableModel tableModel = new DefaultTableModel();
		      JTable table = new JTable(tableModel);
		      
		      //gaurdamos los almaceneros de la base de datos
		      ResultSet listAlmaceneros = model.getListaAlmaceneros();
		      this.almaceneros = new ArrayList<Almacenero>();
		      this.almaceneros = model.arrayListAlmaceneros(almaceneros, listAlmaceneros) ;
		      
		      //en cada almacenero guardamos sus ordenes correspondientes
		      model.listarOrdenesPorAlmacenero(almaceneros);
		      
		      //guardamos las distintas fechas en las que existen OT
		      ResultSet fechas = model.getFechasOT();

		      
		      //aádimos las columnas: "FECHA", "ALMACENERO1","ALMACENERO2",...
		      anadirColunmnas(tableModel,this.almaceneros);
		      
		      
//		     
		      anadirFilasProductosOT(tableModel,fechas);
		      return table;
		}

		/**
		 * Añade las filas con el numero total de los productos de todas las ot
		 * @param tableModel
		 * @param fechas
		 * @throws SQLException 
		 */
		private void anadirFilasProductosOT(DefaultTableModel tableModel, ResultSet rs) throws SQLException {
			//itera por cada fecha con formato '2021-11-27'
			String fecha = "";
			
			
			Object [] o = new Object[almaceneros.size()]; //se crea del tamaño: numAlmacenero + 1 para la fecha
			int[] numPFecha = new int[almaceneros.size() ];
			int cont = 0;
			while(rs.next()) {
				fecha = rs.getString(1);
				for (int i = 0; i < almaceneros.size(); i++) {
					numPFecha[i] = model.numPFecha(almaceneros.get(i).getIDAlmacenero(), fecha);
					System.out.println(almaceneros.get(i).getIDAlmacenero() + ": " + numPFecha[i]);

				}
				//tras iterar los almaceneros de esa fecha:
				//PROBLEMA: con más de dos almaceneros no me funciona..
				o = new Object[] { fecha,numPFecha[0], numPFecha[1] };
				tableModel.insertRow(cont, o);
				cont++;
			}
			
		}

		

	
	
	
}
