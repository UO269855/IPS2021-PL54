package usuario.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import common.database.DatabaseWrapper;
import common.modelo.Pedido;
import common.modelo.Producto;
import usuario.controllers.BusinessLogicUtil;

public class VentanaPrincipal {

	protected JFrame frame;
	private JPanel panelTítulo;
	private JPanel panelCentral;
	private JPanel panelCarrito;
	private JLabel lblVentanaPrincipal;
	private RestarBoton rb;
	private List<Producto> productos = new ArrayList<Producto>();
	private Hashtable<Producto, Integer> carrito = new Hashtable<>();
	private JPanel panelFinalizar;
	private JButton btnSiguiente;
	private JLabel lblCarrito;
	private JTable listaCarrito;
	private JTextField textFieldTotal;
	private JPanel panelCarritoGestion;
	private JButton btnClearPedido;
	private JButton btnTramitar;
	private JScrollPane panelArticulos;
	private JPanel panelCarritoCentro;
	private JPanel pnProductButtons;
//	private JButton btnIncrementar;
//	private JButton btnDecrementar;
//	private JButton btnBorrar;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void setVisible(boolean b) {
		//this.setVisible(b);
	}

	public VentanaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		rb = new RestarBoton();
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(25, 25));
		frame.getContentPane().add(getPanelTítulo(), BorderLayout.NORTH);
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

	private JPanel getPanelTítulo() {
		if (panelTítulo == null) {
			panelTítulo = new JPanel();
			panelTítulo.add(getLblVentanaPrincipal());
		}
		return panelTítulo;
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
			panelCarrito.setLayout(new BorderLayout(0, 0));
			panelCarrito.add(getLblCarrito(), BorderLayout.NORTH);
			panelCarrito.add(getPanelCarritoGestion(), BorderLayout.SOUTH);
			panelCarrito.add(getPanelCarritoCentro(), BorderLayout.CENTER);
		}
		return panelCarrito;
	}

	private JLabel getLblVentanaPrincipal() {
		if (lblVentanaPrincipal == null) {
			lblVentanaPrincipal = new JLabel("Ventana Principal");
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
			añadirAPedido(Integer.parseInt(bt.getActionCommand()), (int) spinner.getValue());
			if (!carrito.isEmpty()) {
				getBtnTramitar().setEnabled(true);
				getBtnSiguiente().setEnabled(true);
			}
		}
	}

	class RestarBoton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton bt = (JButton) e.getSource();
			restarAPedido(Integer.parseInt(bt.getActionCommand()));
			if (carrito.isEmpty()) {
				getBtnTramitar().setEnabled(false);
				getBtnSiguiente().setEnabled(false);
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
		panel2.setLayout(new GridLayout(3, 1));
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(nuevoPanel2(pr), BorderLayout.CENTER);
		panel2.add(spinner);
		panel2.add(nuevoBotonSumar(pr, sb));
		panel2.add(nuevoBotonRestar(pr));
		panel.add(panel2, BorderLayout.EAST);
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(panel, gbc, 0);
        
		panel.setVisible(true);
		panel.validate();
		panel.repaint();
		return panel;
	}

	private JButton nuevoBotonSumar(Producto pr, SumarBoton sb) {
		JButton boton = new JButton("");
		boton.setBackground(Color.white);
		boton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		boton.setText("Añadir");
		boton.setActionCommand(String.valueOf(pr.getIdProducto()));
		boton.addActionListener(sb);
		return boton;
	}

	private JButton nuevoBotonRestar(Producto pr) {
		JButton boton = new JButton("");
		boton.setBackground(Color.white);
		boton.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		boton.setText("Eliminar");
		boton.setActionCommand(String.valueOf(pr.getIdProducto()));
		boton.addActionListener(rb);
		return boton;
	}

	private JPanel nuevoPanel2(Producto pr) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("");
		JLabel label2 = new JLabel("");
		JLabel label3 = new JLabel("");
		label.setBackground(Color.white);
		label.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		label.setText(pr.getNombre());

		label2.setBackground(Color.white);
		label2.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		label2.setText(pr.getDescripcion());

		label3.setBackground(Color.white);
		label3.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		label3.setText(pr.getPrecio() + "€");

		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(label2, BorderLayout.CENTER);
		panel.add(label3, BorderLayout.EAST);
		return panel;
	}

	public void añadirAPedido(int codigo, int unidades) {
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
		}
		if (getCarrito().get(p) == 0) {
			getCarrito().remove(p);
		}

		setModelLista();
	}

	public void setModelLista() {	
		Object[] tableHeaders = new Object[] { "Producto", "Unidades", "Precio" };
		DefaultTableModel lista = new DefaultTableModel(tableHeaders, getCarrito().size());
		Object[] row = new Object[3];
		double total = 0;

		for (Producto p: productos) {
			if (carrito.containsKey(p)) {
				total += getCarrito().get(p) * p.getPrecio();
				row[0] = p.getNombre();
				row[1] = getCarrito().get(p);
				row[2] = getCarrito().get(p) * p.getPrecio();
				lista.removeRow(0);
				lista.addRow(row);
			}

		}
		listaCarrito.setModel(lista);
		textFieldTotal.setText(String.valueOf(total) + "€");
	}

	public Producto getProducto(int id) {
		for (Producto pr : productos) {
			if (pr.getIdProducto() == id) {
				return pr;
			}
		}
		return null;
	}

	private JPanel getPanelFinalizar() {
		if (panelFinalizar == null) {
			panelFinalizar = new JPanel();
			panelFinalizar.add(getBtnSiguiente());
		}
		return panelFinalizar;
	}

	private JButton getBtnSiguiente() {
		if (btnSiguiente == null) {
			btnSiguiente = new JButton("Siguiente");
			btnSiguiente.setEnabled(false);
		}
		return btnSiguiente;
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

	private JTextField getTextFieldTotal() {
		if (textFieldTotal == null) {
			textFieldTotal = new JTextField();
			textFieldTotal.setText("0 €");
			textFieldTotal.setHorizontalAlignment(SwingConstants.TRAILING);
			textFieldTotal.setEditable(false);
			textFieldTotal.setColumns(10);
		}
		return textFieldTotal;
	}

	private JPanel getPanelCarritoGestion() {
		if (panelCarritoGestion == null) {
			panelCarritoGestion = new JPanel();
			panelCarritoGestion.add(getTextFieldTotal());
			panelCarritoGestion.add(getBtnClearPedido());
			panelCarritoGestion.add(getBtnTramitar());
		}
		return panelCarritoGestion;
	}

	private JButton getBtnClearPedido() {
		if (btnClearPedido == null) {
			btnClearPedido = new JButton("Limpiar Pedido");
			btnClearPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					carrito = new Hashtable<>();
					listaCarrito.setModel(new DefaultTableModel());
					textFieldTotal.setText("0 €");
					getBtnTramitar().setEnabled(false);
					getBtnSiguiente().setEnabled(false);
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

	protected void tramitar() {
		try {
			String value = textFieldTotal.getText().substring(0, textFieldTotal.getText().length() - 1);
			int number = (int) (Double.parseDouble(value));
			Pedido pedido = BusinessLogicUtil.createPedido(number);
			DatabaseWrapper.createPedido(pedido, productos, new Hashtable<Producto, Integer>(getCarrito()), getListaCarrito().getModel());
		} catch (UnexpectedException e) {
			System.err.println(e);
		}
		JOptionPane.showMessageDialog(this.frame, "Se ha realizado su compra");
		getListaCarrito().setModel(new DefaultTableModel());
		getCarrito().clear();
		limpiarCarrito();	
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
//			pnProductButtons.add(getBtnDecrementar());
//			pnProductButtons.add(getBtnBorrar());
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
//	private JButton getBtnDecrementar() {
//		if (btnDecrementar == null) {
//			btnDecrementar = new JButton("Decrementar");
//			btnDecrementar.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					int selectedRow = listaCarrito.getSelectedRow();
//					int quantity = ((int) listaCarrito.getModel().getValueAt(selectedRow, 1));
//
//					if (listaCarrito.getSelectedRow() >= 0) {
//						if (quantity > 1) {
//							listaCarrito.getModel().setValueAt(quantity - 1, selectedRow, 1);
//							carrito.get(listaCarrito.getSelectedRow()).restaUnidades();
//							// tProducts.clearSelection();
//						}
//					}
//				}
//			});
//			btnDecrementar.setMnemonic('d');
//			btnDecrementar.setEnabled(false);
//		}
//		return btnDecrementar;
//	}
//
//	private JButton getBtnBorrar() {
//		if (btnBorrar == null) {
//			btnBorrar = new JButton("Borrar");
//			btnBorrar.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					if (listaCarrito.getSelectedRow() >= 0) {
//						((DefaultTableModel) listaCarrito.getModel()).removeRow(listaCarrito.getSelectedRow());
//						btnBorrar.setEnabled(false);
//						listaCarrito.clearSelection();
//						carrito.remove(arg0)(listaCarrito.getSelectedRow() + 1);
//						setModelLista();
//					}
//				}
//			});
//			btnBorrar.setMnemonic('b');
//			btnBorrar.setEnabled(false);
//		}
//		return btnBorrar;
//	}
}
