package almacenero.controllers;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;

public class VentanaEmpaquetado extends JFrame {

	private JPanel pnlPrincipal;
	private JPanel panelTop;
	private JPanel panelCenter;
	private JPanel panelBot;
	private JButton btnMostrar;
	private JPanel panelEscanear;
	private JPanel panelFinalizar;
	private JTextField textField;
	private JButton btnEscanear;
	private JButton btnFinalizar;
	private JComboBox comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaEmpaquetado frame = new VentanaEmpaquetado();
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
	public VentanaEmpaquetado() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 396, 564);
		setResizable(false);
		setTitle("Empaquetado");
		pnlPrincipal = new JPanel();
		pnlPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pnlPrincipal);
		pnlPrincipal.setLayout(new BorderLayout(0, 0));
		pnlPrincipal.add(getPanelTop(), BorderLayout.NORTH);
		pnlPrincipal.add(getPanelCenter(), BorderLayout.CENTER);
		pnlPrincipal.add(getPanelBot(), BorderLayout.SOUTH);
		
		
		//AQUI SE INCLUYEN LOS METODOS QUE INICIALIZAN LA VENTANA
		rellenarComboBoxPedidos();
	}
	
	private void rellenarComboBoxPedidos() {
		
	}
	

	private JPanel getPanelTop() {
		if (panelTop == null) {
			panelTop = new JPanel();
			panelTop.setLayout(new GridLayout(1, 0, 0, 0));
			panelTop.add(getComboBox());
			panelTop.add(getBtnMostrar());
		}
		return panelTop;
	}
	private JPanel getPanelCenter() {
		if (panelCenter == null) {
			panelCenter = new JPanel();
		}
		return panelCenter;
	}
	private JPanel getPanelBot() {
		if (panelBot == null) {
			panelBot = new JPanel();
			panelBot.setLayout(new GridLayout(1, 0, 0, 0));
			panelBot.add(getPanelEscanear());
			panelBot.add(getPanelFinalizar());
		}
		return panelBot;
	}
	private JButton getBtnMostrar() {
		if (btnMostrar == null) {
			btnMostrar = new JButton("MOSTRAR");
		}
		return btnMostrar;
	}
	private JPanel getPanelEscanear() {
		if (panelEscanear == null) {
			panelEscanear = new JPanel();
			panelEscanear.add(getTextField());
			panelEscanear.add(getBtnEscanear());
		}
		return panelEscanear;
	}
	private JPanel getPanelFinalizar() {
		if (panelFinalizar == null) {
			panelFinalizar = new JPanel();
			panelFinalizar.add(getBtnFinalizar());
		}
		return panelFinalizar;
	}
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setColumns(10);
		}
		return textField;
	}
	private JButton getBtnEscanear() {
		if (btnEscanear == null) {
			btnEscanear = new JButton("ESCANEAR");
		}
		return btnEscanear;
	}
	private JButton getBtnFinalizar() {
		if (btnFinalizar == null) {
			btnFinalizar = new JButton("FINALIZAR");
		}
		return btnFinalizar;
	}
	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
		}
		return comboBox;
	}
}
