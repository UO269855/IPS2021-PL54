package sistema.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import almacenero.controllers.OrdenTrabajoController;
import almacenero.controllers.OrdenTrabajoModel;

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
	@SuppressWarnings("rawtypes")
	private JComboBox combxPedidos;
	private JButton btnMostrar;
	private JScrollPane panelMostrador;
	private JTable tableMostradora;
	private JPanel panelCombo;
	private JPanel panelBoton;
	
	//añadidos para las consultas sobre las OT
	private static OrdenTrabajoModel model;
	private static OrdenTrabajoController control;
	
	

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
	 * @throws SQLException 
	 */
	public VentanaInformes() throws SQLException {
		this.model = new OrdenTrabajoModel();
		this.control = new OrdenTrabajoController(model);
		
		
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getCombxPedidos() {
		if (combxPedidos == null) {
			combxPedidos = new JComboBox();
			combxPedidos.setModel(new DefaultComboBoxModel(new String[] {"Informe paquetes por dia", "OT por fecha y cliente", "Productos de OT por fecha y cliente", "Productos a reponer"}));
		}
		return combxPedidos;
	}
	private JButton getBtnMostrar() {
		if (btnMostrar == null) {
			btnMostrar = new JButton("MOSTRAR");
			btnMostrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						mostrarInforme(getCombxPedidos().getSelectedIndex());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		return btnMostrar;
	}
	
	
	
	/**
	 * Muestra la tabla del informe correspondiente a la opcion del combobox seleccionada en el panel.
	 * index: 0 --> paquetes por dia
	 * index: 1 --> ot por fecha y cliente
	 * index: 2 --> productos en ot por fecha y cliente
	 * index: 3 --> stock
	 * @param selectedItem
	 * @throws SQLException 
	 */
	protected void mostrarInforme(int index) throws SQLException {
		if(index == 1) {
			//ot por fecha y cliente
			JTable tab = control.tablaInformeOT();
			getPanelMostrador().setViewportView(tab);;
		} 
		else if(index == 2) {
			//productos en ot por fecha y cliente
			JTable tab = control.tablaInformeProductosOT();
			getPanelMostrador().setViewportView(tab);;
		}
		
		else if(index == 3) {
			//stock
			JTable tab = control.generarInformeStock();
			getPanelMostrador().setViewportView(tab);;
		}
		
		
	}

	private JScrollPane getPanelMostrador() {
		if (panelMostrador == null) {
			panelMostrador = new JScrollPane();
			panelMostrador.setColumnHeaderView(getTableMostradora());
		}
		return panelMostrador;
	}
	private JTable getTableMostradora() {
		if (tableMostradora == null) {
			tableMostradora = new JTable();
		}
		return tableMostradora;
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
}
