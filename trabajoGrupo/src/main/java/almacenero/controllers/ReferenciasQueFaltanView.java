package almacenero.controllers;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class ReferenciasQueFaltanView extends JFrame {

	private JPanel contentPane;
	private JLabel lbIDOrden;
	private JTextField tfIDOrden;
	private JButton btComprobar;
	private JTextArea taIncidencias;

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
		setTitle("comprobarUnidades");
		initialize();
	}

		
	
	public void initialize(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 416, 297);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLbIDOrden());
		contentPane.add(getTfIDOrden());
		contentPane.add(getBtComprobar());
		contentPane.add(getTaIncidencias());
	}
	

	private JLabel getLbIDOrden() {
		if (lbIDOrden == null) {
			lbIDOrden = new JLabel("idOrden:");
			lbIDOrden.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lbIDOrden.setBounds(34, 42, 61, 13);
		}
		return lbIDOrden;
	}
	public JTextField getTfIDOrden() {
		if (tfIDOrden == null) {
			tfIDOrden = new JTextField();
			tfIDOrden.setBounds(89, 40, 50, 19);
			tfIDOrden.setColumns(10);
		}
		return tfIDOrden;
	}
	public  JButton getBtComprobar() {
		if (btComprobar == null) {
			btComprobar = new JButton("Comprobar");
			btComprobar.setFont(new Font("Tahoma", Font.PLAIN, 12));
			btComprobar.setBounds(152, 39, 96, 21);
		}
		return btComprobar;
	}
	public JTextArea getTaIncidencias() {
		if (taIncidencias == null) {
			taIncidencias = new JTextArea();
			taIncidencias.setEditable(false);
			taIncidencias.setFont(new Font("Monospaced", Font.PLAIN, 12));
			taIncidencias.setBounds(34, 86, 358, 56);
		}
		return taIncidencias;
	}
}
