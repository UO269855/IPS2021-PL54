package usuario.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import common.database.DatabaseWrapper;
import common.modelo.Producto;
import common.modelo.User;

public class VentanaPrincipal {

	protected JFrame frame;
	private JPanel panelTitulo;
	private JPanel panelCentral;
	private JPanel panelCarrito;
	private JLabel lblVentanaPrincipal;
	private List<Producto> productos = new ArrayList<Producto>();
	private Hashtable<Producto, Integer> carrito = new Hashtable<>();
	private Hashtable<String, List<String>> categorias = new Hashtable<>();
	private JLabel lblCarrito;
	private JTable listaCarrito;
	private JPanel panelCarritoGestion;
	private JScrollPane panelArticulos;
	private JPanel panelCategorias;
	private JPanel panelCarritoCentro;
	private JPanel pnProductButtons;
	private JButton btnDecrementar;
	private JButton btnBorrar;
	private User user;
	private String type;
	private JLabel lblUsuario;
	private String nombreIntroducido;
	private String totalAPagar;
	private JPanel panelBotones;
	private JButton btnClearPedido;
	private JButton btnTramitar;
	private JTextPane textPaneTotal;
	private double precioTotal;
	private JLabel lblCategorias;
	private Boolean checker;
	private JScrollPane scroll;
	private JPanel temp = new JPanel();
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					VentanaPrincipal frame = new VentanaPrincipal();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	protected void setVisible(boolean b) {
		//this.setVisible(b);
	}

	public VentanaPrincipal() {
		 initialize();
	}
	
	public VentanaPrincipal(String type, User usuario) {
		this.type = type;
		this.user = usuario;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1096, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(25, 25));
		frame.getContentPane().add(getPanelTitulo(), BorderLayout.NORTH);
		frame.getContentPane().add(getPanelCentral(), BorderLayout.CENTER);
		frame.getContentPane().add(getPanelCarrito(), BorderLayout.EAST);
		frame.getContentPane().add(getPanelCategorias(), BorderLayout.WEST);
		this.frame.setVisible(true);
		this.frame.setMinimumSize(new Dimension(1200, 700));
		this.frame.setResizable(false);

