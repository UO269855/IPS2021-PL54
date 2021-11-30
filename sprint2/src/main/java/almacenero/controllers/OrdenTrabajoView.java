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
	private JButton btFinalizar;
	private JLabel lbOrden;
	private JScrollPane spOrden;
	private JTable tabOrden;
	private JScrollPane spAyudasOrden;
	private JTextArea taAyudasOrden;
	
	
	
	
	
	
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
		setResizable(false);
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
		contentPane.add(getBtFinalizar());
		contentPane.add(getLbOrden());
		contentPane.add(getSpOrden());
		contentPane.add(getSpAyudasOrden());
		
	}
	
	/**
	 * Método privado que pide el ID del almacenero
	 * @return
	 */
	private String getIdAlmacenero() {
		// TODO Auto-generated method stub
		return null;
	}
	public JTextField getTxEscaner() {
		if (txEscaner == null) {
			txEscaner = new JTextField();
			txEscaner.setColumns(10);
			txEscaner.setBounds(430, 420, 85, 19);
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
			spEscaner.setBounds(40, 207, 620, 185);
			spEscaner.setViewportView(getTabEscaner());
		}
		return spEscaner;
	}
	private JLabel getLbProductosOT() {
		if (lbProductosOT == null) {
			lbProductosOT = new JLabel("Productos de la OT:");
			lbProductosOT.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbProductosOT.setBounds(40, 184, 185, 13);
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
			lbIdProducto.setFont(new Font("Tahoma", Font.PLAIN, 12));
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
			lbIncidencia = new Label("Nueva incidencia:");
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
	
	public JButton getBtFinalizar() {
		if (btFinalizar == null) {
			btFinalizar = new JButton("Finalizar");
			btFinalizar.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btFinalizar.setEnabled(true);
			btFinalizar.setBounds(555, 450, 105, 21);
		}
		return btFinalizar;
	}
	private JLabel getLbOrden() {
		if (lbOrden == null) {
			lbOrden = new JLabel("Elige la orden a gestionar:");
			lbOrden.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbOrden.setBounds(40, 36, 150, 13);
		}
		return lbOrden;
	}
	private JScrollPane getSpOrden() {
		if (spOrden == null) {
			spOrden = new JScrollPane();
			spOrden.setBounds(40, 63, 327, 86);
			spOrden.setViewportView(getTabOrden());
		}
		return spOrden;
	}
	public JTable getTabOrden() {
		if (tabOrden == null) {
			tabOrden = new JTable();
		}
		return tabOrden;
	}
	
	private JScrollPane getSpAyudasOrden() {
		if (spAyudasOrden == null) {
			spAyudasOrden = new JScrollPane();
			spAyudasOrden.setEnabled(true);
			spAyudasOrden.setBounds(40,400, 327, 90);
			spAyudasOrden.setViewportView(getTaAyudasOrden());
		}
		return spAyudasOrden;
	}
	public JTextArea getTaAyudasOrden() {
		if (taAyudasOrden == null) {
			taAyudasOrden = new JTextArea();
			taAyudasOrden.setEnabled(true);
			taAyudasOrden.setEditable(false);
		}
		return taAyudasOrden;
	}
}
