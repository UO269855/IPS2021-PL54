package almacenero.controllers;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.sql.SQLException;
import java.util.Scanner;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.ScrollPane;
import java.awt.TextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import java.awt.Label;

/**
 * Clase que implementa la ventana donde se muestran los pedidos pendientes y se crea la OT
 * @author Alicia Fernández Pushkina -UO275727
 *
 */
@SuppressWarnings("serial")
public class AlmacenView extends JFrame {

	private JPanel contentPane;
	private JLabel lblAlmacenero;
	private JTextField tfAlmacenero;
	private JLabel lbPedidosPendientes;
	private JScrollPane spPedidos;
	private JTable tabPedidos;
	private JButton btAlmacenero;
	private JButton btObtenerReferencias;
	
	
	
	
	
//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					OrdenTrabajoView frame = new OrdenTrabajoView();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	
	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public AlmacenView() throws SQLException {
		setResizable(false);
		initialize();
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public void initialize() throws SQLException {
		
		setTitle("Almacen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 551);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblAlmacenero());
		contentPane.add(getTfAlmacenero());
		contentPane.add(getLbPedidosPendientes());
		contentPane.add(getSpPedidos());
		contentPane.add(getBtAlmacenero());
		contentPane.add(getBtObtenerReferencias());
	}
	
	private JLabel getLblAlmacenero() {
		if (lblAlmacenero == null) {
			lblAlmacenero = new JLabel("Almacenero ID:");
			lblAlmacenero.setLabelFor(getTfAlmacenero());
			lblAlmacenero.setDisplayedMnemonic('a');
			lblAlmacenero.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblAlmacenero.setBounds(40, 29, 117, 17);
		}
		return lblAlmacenero;
	}
	public JTextField getTfAlmacenero() {
		if (tfAlmacenero == null) {
			tfAlmacenero = new JTextField();
			tfAlmacenero.setBounds(143, 27, 85, 19);
			tfAlmacenero.setColumns(10);
			tfAlmacenero.setText(getIdAlmacenero());
		}
		return tfAlmacenero;
	}
	
	/**
	 * Método privado que pide el ID del almacenero
	 * @return
	 */
	private String getIdAlmacenero() {
		// TODO Auto-generated method stub
		return null;
	}

	private JLabel getLbPedidosPendientes() {
		if (lbPedidosPendientes == null) {
			lbPedidosPendientes = new JLabel("Pedidos pendientes:");
			lbPedidosPendientes.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lbPedidosPendientes.setBounds(40, 79, 141, 13);
		}
		return lbPedidosPendientes;
	}
	
	
	
	/**
	 * incluyo la tabla en un JScrollPane y anyado este en vez de la tabla para poder ver los headers de la tabla
	 * @return tablePanel
	 */
	public JScrollPane getSpPedidos() {
		if (spPedidos == null) {
			spPedidos = new JScrollPane(getTabPedidos());
			spPedidos.setBounds(40, 108, 369, 284);
//			tablePanel.setColumnHeaderView(getTabPedidos());
			
			
		}
		return spPedidos;
	}
	public JTable getTabPedidos() {
		if (tabPedidos == null) {
			tabPedidos = new JTable();
			tabPedidos.setDefaultEditor(Object.class, null); //readonly

			tabPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		}
		return tabPedidos;
	}
	public  JButton getBtAlmacenero() {
		if (btAlmacenero == null) {
			btAlmacenero = new JButton("Confirmar");
			btAlmacenero.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btAlmacenero.setBounds(258, 25, 85, 21);
		}
		return btAlmacenero;
	}
	public JButton getBtObtenerReferencias() {
		if (btObtenerReferencias == null) {
			btObtenerReferencias = new JButton("Obtener referencias");
			btObtenerReferencias.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btObtenerReferencias.setEnabled(false);
			btObtenerReferencias.setBounds(40, 419, 166, 21);
		}
		return btObtenerReferencias;
	}
}
