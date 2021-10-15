package almacenero.controllers;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JList;

public class OrdenTrabajoView extends JFrame {

	private JPanel contentPane;
	private JLabel lblAlmacenero;
	private JTextField tfAlmacenero;
	private JLabel lbPedidosPendientes;
	private JList lstPedidos;

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
	 * Create the frame.
	 */
	public OrdenTrabajoView() {
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
		contentPane.add(getLstPedidos());
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
	private JTextField getTfAlmacenero() {
		if (tfAlmacenero == null) {
			tfAlmacenero = new JTextField();
			tfAlmacenero.setBounds(143, 27, 111, 19);
			tfAlmacenero.setColumns(10);
		}
		return tfAlmacenero;
	}
	private JLabel getLbPedidosPendientes() {
		if (lbPedidosPendientes == null) {
			lbPedidosPendientes = new JLabel("Pedidos pendientes:");
			lbPedidosPendientes.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lbPedidosPendientes.setBounds(40, 107, 141, 13);
		}
		return lbPedidosPendientes;
	}
	private JList getLstPedidos() {
		if (lstPedidos == null) {
			lstPedidos = new JList();
			lstPedidos.setBounds(40, 149, 494, 190);
		}
		return lstPedidos;
	}
}
