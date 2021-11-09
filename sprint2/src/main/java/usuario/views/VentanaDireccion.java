package usuario.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.UnexpectedException;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import common.database.DatabaseWrapper;
import common.modelo.Pedido;
import common.modelo.Producto;
import usuario.controllers.BusinessLogicUtil;

public class VentanaDireccion {

	private JFrame frame;
	private JPanel panelTitulo;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JPanel panelBoton;
	private JButton btnNewButton;
	private JPanel panelDireccion;
	private JTextField textFieldDireccion;
	private VentanaPrincipal principal;
	private JButton btnVolver;

	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		EventQueue.invokeLater(new Runnable() {
	//			public void run() {
	//				try {
	//					VentanaDireccion window = new VentanaDireccion();
	//					window.frame.setVisible(true);
	//				} catch (Exception e) {
	//					e.printStackTrace();
	//				}
	//			}
	//		});
	//	}

	/**
	 * Create the application.
	 */
	public VentanaDireccion(VentanaPrincipal principal) {
		this.principal = principal;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 624, 386);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(getPanelTitulo(), BorderLayout.NORTH);
		frame.getContentPane().add(getPanelBoton(), BorderLayout.SOUTH);
		frame.getContentPane().add(getPanelDireccion(), BorderLayout.CENTER);
		this.frame.setVisible(true);
		frame.setResizable(false);
	}

	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			panelTitulo = new JPanel();
			panelTitulo.setLayout(new GridLayout(2, 1, 0, 0));
			panelTitulo.add(getLblNewLabel());
			panelTitulo.add(getLblNewLabel_1());
		}
		return panelTitulo;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Tramite");
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lblNewLabel;
	}
	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("Introduzca su direccion");
			lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblNewLabel_1;
	}
	private JPanel getPanelBoton() {
		if (panelBoton == null) {
			panelBoton = new JPanel();
			panelBoton.add(getBtnVolver());
			panelBoton.add(getBtnNewButton());
		}
		return panelBoton;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Siguiente");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (getPrincipal().getUser().equals("Empresa")) {
						tramitar();
					}
					else {
						nextVentana();
					}
				}
			});
			btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnNewButton.setEnabled(false);
		}
		return btnNewButton;
	}
	private JPanel getPanelDireccion() {
		if (panelDireccion == null) {
			panelDireccion = new JPanel();
			panelDireccion.setLayout(null);
			panelDireccion.add(getTextFieldDireccion());
		}
		return panelDireccion;
	}
	JTextField getTextFieldDireccion() {
		if (textFieldDireccion == null) {
			textFieldDireccion = new JTextField();
			textFieldDireccion.setBounds(143, 101, 332, 43);
			textFieldDireccion.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
				    enableButton();
				  }
				  public void removeUpdate(DocumentEvent e) {
				    enableButton();
				  }
				  public void insertUpdate(DocumentEvent e) {
				    enableButton();
				}
				public void enableButton() {
					if (textFieldDireccion.getText().equals(""))
					{
						btnNewButton.setEnabled(false);
					}
					else
					{
						btnNewButton.setEnabled(true);
					}
				}
			});
			textFieldDireccion.setColumns(10);
		}
		return textFieldDireccion;
	}

	public VentanaPrincipal getPrincipal() {
		return principal;
	}
	
	protected void tramitar() {
		try {
			String value = getPrincipal().getTextFieldTotal().getText().substring(0, 
					getPrincipal().getTextFieldTotal().getText().length() - 1);
			int number = (int) (Double.parseDouble(value));
			int total = 0;
			for(Producto p: getPrincipal().getProductos()) {
				if (getPrincipal().getCarrito().containsKey(p)) {
					total += getPrincipal().getCarrito().get(p);
				}
			}
			Pedido pedido = BusinessLogicUtil.createPedido(number, total, getTextFieldDireccion().getText(), "Empresa");
			DatabaseWrapper.createPedido(pedido, getPrincipal().getProductos(),
					new Hashtable<Producto, Integer>(getPrincipal().getCarrito()), getPrincipal().getListaCarrito().getModel());
		} catch (UnexpectedException e) {
			System.err.println(e);
		}
		this.frame.setVisible(false);
		JOptionPane.showMessageDialog(this.frame, "Se ha realizado su compra");
		
	}


	protected void nextVentana() {
		new VentanaPago(this);
		this.frame.setVisible(false);
		
	}

	public void show() {
		this.frame.setVisible(true);
		
	}
	private JButton getBtnVolver() {
		if (btnVolver == null) {
			btnVolver = new JButton("Volver");
			btnVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					previous();
				}
			});
			btnVolver.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnVolver;
	}

	protected void previous() {
		principal.show();
		this.frame.dispose();
	}
}
