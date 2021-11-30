package sistema.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import almacenero.controllers.GenerarInformePaquetes;

import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class VentanaInformes extends JFrame {

	private JPanel contentPane;
	private JPanel panelTop;
	private JComboBox combxPedidos;
	private JButton btnMostrar;
	private JScrollPane panelMostrador;
	private JPanel panelCombo;
	private JPanel panelBoton;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInformes frame = new VentanaInformes();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaInformes() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 773, 470);
		setTitle("Ventana Informes");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(getPanelTop(), BorderLayout.NORTH);
		contentPane.add(getPanelMostrador(), BorderLayout.CENTER);
	}

	private JPanel getPanelTop() {
		if (panelTop == null) {
			panelTop = new JPanel();
			panelTop.setLayout(new GridLayout(0, 2, 0, 0));
			panelTop.add(getPanelCombo());
			panelTop.add(getPanelBoton());
		}
		return panelTop;
	}
	 private JComboBox getCombxPedidos() {
		if (combxPedidos == null) {
			combxPedidos = new JComboBox();
			combxPedidos.setModel(new DefaultComboBoxModel(new String[] {"informe paquetes por dia","informe ventas tipo cliente","informe ventas metodo pago"}));
		}
		return combxPedidos;
	}
	private JButton getBtnMostrar() {
		if (btnMostrar == null) {
			btnMostrar = new JButton("MOSTRAR");
			btnMostrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(getCombxPedidos().getModel().getSelectedItem().toString().equals("informe paquetes por dia")) {
						try {
							
							getPanel().add(new GenerarInformePaquetes().execute());
							getPanel().validate();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else if(getCombxPedidos().getModel().getSelectedItem().toString().equals("informe ventas tipo cliente")){
						//panelMostrador.setColumnHeaderView(new InformeVentaController().tablaInformeTipoCliente());
					}else if(getCombxPedidos().getModel().getSelectedItem().toString().equals("informe ventas metodo pago")){
						//panelMostrador.setColumnHeaderView(new InformeVentaController().tablaInformeTipoPago());
					}
				}
			});
		}
		return btnMostrar;
	}
	private JScrollPane getPanelMostrador() {
		if (panelMostrador == null) {
			panelMostrador = new JScrollPane();
			panelMostrador.setViewportView(getPanel());
			
		}
		return panelMostrador;
	}
	
	private JPanel getPanelCombo() {
		if (panelCombo == null) {
			panelCombo = new JPanel();
			panelCombo.add(getCombxPedidos());
		}
		return panelCombo;
	}
	private JPanel getPanelBoton() {
		if (panelBoton == null) {
			panelBoton = new JPanel();
			panelBoton.add(getBtnMostrar());
		}
		return panelBoton;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1, 0, 0));
		}
		
			return panel;
	}
}
