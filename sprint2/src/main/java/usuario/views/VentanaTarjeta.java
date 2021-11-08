package usuario.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.UnexpectedException;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import common.database.DatabaseWrapper;
import common.modelo.Pedido;
import common.modelo.Producto;
import usuario.controllers.BusinessLogicUtil;

public class VentanaTarjeta {

	private JFrame frame;
	private VentanaPago pago;
	private JPanel panelNorth;
	private JPanel panel_1;
	private JPanel panel_2;
	private JLabel lblNewLabel;
	private JButton btnAtras;
	private JButton btnTramitar;
	private JTextField textFieldNumero;
	private JLabel lblNumero;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					VentanaTarjeta window = new VentanaTarjeta();
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
	public VentanaTarjeta(VentanaPago pago) {
		this.pago = pago;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(getPanelNorth(), BorderLayout.NORTH);
		frame.getContentPane().add(getPanel_1(), BorderLayout.CENTER);
		frame.getContentPane().add(getPanel_2(), BorderLayout.SOUTH);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	private JPanel getPanelNorth() {
		if (panelNorth == null) {
			panelNorth = new JPanel();
			panelNorth.add(getLblNewLabel());
		}
		return panelNorth;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setLayout(null);
			panel_1.add(getTextFieldNumero());
			panel_1.add(getLblNumero());
		}
		return panel_1;
	}
	private JPanel getPanel_2() {
		if (panel_2 == null) {
			panel_2 = new JPanel();
			panel_2.add(getBtnAtras());
			panel_2.add(getBtnTramitar());
		}
		return panel_2;
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("Introduza los datos de la tarjeta");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lblNewLabel;
	}
	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atras");
			btnAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnAtras;
	}
	private JButton getBtnTramitar() {
		if (btnTramitar == null) {
			btnTramitar = new JButton("Tramitar");
			btnTramitar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tramitar();
				}
			});

			btnTramitar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnTramitar;
	}
	protected void tramitar() {
		try {
			String value = pago.getPrincipal().getTextFieldTotal().getText().substring(0, 
					pago.getPrincipal().getTextFieldTotal().getText().length() - 1);
			int number = (int) (Double.parseDouble(value));
			int total = 0;
			for(Producto p: pago.getPrincipal().getProductos()) {
				if (pago.getPrincipal().getCarrito().containsKey(p)) {
					total += pago.getPrincipal().getCarrito().get(p);
				}
			}
			Pedido pedido = BusinessLogicUtil.createPedido(number, total, pago.getPrevious().getTextFieldDireccion().getText(), "Tarjeta");
			DatabaseWrapper.createPedido(pedido, pago.getPrincipal().getProductos(),
					new Hashtable<Producto, Integer>(pago.getPrincipal().getCarrito()), pago.getPrincipal().getListaCarrito().getModel());
		} catch (UnexpectedException e) {
			System.err.println(e);
		}
		this.frame.setVisible(false);
		JOptionPane.showMessageDialog(this.frame, "Se ha realizado su compra");
		
	}

	private JTextField getTextFieldNumero() {
		if (textFieldNumero == null) {
			textFieldNumero = new JTextField();
			textFieldNumero.setBounds(76, 75, 296, 32);
			textFieldNumero.addKeyListener(new KeyAdapter() {
		        @Override
		        public void keyPressed(KeyEvent e) {
		            int key = e.getKeyCode();
		            if (Character.isAlphabetic(key)) {
		            	textFieldNumero.setEditable(false);
		            }
		            else if (Character.isDigit(key)) {
		            	textFieldNumero.setEditable(true);
		            }
		        };
		    });
			textFieldNumero.getDocument().addDocumentListener(new DocumentListener() {
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
					if (textFieldNumero.getText().equals(""))
					{
						btnTramitar.setEnabled(false);
					}
					else
					{
						btnTramitar.setEnabled(true);
					}
				}
			});
			textFieldNumero.setColumns(10);
		}
		return textFieldNumero;
	}
	private JLabel getLblNumero() {
		if (lblNumero == null) {
			lblNumero = new JLabel("Introduzca el numero de la tarjeta");
			lblNumero.setFont(new Font("Tahoma", Font.PLAIN, 18));
			lblNumero.setBounds(76, 43, 296, 22);
		}
		return lblNumero;
	}
}