		try {
			creaTablero();
			creaCategorias();
		} catch (UnexpectedException e) {
			e.printStackTrace();
		}
	}

	private void creaCategorias() {
		try {
			categorias = DatabaseWrapper.getCategorias();
		} catch (UnexpectedException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		JPanel p = new JPanel();		
		p.setLayout(new GridLayout(0,1,0,5));
		for (int i = 0; i < categorias.size(); i++) {
			String nombreCategoria = categorias.keySet().toArray()[i].toString();
			JPanel pCategoria = new JPanel();
			pCategoria.setLayout(new GridLayout(0,1,0,0));
			pCategoria.add(addCategoria(nombreCategoria));	
			pCategoria.add(new JLabel("Subcategorias de " + nombreCategoria));	
			if (categorias.containsKey(nombreCategoria)) {
				for (String subCategoria : categorias.get(nombreCategoria)) {
					pCategoria.add(addSubCategoria(subCategoria));	
				}
			}
			pCategoria.add(new JLabel("------------------------------------------"));
			p.add(pCategoria);
		
		}
		JScrollPane scroll = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelCategorias.add(scroll, BorderLayout.CENTER);
	}

	private JPanel addCategoria(String categoria) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1,0,0));
		JButton buttonCategoria = new JButton(categoria);
		buttonCategoria.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					creaTablero(categoria);
				} catch (UnexpectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		buttonCategoria.setFont(new Font("Tahoma", 0, 16));
		panel.add(buttonCategoria);
		return panel;
	}
	
	private JButton addSubCategoria(String categoria) {
		JButton buttonCategoria = new JButton(categoria);
		buttonCategoria.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					creaTablero2(categoria);
				} catch (UnexpectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		buttonCategoria.setFont(new Font("Tahoma", 0, 16));
		return buttonCategoria;
	}

	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			panelTitulo = new JPanel();
			panelTitulo.setLayout(new GridLayout(2, 1, 0, 0));
			panelTitulo.add(getLblVentanaPrincipal());
			panelTitulo.add(getLblUsuario());
		}
		return panelTitulo;
	}

	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setLayout(new BorderLayout(0, 0));
			//panelCentral.add(getPanelArticulos(), BorderLayout.CENTER);
		}
		return panelCentral;
	}

	private JPanel getPanelCarrito() {
		if (panelCarrito == null) {
			panelCarrito = new JPanel();
			panelCarrito.setLayout(new BorderLayout(0, 10));
			panelCarrito.add(getLblCarrito(), BorderLayout.NORTH);
			panelCarrito.add(getPanelCarritoGestion(), BorderLayout.SOUTH);
			panelCarrito.add(getPanelCarritoCentro(), BorderLayout.CENTER);
		}
		return panelCarrito;
	}
	
	private JPanel getPanelCategorias() {
		if (panelCategorias == null) {
			panelCategorias = new JPanel();
			panelCategorias.setLayout(new BorderLayout(0, 2));
			addTopCategorias();
		}
		return panelCategorias;
	}

	private void addTopCategorias() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2,0,0));
		panel.add(getLblCategorias());
		panel.add(botonReset());
		panelCategorias.add(panel, BorderLayout.NORTH);
		
	}

	private JButton botonReset() {
		JButton btn = new JButton();
		btn.setText("Reiniciar");
		btn.setFont(new Font("Tahoma", 0, 12));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					creaTablero();
				} catch (UnexpectedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		return btn;
	}

	private JLabel getLblVentanaPrincipal() {
		if (lblVentanaPrincipal == null) {
			lblVentanaPrincipal = new JLabel("Ventana Principal");
			lblVentanaPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
			lblVentanaPrincipal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lblVentanaPrincipal;
	}

	private void creaTablero() throws UnexpectedException {
		panelCentral.removeAll();
		if (productos == null || productos.isEmpty() ) {
			try {
				productos = DatabaseWrapper.getProductos();
			} catch (UnexpectedException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
		}
		checker = false;
		JPanel p = new JPanel();
		p.setMinimumSize(new Dimension(200,600));
		p.setLayout(new GridLayout(0,1,0,0));
		for (int i = 0; i < productos.size(); i++) {
			if (productos.get(i).getStock()>=0) {
				p.add((nuevoPanel(p,productos.get(i))));
			}
			
		}
		JScrollPane scroll = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setMinimumSize(new Dimension(300,600));
		this.scroll = scroll;
		p.repaint();
		p.validate();
		scroll.repaint();
		scroll.validate();
		panelCentral.add(scroll);
		panelCentral.repaint();
		panelCentral.validate();
	}
	
	private void creaTablero(String categoria) throws UnexpectedException {
		panelCentral.removeAll();
		if (productos == null || productos.isEmpty() ) {
			try {
				productos = DatabaseWrapper.getProductos();
			} catch (UnexpectedException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
		}
		checker = false;
		JPanel p = new JPanel();
		p.setMinimumSize(new Dimension(200,600));
		p.setLayout(new GridLayout(0,1,0,0));
		for (int i = 0; i < productos.size(); i++) {
			if (productos.get(i).getStock()>=0 && productos.get(i).getCategoria().equals(categoria)) {
				p.add((nuevoPanel(p,productos.get(i))));
			}
			
		}
		JScrollPane scroll = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll = scroll;
		p.repaint();
		p.validate();
		scroll.repaint();
		scroll.validate();
		panelCentral.add(scroll);
		panelCentral.repaint();
		panelCentral.validate();
	}
	private void creaTablero2(String categoria) throws UnexpectedException {
		panelCentral.removeAll();
		if (productos == null || productos.isEmpty() ) {
			try {
				productos = DatabaseWrapper.getProductos();
			} catch (UnexpectedException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
		}
		checker = false;
		JPanel p = new JPanel();
		p.setMinimumSize(new Dimension(200,600));
		p.setLayout(new GridLayout(0,1,0,0));
		for (int i = 0; i < productos.size(); i++) {
			if (productos.get(i).getStock()>0 && productos.get(i).getSubcategoria().equals(categoria)) {
				p.add((nuevoPanel(p,productos.get(i))));
			}
			
		}
		JScrollPane scroll = new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.scroll = scroll;
		p.repaint();
		p.validate();
		scroll.repaint();
		scroll.validate();
		panelCentral.add(scroll);
		panelCentral.repaint();
		panelCentral.validate();
	}

	class SumarBoton implements ActionListener {
		public JSpinner spinner;

		public SumarBoton(JSpinner sp) {
			spinner = sp;
		}

		public void actionPerformed(ActionEvent e) {
			JButton bt = (JButton) e.getSource();
			addAPedido(Integer.parseInt(bt.getActionCommand()), (int) spinner.getValue());
			if (!carrito.isEmpty()) {
				getBtnTramitar().setEnabled(true);
				getBtnBorrar().setEnabled(true);
				getBtnDecrementar().setEnabled(true);
			}
		}
	}

	class RestarBoton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton bt = (JButton) e.getSource();
			restarAPedido(Integer.parseInt(bt.getActionCommand()));
			if (carrito.isEmpty()) {
				getBtnTramitar().setEnabled(false);
			}
		}
	}

	private JPanel nuevoPanel(JPanel p, Producto pr) {
		JPanel panel = new JPanel();
		panel.setSize(new Dimension(p.getSize().height/4, p.getSize().width));
		JPanel panel2 = new JPanel();
		SpinnerNumberModel model = new SpinnerNumberModel(1, 0, pr.getStock(), 1); 
		JSpinner spinner = new JSpinner(model);
		spinner.setFont(new Font("Tahoma", 0, 16));
		spinner.setValue(1);
		SumarBoton sb = new SumarBoton(spinner);
		panel2.setLayout(new GridLayout(2, 1));
		BorderLayout bLayout = new BorderLayout(0, 0);
		bLayout.setHgap(10);
		panel.setLayout(bLayout);
		nuevoPanel2(pr, panel);
		panel2.add(spinner);
		panel2.add(nuevoBotonSumar(pr, sb));
		panel.add(panel2, BorderLayout.EAST);

		p.add(panel);

		panel.setVisible(true);
		panel.validate();
		panel.repaint();
		return panel;
	}

	private JButton nuevoBotonSumar(Producto pr, SumarBoton sb) {
		JButton boton = new JButton("");
		boton.setFont(new Font("Tahoma", 0, 16));
		boton.setBackground(Color.white);
		boton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		boton.setText("   Add   ");
		boton.setActionCommand(String.valueOf(pr.getIdProducto()));
		boton.addActionListener(sb);
		return boton;
	}

	private void nuevoPanel2(Producto pr, JPanel main) {
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JLabel label = new JLabel("");
		JLabel label2 = new JLabel("");
		JLabel label3 = new JLabel("");
		double precio = 0.0;
		
		if (type.equals("Empresa")) {
			precio = pr.getPrecioEmpresa();
		}
		else {
			precio = pr.getPrecioCliente();
		}
		int rows = 1;
		label.setBackground(Color.white);
		label.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		String nombre = pr.getNombre();
		if (nombre.length() <= 30) {
			StringBuffer sf = new StringBuffer(nombre);
			int size = 30-pr.getNombre().length();
			for (int i = 0; i < size; i++) {
				if (i < size/2) {
					sf.insert(0, " ");
				}
				else {
					sf.insert(sf.length(), " ");
				}
			}
			nombre = sf.toString();
		}

		label.setText(nombre);
		
		String descripcion = pr.getDescripcion();
		if (pr.getNombre().length() <= 30) {
			StringBuffer sf = new StringBuffer(descripcion);
			int size = 30-descripcion.length();
			for (int i = 0; i < size; i++) {
				if (i < size/2) {
					sf.insert(0, " ");
				}
				else {
					sf.insert(sf.length(), " ");
				}
			}
			descripcion = sf.toString();
		}
		label2.setBackground(Color.white);
		label2.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		label2.setText(descripcion);

		label3.setBackground(Color.white);
		label3.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		String cadena = "";
		if (type.equals("Empresa")) {
			cadena= "0%";
		}
		else {
			cadena = pr.getIvaPercentage();
		}
		label3.setText(precio + " euros. Incluye " + cadena + " de IVA por ser " + type);

		label.setFont(new Font("Tahoma", 0, 16));
		label2.setFont(new Font("Tahoma", 0, 16));
		label3.setFont(new Font("Tahoma", 0, 16));

	
		panel2.add(label);
		panel2.add(label2);
		panel3.add(label3);
		
		if (pr.getStock() <= pr.getStockMin()) {
			JLabel label4 = new JLabel("QUEDAN POCAS EXISTENCIAS");
			label4.setFont(new Font("Tahoma", 0, 16));
			label4.setBackground(Color.white);
			label4.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
			rows++;
			panel3.add(label4);
		}
		BorderLayout bLayout = new BorderLayout(0, 0);
		bLayout.setHgap(10);
		panel.setLayout(bLayout);
		panel2.setLayout(new GridLayout(2,1,10,0));
		panel3.setLayout(new GridLayout(rows,1, 10, 0));
		
		panel.add(panel2, BorderLayout.WEST);
		panel.add(panel3, BorderLayout.CENTER);
		main.add(panel, BorderLayout.CENTER);
	}

	public void addAPedido(int codigo, int unidades) {
		Producto p = getProducto(codigo);
		Integer value = 0;
		if (getCarrito().get(p) != null) {
			value = getCarrito().get(p);
		}

		if (value + unidades <= p.getStock()) {
			getCarrito().put(p, value + unidades);
			setModelLista();
		}

	}
	
	public void restarAPedido(int codigo) {
		Producto p = getProducto(codigo);
		Integer value = getCarrito().get(p);

		if (getCarrito().containsKey(p)) {
			getCarrito().put(p, value - 1);
			if (getCarrito().get(p) == 0) {
				getCarrito().remove(p);
			}
		}


		setModelLista();
	}

	public void restarAPedido(String codigo) {
		Producto p = getProducto(codigo);
		Integer value = getCarrito().get(p);

		if (getCarrito().containsKey(p)) {
			getCarrito().put(p, value - 1);
			if (getCarrito().get(p) == 0) {
				getCarrito().remove(p);
			}
		}


		setModelLista();
	}

	public void setModelLista() {	
		Object[] tableHeaders = new Object[] { "Producto", "Unidades", "Precio" };
		DefaultTableModel lista = new DefaultTableModel(tableHeaders, getCarrito().size());
		Object[] row = new Object[3];
		double total = 0;
		double noIva = 0.0;
		double iva = 0.0;

		for (Producto p: productos) {
			if (carrito.containsKey(p)) {
				double precio = 0.0;

				if (type.equals("Empresa")) {
					precio = p.getPrecioEmpresa();
				}
				else {
					precio = p.getPrecioCliente();
				}
				noIva += getCarrito().get(p) * p.getPrecioEmpresa();
				total += getCarrito().get(p) * precio;
				row[0] = p.getNombre();
				row[1] = getCarrito().get(p);
				row[2] = getCarrito().get(p) * precio;
				lista.removeRow(0);
				lista.addRow(row);
			}

		}
		DecimalFormat df = new DecimalFormat("#.##");
		iva += total - noIva;
		listaCarrito.clearSelection();
		listaCarrito.setModel(lista);
		String text = String.valueOf(df.format(noIva)) + 
				" Sin Iva \r" + String.valueOf(df.format(iva)) + 
				" Iva total \r" + String.valueOf(df.format(total)) + " total";
		textPaneTotal.setText(text);
		precioTotal = total;
	}

	public Producto getProducto(int id) {
		for (Producto pr : productos) {
			if (pr.getIdProducto() == id ) {
				return pr;
			}
		}
		return productos.get(id);
	}

	public Producto getProducto(String id) {
		for (Producto pr : productos) {
			if (pr.getNombre().equals(id)) {
				return pr;
			}
		}
		return null;
	}

	private JLabel getLblCarrito() {
		if (lblCarrito == null) {
			lblCarrito = new JLabel("Carrito");
			lblCarrito.setHorizontalAlignment(SwingConstants.CENTER);
			lblCarrito.setFont(new Font("Tahoma", Font.PLAIN, 26));
		}
		return lblCarrito;
	}

	JTable getListaCarrito() {
		if (listaCarrito == null) {
			listaCarrito = new JTable();
		}
		return listaCarrito;
	}

	private JPanel getPanelCarritoGestion() {
		if (panelCarritoGestion == null) {
			panelCarritoGestion = new JPanel();
			panelCarritoGestion.setLayout(new GridLayout(0, 2, 0, 0));
			panelCarritoGestion.add(getTextPaneTotal());
			panelCarritoGestion.add(getPanelBotones());
		}
		return panelCarritoGestion;
	}

	public void limpiarCarrito() {
		this.carrito = new Hashtable<>();
	}

	public Hashtable<Producto, Integer> getCarrito() {
		return carrito;
	}

//	private JScrollPane getPanelArticulos() {
//		if (panelArticulos == null) {
//			panelArticulos = new JScrollPane();
//			panelArticulos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		}
//		return panelArticulos;
//	}

	private JPanel getPanelCarritoCentro() {
		if (panelCarritoCentro == null) {
			panelCarritoCentro = new JPanel();
			panelCarritoCentro.setLayout(new BorderLayout(0, 0));
			panelCarritoCentro.add(getListaCarrito());
			panelCarritoCentro.add(getPnProductButtons(), BorderLayout.SOUTH);
		}
		return panelCarritoCentro;
	}

	private JPanel getPnProductButtons() {
		if (pnProductButtons == null) {
			pnProductButtons = new JPanel();
			//			pnProductButtons.add(getBtnIncrementar());
			pnProductButtons.add(getBtnDecrementar());
			pnProductButtons.add(getBtnBorrar());
		}
		return pnProductButtons;
	}

	//	private JButton getBtnIncrementar() {
	//		if (btnIncrementar == null) {
	//			btnIncrementar = new JButton("Incrementar");
	//			btnIncrementar.addActionListener(new ActionListener() {
	//				public void actionPerformed(ActionEvent e) {
	//					int selectedRow = listaCarrito.getSelectedRow();
	//					int quantity = ((int) listaCarrito.getModel().getValueAt(selectedRow, 1));
	//
	//					if (listaCarrito.getSelectedRow() >= 0) {
	//						listaCarrito.getModel().setValueAt(quantity + 1, selectedRow, 1);
	//						carrito.get(listaCarrito.getSelectedRow()).addUnidades(1);
	//					}
	//				}
	//			});
	//			btnIncrementar.setMnemonic('i');
	//			btnIncrementar.setEnabled(false);
	//		}
	//		return btnIncrementar;
	//	}
	//
	private JButton getBtnDecrementar() {
		if (btnDecrementar == null) {
			btnDecrementar = new JButton("Decrementar");
			btnDecrementar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = listaCarrito.getSelectedRow();
					if (selectedRow >= 0) {
						String name = listaCarrito.getModel().getValueAt(selectedRow, 0).toString();
						int quantity = carrito.get(getProducto(name));
						if (quantity > 1) {
							restarAPedido(name);
							setModelLista();
							// tProducts.clearSelection();
						}
					}
				}
			});
			btnDecrementar.setMnemonic('d');
			btnDecrementar.setEnabled(false);
		}
		return btnDecrementar;
	}

	private JButton getBtnBorrar() {
		if (btnBorrar == null) {
			btnBorrar = new JButton("Borrar");
			btnBorrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = listaCarrito.getSelectedRow();
					if (selectedRow >= 0) {
						String name = listaCarrito.getModel().getValueAt(selectedRow, 0).toString();
						eliminarDePedido(name);
					}
				}
			});
			btnBorrar.setMnemonic('b');
			btnBorrar.setEnabled(false);
		}
		return btnBorrar;
	}

	protected void eliminarDePedido(String selectedRow) {
		Producto p = getProducto(selectedRow);
		getCarrito().remove(p);
		setModelLista();
		if (carrito.size() <= 0) {
			getBtnTramitar().setEnabled(false);
			getBtnDecrementar().setEnabled(false);
			getBtnBorrar().setEnabled(false);
		}
	}
	private JLabel getLblUsuario() {
		if (lblUsuario == null) {
			if (!type.equals("Anonimo")) {
				lblUsuario = new JLabel("Bienvenido: " + user.getUsuario());
			}
			else {
				lblUsuario = new JLabel("Bienvenido: Anonimo");
			}
			
			lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lblUsuario;
	}

	public List<Producto> getProductos() {
		return productos;
	}
	
	public String getType() {
		return type;
	}
	
	public User getUser() {
		return user;
	}

	public void show() {
		this.frame.setVisible(true);
	}
	
	public String getNombreIntroducido() {
		return this.nombreIntroducido;
	}

	public String getTotalAPagar() {
		return this.totalAPagar;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new GridLayout(0, 1, 0, 0));
			panelBotones.add(getBtnClearPedido());
			panelBotones.add(getBtnTramitar());
		}
		return panelBotones;
	}
	private JButton getBtnClearPedido() {
		if (btnClearPedido == null) {
			btnClearPedido = new JButton("Limpiar Pedido");
			btnClearPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					carrito = new Hashtable<>();
					listaCarrito.setModel(new DefaultTableModel());
					String text = String.valueOf(0.00) + 
							" Sin Iva \r" + String.valueOf(0.00) + 
							" Iva total \r" + String.valueOf(0.00) + " total";
					textPaneTotal.setText(text);
					getBtnTramitar().setEnabled(false);
					getBtnDecrementar().setEnabled(false);
					getBtnBorrar().setEnabled(false);
				}
			});
		}
		return btnClearPedido;
	}
	private JButton getBtnTramitar() {
		if (btnTramitar == null) {
			btnTramitar = new JButton("Tramitar");
			btnTramitar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tramitar();
				}
			});
			btnTramitar.setEnabled(false);
		}
		return btnTramitar;
	}
	
	private void tramitar() {
		totalAPagar = precioTotal + "";
		new VentanaDireccion(this);
	    this.frame.setVisible(false);	
	}
	
	private JTextPane getTextPaneTotal() {
		if (textPaneTotal == null) {
			textPaneTotal = new JTextPane();
			String text = String.valueOf(0.00) + 
					" Sin Iva \r" + String.valueOf(0.00) + 
					" Iva total \r" + String.valueOf(0.00) + " total";
			textPaneTotal.setText(text);
			textPaneTotal.setEditable(false);
		}
		return textPaneTotal;
	}
	public double getTotal() {
		return precioTotal;
	}
	private JLabel getLblCategorias() {
		if (lblCategorias == null) {
			lblCategorias = new JLabel("Categorias");
			lblCategorias.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return lblCategorias;
	}
}