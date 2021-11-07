package usuario.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(getPanelTitulo(), BorderLayout.NORTH);
		frame.getContentPane().add(getPanelBoton(), BorderLayout.SOUTH);
		frame.getContentPane().add(getPanelDireccion(), BorderLayout.CENTER);
		this.frame.setVisible(true);
		this.frame.setMinimumSize(new Dimension(624, 386));
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
			panelBoton.add(getBtnNewButton());
		}
		return panelBoton;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Siguiente");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tramitar();
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
	private JTextField getTextFieldDireccion() {
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




	protected void tramitar() {
		try {
			String value = principal.getTextFieldTotal().getText().substring(0, 
					principal.getTextFieldTotal().getText().length() - 1);
			int number = (int) (Double.parseDouble(value));
			int total = 0;
			for(Producto p: principal.getProductos()) {
				if (principal.getCarrito().containsKey(p)) {
					total += principal.getCarrito().get(p);
				}
			}
			Pedido pedido = BusinessLogicUtil.createPedido(number, total, getTextFieldDireccion().getText());
			DatabaseWrapper.createPedido(pedido, principal.getProductos(),
					new Hashtable<Producto, Integer>(principal.getCarrito()), principal.getListaCarrito().getModel());
		} catch (UnexpectedException e) {
			System.err.println(e);
		}
		JOptionPane.showMessageDialog(this.frame, "Se ha realizado su compra");
		this.frame.dispose();
	}
}
