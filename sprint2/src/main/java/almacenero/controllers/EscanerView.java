package almacenero.controllers;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class EscanerView extends JFrame {

	private JPanel contentPane;
	private JLabel lbIdOrden;
	private JTextField taIdOrden;
	private JButton btIngresar;
	private JTextField txEscaner;
	private JButton btEscaner;

	private JTable listaPedido;
	private JScrollPane spEscaner;
	private JTable tabEscaner;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EscanerView frame = new EscanerView();
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
	public EscanerView() {
		initialize();
	}
	
	public void initialize() {
		setTitle("Escaner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 645, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbIdOrden());
		contentPane.add(getTaIdOrden());
		contentPane.add(getBtIngresar());
		contentPane.add(getTxEscaner());
		contentPane.add(getBtEscaner());
		contentPane.add(getSpEscaner());
		
	}

	private JLabel getLbIdOrden() {
		if (lbIdOrden == null) {
			lbIdOrden = new JLabel("IdOrden:");
			lbIdOrden.setBounds(23, 10, 69, 13);
		}
		return lbIdOrden;
	}
	public JTextField getTaIdOrden() {
		if (taIdOrden == null) {
			taIdOrden = new JTextField();
			taIdOrden.setBounds(81, 7, 69, 19);
			taIdOrden.setColumns(10);
		}
		return taIdOrden;
	}
	public JButton getBtIngresar() {
		if (btIngresar == null) {
			btIngresar = new JButton("Ingresar");
			btIngresar.setBounds(160, 6, 85, 21);
		}
		return btIngresar;
	}
	private JTextField getTxEscaner() {
		if (txEscaner == null) {
			txEscaner = new JTextField();
			txEscaner.setBounds(410, 360, 85, 19);
			txEscaner.setColumns(10);
		}
		return txEscaner;
	}
	public JButton getBtEscaner() {
		if (btEscaner == null) {
			btEscaner = new JButton("Escanear");
			btEscaner.setBounds(505, 359, 105, 21);
		}
		return btEscaner;
	}
	public JScrollPane getSpEscaner() {
		if (spEscaner == null) {
			spEscaner = new JScrollPane();
			spEscaner.setBounds(23, 55, 270, 185);
			spEscaner.setColumnHeaderView(getTabEscaner());
		}
		return spEscaner;
	}
	public JTable getTabEscaner() {
		if (tabEscaner == null) {
			tabEscaner = new JTable();
		}
		return tabEscaner;
	}
}
