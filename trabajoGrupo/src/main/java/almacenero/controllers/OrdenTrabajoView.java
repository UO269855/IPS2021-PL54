package almacenero.controllers;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.ScrollPane;

public class OrdenTrabajoView extends JFrame {

	private JPanel contentPane;
	private JLabel lblAlmacenero;
	private JTextField tfAlmacenero;
	private JLabel lbPedidosPendientes;
	private JButton btMostrarPendientes;
	private JScrollPane tablePanel;
	private JTable tabPedidos;
	private JButton btAlmacenero;
	
	
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrdenTrabajoView frame = new OrdenTrabajoView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 * @throws SQLException 
	 */
	public OrdenTrabajoView() throws SQLException {
		initialize();
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public void initialize() throws SQLException {
		
		setTitle("frameOT");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 664, 516);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblAlmacenero());
		contentPane.add(getTfAlmacenero());
		contentPane.add(getLbPedidosPendientes());
		contentPane.add(getBtMostrarPendientes());
		contentPane.add(getTablePanel());
		contentPane.add(getBtAlmacenero());
	}
	private JLabel getLblAlmacenero() {
		if (lblAlmacenero == null) {
			lblAlmacenero = new JLabel("Almacenero ID:");
			lblAlmacenero.setLabelFor(getTfAlmacenero());
			lblAlmacenero.setDisplayedMnemonic('a');
			lblAlmacenero.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblAlmacenero.setBounds(40, 26, 117, 17);
		}
		return lblAlmacenero;
	}
	public JTextField getTfAlmacenero() {
		if (tfAlmacenero == null) {
			tfAlmacenero = new JTextField();
			tfAlmacenero.setBounds(143, 27, 111, 19);
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
			lbPedidosPendientes.setBounds(40, 126, 141, 13);
		}
		return lbPedidosPendientes;
	}
	
	public JButton getBtMostrarPendientes() {
		if (btMostrarPendientes == null) {
			btMostrarPendientes = new JButton("Mostrar pedidos pendientes");
			btMostrarPendientes.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btMostrarPendientes.setBounds(40, 73, 206, 21);
		}
		return btMostrarPendientes;
	}
	
	
	
	/**
	 * incluyo la tabla en un JScrollPane y anyado este en vez de la tabla para poder ver los headers de la tabla
	 * @return tablePanel
	 */
	public JScrollPane getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new JScrollPane(getTabPedidos());
			tablePanel.setBounds(40, 149, 303, 108);
//			tablePanel.setColumnHeaderView(getTabPedidos());
			
			
		}
		return tablePanel;
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
			btAlmacenero.setBounds(271, 26, 85, 21);
		}
		return btAlmacenero;
	}
}
