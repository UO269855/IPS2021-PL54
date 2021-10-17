package almacenero.controllers;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

public class ReferenciasQueFaltanView extends JFrame {

	private JPanel contentPane;
	private JLabel lbIDOrden;
	private JTextField tfIDOrden;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReferenciasQueFaltanView frame = new ReferenciasQueFaltanView();
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
	public ReferenciasQueFaltanView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbIDOrden());
		contentPane.add(getTfIDOrden());
	}
	private JLabel getLbIDOrden() {
		if (lbIDOrden == null) {
			lbIDOrden = new JLabel("idOrden:");
			lbIDOrden.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbIDOrden.setBounds(34, 42, 61, 13);
		}
		return lbIDOrden;
	}
	private JTextField getTfIDOrden() {
		if (tfIDOrden == null) {
			tfIDOrden = new JTextField();
			tfIDOrden.setEditable(false);
			tfIDOrden.setBounds(89, 40, 50, 19);
			tfIDOrden.setColumns(10);
		}
		return tfIDOrden;
	}
}
