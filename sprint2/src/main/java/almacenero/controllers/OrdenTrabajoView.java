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

/**
 * Clase que implementa la ventana donde se muestran los pedidos pendientes y se crea la OT
 * @author Alicia Fernández Pushkina -UO275727
 *
 */
@SuppressWarnings("serial")
public class OrdenTrabajoView extends JFrame {

	private JPanel contentPane;
	private JLabel lblAlmacenero;
	private JTextField tfAlmacenero;
	private JLabel lbPedidosPendientes;
	private JButton btMostrarPendientes;
	private JScrollPane tablePanel;
	private JTable tabPedidos;
	private JButton btAlmacenero;
	private JButton btComprobarOrden;
	
	
	
	
	
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
	public OrdenTrabajoView() throws SQLException {
		initialize();
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public void initialize() throws SQLException {
		
		setTitle("AsignarPedido");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 403, 486);
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
		contentPane.add(getBtComprobarOrden());
		
		JButton btnObtenerReferencias = new JButton("Obtener Referencias");
		btnObtenerReferencias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 int idOrden = 0 ;
				 System.out.println("Introduce el id de la orden a revisar:");
				  Scanner entradaEscaner = new Scanner (System.in);
				  idOrden = Integer.parseInt(entradaEscaner.nextLine()); 
				  
				int idPedido = -1;
				try {
					idPedido = new OrdenTrabajoModel().getIdPedido(idOrden);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if(idPedido == -1) {
					System.out.println("no se encuetra ningun pedido asociado a dicha orden");
					return;
				}
				
				  try {
					new algortimoAlmacenero().execute(idPedido);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnObtenerReferencias.setBounds(23, 418, 166, 23);
		contentPane.add(btnObtenerReferencias);
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
			btAlmacenero.setBounds(258, 25, 85, 21);
		}
		return btAlmacenero;
	}
	public JButton getBtComprobarOrden() {
		if (btComprobarOrden == null) {
			btComprobarOrden = new JButton("Comprobar orden");
			btComprobarOrden.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btComprobarOrden.setBounds(202, 418, 141, 21);
		}
		return btComprobarOrden;
	}
	
	
	
}
