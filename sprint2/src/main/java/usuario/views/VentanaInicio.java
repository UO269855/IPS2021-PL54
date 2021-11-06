package usuario.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VentanaInicio {

	private JFrame frame;
	private JPanel panelTitulo;
	private JLabel lblTitulo;
	private JLabel lblSubtitulo;
	private JPanel panelCredenciales;
	private JTextField textFieldInicioSesion;
	private JLabel lblTextField;
	private JLabel lblDatos;
	private JButton btnSiguiente;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					VentanaInicio window = new VentanaInicio();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public VentanaInicio() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 624, 386);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(getPanelTitulo(), BorderLayout.NORTH);
		frame.getContentPane().add(getPanelCredenciales(), BorderLayout.CENTER);
	}

	private JPanel getPanelTitulo() {
		if (panelTitulo == null) {
			panelTitulo = new JPanel();
			panelTitulo.setLayout(new GridLayout(2, 1, 0, 0));
			panelTitulo.add(getLblTitulo());
			panelTitulo.add(getLblSubtitulo());
		}
		return panelTitulo;
	}
	private JLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new JLabel("Inicio de Sesi\u00F3n");
			lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 26));
		}
		return lblTitulo;
	}
	private JLabel getLblSubtitulo() {
		if (lblSubtitulo == null) {
			lblSubtitulo = new JLabel("Introduzca sus credenciales");
			lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lblSubtitulo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		}
		return lblSubtitulo;
	}
	private JPanel getPanelCredenciales() {
		if (panelCredenciales == null) {
			panelCredenciales = new JPanel();
			panelCredenciales.setLayout(null);
			panelCredenciales.add(getTextFieldInicioSesion());
			panelCredenciales.add(getLblTextField());
			panelCredenciales.add(getLblDatos());
			panelCredenciales.add(getBtnSiguiente());
		}
		return panelCredenciales;
	}
	private JTextField getTextFieldInicioSesion() {
		if (textFieldInicioSesion == null) {
			textFieldInicioSesion = new JTextField();
			textFieldInicioSesion.setBounds(147, 72, 356, 50);
			textFieldInicioSesion.setColumns(10);
		}
		return textFieldInicioSesion;
	}
	private JLabel getLblTextField() {
		if (lblTextField == null) {
			lblTextField = new JLabel("Credenciales");
			lblTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblTextField.setBounds(44, 78, 99, 32);
		}
		return lblTextField;
	}
	private JLabel getLblDatos() {
		if (lblDatos == null) {
			lblDatos = new JLabel("Dni, Email o nada si ud. es cliente, empresa o quiere continuar como an\u00F3nimo");
			lblDatos.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblDatos.setHorizontalAlignment(SwingConstants.CENTER);
			lblDatos.setBounds(32, 49, 599, 13);
		}
		return lblDatos;
	}
	private JButton getBtnSiguiente() {
		if (btnSiguiente == null) {
			btnSiguiente = new JButton("Siguiente");
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarVentanaPrincipal();
				}
			});
			btnSiguiente.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnSiguiente.setBounds(232, 187, 136, 32);
		}
		return btnSiguiente;
	}

	protected void mostrarVentanaPrincipal() {
		String checkUsuario = textFieldInicioSesion.getText();
		String result = "";
		if (checkUsuario.contains("@")) {
			result = "Empresa";
		}
		if (isDni(checkUsuario)) {
			result = "Cliente";
		}
		if (checkUsuario.equals("")) {
			result = "Anonimo";
		}
		new VentanaPrincipal(result);
	    this.frame.setVisible(false);
		
	}

	private boolean isDni(String checkUsuario) {
		char[] array = checkUsuario.toCharArray();
		boolean result = true;
		for (int i = 0; i < array.length; i++) {
			if (i < array.length - 1 && Character.isDigit(array[i])) {
				continue;
			}
			else if (i == array.length - 1 && Character.isLetter(array[i])) {
				continue;
			}
			else {
				result = false;
				break;
			}
		}
		return result;
	}
}
