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
public class OrdenTrabajoView extends JFrame {

	private JPanel contentPane;
	private JLabel lblAlmacenero;
	private JTextField tfAlmacenero;
	private JLabel lbPedidosPendientes;
	private JButton btMostrarPendientes;
	private JScrollPane spPedidos;
	private JTable tabPedidos;
	private JButton btAlmacenero;
	private JTextField txEscaner;
	private JButton btEscaner;
	private JSpinner spUnidadesEscaner;
	private JScrollPane spEscaner;
	private JLabel lbProductosOT;
	private JTable tabEscaner;
	private JLabel lbIdProducto;
	private JButton btIncidencia;
	private JScrollPane spIncidencia;
	private Label lbIncidencia;
	private JTextArea taIncidencia;
	private JScrollPane spIncidenciaVieja;
	private JTextArea taIncidenciaVieja;
	
	
	
	
	
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
		
		setTitle("Orden de trabajo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 551);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblAlmacenero());
		contentPane.add(getTfAlmacenero());
		contentPane.add(getLbPedidosPendientes());
		contentPane.add(getBtMostrarPendientes());
		contentPane.add(getSpPedidos());
		contentPane.add(getBtAlmacenero());
		
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
		btnObtenerReferencias.setBounds(41, 416, 166, 23);
		contentPane.add(btnObtenerReferencias);
		contentPane.add(getTxEscaner());
		contentPane.add(getBtEscaner());
		contentPane.add(getSpUnidadesEscaner());
		contentPane.add(getSpEscaner());
		contentPane.add(getLbProductosOT());
		contentPane.add(getLbIdProducto());
		contentPane.add(getBtIncidencia());
		contentPane.add(getSpIncidencia());
		contentPane.add(getLbIncidencia());
		contentPane.add(getSpIncidenciaVieja());
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
			lbPedidosPendientes.setBounds(40, 136, 141, 13);
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
	public JScrollPane getSpPedidos() {
		if (spPedidos == null) {
			spPedidos = new JScrollPane(getTabPedidos());
			spPedidos.setBounds(40, 159, 303, 108);
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
	public JTextField getTxEscaner() {
		if (txEscaner == null) {
			txEscaner = new JTextField();
			txEscaner.setColumns(10);
			txEscaner.setBounds(430, 418, 85, 19);
		}
		return txEscaner;
	}
	public JButton getBtEscaner() {
		if (btEscaner == null) {
			btEscaner = new JButton("Escanear");
			btEscaner.setEnabled(false);
			btEscaner.setBounds(555, 418, 105, 21);
		}
		return btEscaner;
	}
	public JSpinner getSpUnidadesEscaner() {
		if (spUnidadesEscaner == null) {
			spUnidadesEscaner = new JSpinner();
			spUnidadesEscaner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spUnidadesEscaner.setBounds(525, 419, 30, 20);
		}
		return spUnidadesEscaner;
	}
	private JScrollPane getSpEscaner() {
		if (spEscaner == null) {
			spEscaner = new JScrollPane();
			spEscaner.setBounds(390, 207, 270, 185);
			spEscaner.setViewportView(getTabEscaner());
		}
		return spEscaner;
	}
	private JLabel getLbProductosOT() {
		if (lbProductosOT == null) {
			lbProductosOT = new JLabel("Productos de la OT:");
			lbProductosOT.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbProductosOT.setBounds(390, 185, 185, 13);
		}
		return lbProductosOT;
	}
	public JTable getTabEscaner() {
		if (tabEscaner == null) {
			tabEscaner = new JTable();
		}
		return tabEscaner;
	}
	private JLabel getLbIdProducto() {
		if (lbIdProducto == null) {
			lbIdProducto = new JLabel("IdProducto:");
			lbIdProducto.setBounds(430, 402, 72, 13);
		}
		return lbIdProducto;
	}
	public JButton getBtIncidencia() {
		if (btIncidencia == null) {
			btIncidencia = new JButton("Guardar incidencia");
			btIncidencia.setEnabled(false);
			btIncidencia.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btIncidencia.setBounds(510, 158, 150, 21);
		}
		return btIncidencia;
	}
	private JScrollPane getSpIncidencia() {
		if (spIncidencia == null) {
			spIncidencia = new JScrollPane();
			spIncidencia.setBounds(390, 101, 270, 47);
			spIncidencia.setViewportView(getTaIncidencia());
		}
		return spIncidencia;
	}
	
	private Label getLbIncidencia() {
		if (lbIncidencia == null) {
			lbIncidencia = new Label("Incidencia nueva:");
			lbIncidencia.setBounds(390, 78, 111, 21);
		}
		return lbIncidencia;
	}
	public JTextArea getTaIncidencia() {
		if (taIncidencia == null) {
			taIncidencia = new JTextArea();
			taIncidencia.setEditable(false);
		}
		return taIncidencia;
	}
	private JScrollPane getSpIncidenciaVieja() {
		if (spIncidenciaVieja == null) {
			spIncidenciaVieja = new JScrollPane();
			spIncidenciaVieja.setEnabled(false);
			spIncidenciaVieja.setBounds(390, 29, 270, 43);
			spIncidenciaVieja.setViewportView(getTaIncidenciaVieja());
		}
		return spIncidenciaVieja;
	}
	public JTextArea getTaIncidenciaVieja() {
		if (taIncidenciaVieja == null) {
			taIncidenciaVieja = new JTextArea();
			taIncidenciaVieja.setEnabled(false);
			taIncidenciaVieja.setEditable(false);
		}
		return taIncidenciaVieja;
	}
}
