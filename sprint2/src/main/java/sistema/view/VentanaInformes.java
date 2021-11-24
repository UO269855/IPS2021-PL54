package sistema.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class VentanaInformes extends JFrame {

	private JPanel contentPane;
	private JPanel panelTop;
	private JComboBox combxPedidos;
	private JButton btnMostrar;
	private JScrollPane panelMostrador;
	private JTable tableMostradora;

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
			panelTop.add(getCombxPedidos());
			panelTop.add(getBtnMostrar());
		}
		return panelTop;
	}
	private JComboBox getCombxPedidos() {
		if (combxPedidos == null) {
			combxPedidos = new JComboBox();
		}
		return combxPedidos;
	}
	private JButton getBtnMostrar() {
		if (btnMostrar == null) {
			btnMostrar = new JButton("MOSTRAR");
		}
		return btnMostrar;
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
}
