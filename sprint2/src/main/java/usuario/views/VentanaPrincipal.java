package usuario.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
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
	private JPanel panelFinalizar;
	private JLabel lblCarrito;
	private JTable listaCarrito;
	private JPanel panelCarritoGestion;
	private JScrollPane panelArticulos;
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
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(25, 25));
		frame.getContentPane().add(getPanelTitulo(), BorderLayout.NORTH);
		frame.getContentPane().add(getPanelCentral(), BorderLayout.CENTER);
		frame.getContentPane().add(getPanelCarrito(), BorderLayout.EAST);
		this.frame.setVisible(true);
		this.frame.setMinimumSize(new Dimension(1000, 600));

		try {
			creaTablero();
		} catch (UnexpectedException e) {
			e.printStackTrace();
		}
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
			panelCentral.add(getPanelFinalizar(), BorderLayout.SOUTH);
			panelCentral.add(getPanelArticulos(), BorderLayout.CENTER);
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

	private JLabel getLblVentanaPrincipal() {
		if (lblVentanaPrincipal == null) {
			lblVentanaPrincipal = new JLabel("Ventana Principal");
			lblVentanaPrincipal.setHorizontalAlignment(SwingConstants.CENTER);
			lblVentanaPrincipal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lblVentanaPrincipal;
	}

	private void creaTablero() throws UnexpectedException {
		try {
			productos = DatabaseWrapper.getProductos();
		} catch (UnexpectedException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;


		for (int i = 0; i < productos.size(); i++) {
			p.add((nuevoPanel(p,productos.get(i))), gbc);
		}
		getPanelArticulos().setViewportView(p);

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
		panel.setSize(new Dimension(200, 600));
		JPanel panel2 = new JPanel();
		SpinnerNumberModel model = new SpinnerNumberModel(1, 0, 9, 1); 
		JSpinner spinner = new JSpinner(model);
		spinner.setValue(1);
		SumarBoton sb = new SumarBoton(spinner);
		panel2.setLayout(new GridLayout(2, 1));
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(nuevoPanel2(pr), BorderLayout.CENTER);
		panel2.add(spinner);
		panel2.add(nuevoBotonSumar(pr, sb));
		panel.add(panel2, BorderLayout.EAST);

		p.setLayout(new GridLayout(5, 1));
		p.add(panel);

		panel.setVisible(true);
		panel.validate();
		panel.repaint();
		return panel;
	}

	private JButton nuevoBotonSumar(Producto pr, SumarBoton sb) {
		JButton boton = new JButton("");
		boton.setBackground(Color.white);
		boton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		boton.setText("Add");
		boton.setActionCommand(String.valueOf(pr.getIdProducto()));
		boton.addActionListener(sb);
		return boton;
	}

	private JPanel nuevoPanel2(Producto pr) {
		JPanel panel = new JPanel();
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
		
		label.setBackground(Color.white);
		label.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		label.setText(pr.getNombre());

		label2.setBackground(Color.white);
		label2.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		label2.setText(pr.getDescripcion());

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
		label3.setFont(new Font("Tahoma", 0, 16));

		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(label2, BorderLayout.CENTER);
		panel.add(label3, BorderLayout.EAST);
		return panel;
	}

	public void addAPedido(int codigo, int unidades) {
		Producto p = getProducto(codigo);
		Integer value = 0;
		if (getCarrito().get(p) != null) {
			value = getCarrito().get(p);
		}


		getCarrito().put(p, value + unidades);
		setModelLista();
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
	
	private JPanel getPanelFinalizar() {
		if (panelFinalizar == null) {
			panelFinalizar = new JPanel();
		}
		return panelFinalizar;
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

	private JScrollPane getPanelArticulos() {
		if (panelArticulos == null) {
			panelArticulos = new JScrollPane();
			panelArticulos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		}
		return panelArticulos;
	}

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
}