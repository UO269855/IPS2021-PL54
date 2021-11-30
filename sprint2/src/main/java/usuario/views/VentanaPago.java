package usuario.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

import almacenero.controllers.ComprobacionPagos;
import common.database.DatabaseWrapper;
import common.modelo.Pedido;
import common.modelo.Producto;
import usuario.controllers.BusinessLogicUtil;

public class VentanaPago {

	private JFrame frame;
	private JPanel panelTitulo;
	private JLabel lblTitulo;
	private JPanel panelCardCentro;
	private JPanel panelCard1;
	private JPanel panelCardTarjeta;
	private JButton btnOptionTarjeta;
	private JButton btnOptionTransferencia;
	private JButton btnOptionContrareembolso;
	private JPanel panelAbajo;
	private JButton btnAtras;
	private VentanaDireccion previous;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					VentanaPago window = new VentanaPago();
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
	public VentanaPago(VentanaDireccion ventana ) {
		this.previous = ventana;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setMinimumSize(new Dimension(624, 386));
		frame.getContentPane().add(getPanelTitulo(), BorderLayout.NORTH);
		frame.getContentPane().add(getPanelCardCentro(), BorderLayout.CENTER);
		frame.getContentPane().add(getPanelAbajo(), BorderLayout.SOUTH);
		this.frame.setVisible(true);
		frame.setResizable(false);
	}

	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			panelTitulo = new JPanel();
			panelTitulo.add(getLblTitulo());
		}
		return panelTitulo;
	}
	private JLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new JLabel("Metodo de pago");
			lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lblTitulo;
	}
	private JPanel getPanelCardCentro() {
		if (panelCardCentro == null) {
			panelCardCentro = new JPanel();
			panelCardCentro.setLayout(new CardLayout(0, 0));
			panelCardCentro.add(getPanelCard1(), "name_25506471143600");
			panelCardCentro.add(getPanelCardTarjeta(), "name_25546414223100");
		}
		return panelCardCentro;
	}
	private JPanel getPanelCard1() {
		if (panelCard1 == null) {
			panelCard1 = new JPanel();
			panelCard1.setLayout(new GridLayout(0, 3, 0, 0));
			panelCard1.add(getBtnOptionTarjeta());
			panelCard1.add(getBtnOptionTransferencia());
			panelCard1.add(getBtnOptionContrareembolso());
		}
		return panelCard1;
	}
	private JPanel getPanelCardTarjeta() {
		if (panelCardTarjeta == null) {
			panelCardTarjeta = new JPanel();
		}
		return panelCardTarjeta;
	}
	private JButton getBtnOptionTarjeta() {
		if (btnOptionTarjeta == null) {
			btnOptionTarjeta = new JButton("Tarjeta de credito");
			btnOptionTarjeta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					tramitaTarjeta();
				}
			});
			btnOptionTarjeta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btnOptionTarjeta;
	}
	protected void tramitaTarjeta() {
		this.frame.setVisible(false);
		new VentanaTarjeta(this);
	}

	private JButton getBtnOptionTransferencia() {
		if (btnOptionTransferencia == null) {
			btnOptionTransferencia = new JButton("Transferencia");
			btnOptionTransferencia.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new ComprobacionPagos().GenerarComprobanteTransferencia(previous.getTextFieldDireccion().getText());
					tramitar("Transferencia");
				}
			});
			btnOptionTransferencia.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btnOptionTransferencia;
	}
	protected void tramitar(String metodopago) {
		try {
			double value = getPrincipal().getTotal();
			int totalUnidades = 0;
			for(Producto p: getPrincipal().getProductos()) {
				if (getPrincipal().getCarrito().containsKey(p)) {
					totalUnidades += getPrincipal().getCarrito().get(p);
				}
			}
			Pedido pedido = BusinessLogicUtil.createPedido(value, totalUnidades, previous.getTextFieldDireccion().getText(), metodopago, getPrincipal().getType());
			DatabaseWrapper.createPedido(pedido, getPrincipal().getProductos(),
					new Hashtable<Producto, Integer>(getPrincipal().getCarrito()), getPrincipal().getListaCarrito().getModel());
		} catch (UnexpectedException e) {
			System.err.println(e);
		}
		this.frame.setVisible(false);
		JOptionPane.showMessageDialog(this.frame, "Se ha realizado su compra");
		JOptionPane.showMessageDialog(this.frame, "ImprimiendoComprobante");
	}

	VentanaPrincipal getPrincipal() {
		return previous.getPrincipal();
	}

	private JButton getBtnOptionContrareembolso() {
		if (btnOptionContrareembolso == null) {
			btnOptionContrareembolso = new JButton("Contrareembolso");
			btnOptionContrareembolso.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					new ComprobacionPagos().GenerarComprobanteContrarembolso(previous.getTextFieldDireccion().getText());
					tramitar("Contrareembolso");
				}
			});
			btnOptionContrareembolso.setFont(new Font("Tahoma", Font.PLAIN, 16));
		}
		return btnOptionContrareembolso;
	}
	private JPanel getPanelAbajo() {
		if (panelAbajo == null) {
			panelAbajo = new JPanel();
			panelAbajo.add(getBtnAtras());
		}
		return panelAbajo;
	}
	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Volver");
			btnAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					volver();
				}
			});
		}
		return btnAtras;
	}

	protected void volver() {
		previous.show();
		this.frame.dispose();
	}

	public VentanaDireccion getPrevious() {
		return previous;
	}

	public void show() {
		this.frame.setVisible(true);
	}
}
