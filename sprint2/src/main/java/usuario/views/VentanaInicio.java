package usuario.views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.UnexpectedException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import common.database.DatabaseWrapper;
import common.modelo.Anonimo;
import common.modelo.User;

public class VentanaInicio {

	private JFrame frame;
	private JPanel panelTitulo;
	private JLabel lblTitulo;
	private JLabel lblSubtitulo;
	private JPanel panelCredenciales;
	private JTextField textFieldInicioSesion;
	private JLabel lblTextField;
	private JLabel lblDni;
	private JButton btnSiguiente;
	private JLabel lblEmpresa;
	private JLabel lblAnonimo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicio window = new VentanaInicio();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		frame.setResizable(false);
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
			lblTitulo = new JLabel("Inicio de Sesion");
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
			panelCredenciales.add(getLblDni());
			panelCredenciales.add(getBtnSiguiente());
			panelCredenciales.add(getLblEmpresa());
			panelCredenciales.add(getLblAnonimo());
		}
		return panelCredenciales;
	}
	private JTextField getTextFieldInicioSesion() {
		if (textFieldInicioSesion == null) {
			textFieldInicioSesion = new JTextField();
			textFieldInicioSesion.setBounds(144, 112, 356, 50);
			textFieldInicioSesion.setColumns(10);
		}
		return textFieldInicioSesion;
	}
	private JLabel getLblTextField() {
		if (lblTextField == null) {
			lblTextField = new JLabel("Credenciales");
			lblTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblTextField.setBounds(54, 118, 99, 32);
		}
		return lblTextField;
	}
	private JLabel getLblDni() {
		if (lblDni == null) {
			lblDni = new JLabel("Introduzca su dni con letra para entrar como cliente.");
			lblDni.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblDni.setHorizontalAlignment(SwingConstants.CENTER);
			lblDni.setBounds(11, 47, 599, 15);
		}
		return lblDni;
	}
	private JButton getBtnSiguiente() {
		if (btnSiguiente == null) {
			btnSiguiente = new JButton("Siguiente");
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						mostrarVentanaPrincipal();
					} catch (UnexpectedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			btnSiguiente.setFont(new Font("Tahoma", Font.PLAIN, 15));
			btnSiguiente.setBounds(232, 187, 136, 32);
		}
		return btnSiguiente;
	}

	protected void mostrarVentanaPrincipal() throws UnexpectedException {
		String checkUsuario = textFieldInicioSesion.getText();
		String result = "";
		User user = null;
		if (checkUsuario.contains("@")) {
			result = "Empresa";
			user = DatabaseWrapper.getEmpresa(checkUsuario);
		}
		else if (checkUsuario.equals("")) {
			result = "Anonimo";
			user = new Anonimo();
		}
		else if (isDni(checkUsuario)) {
			result = "Cliente";
			user = DatabaseWrapper.getCliente(checkUsuario);
		}

		else {
			JOptionPane.showMessageDialog(this.frame, "Introduzca unas credenciales validas");
		}
		if (!result.equals("") && user != null) {
			new VentanaPrincipal(result,user);
		    this.frame.setVisible(false);
		}
		else if (user == null) {
			JOptionPane.showMessageDialog(this.frame, "Ese usuario no existe. Introduzca unas credenciales válidas.");
		}
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
	private JLabel getLblEmpresa() {
		if (lblEmpresa == null) {
			lblEmpresa = new JLabel("Introduzca su email para continuar como empresa.");
			lblEmpresa.setHorizontalAlignment(SwingConstants.CENTER);
			lblEmpresa.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblEmpresa.setBounds(10, 67, 599, 15);
		}
		return lblEmpresa;
	}
	private JLabel getLblAnonimo() {
		if (lblAnonimo == null) {
			lblAnonimo = new JLabel("Pulse siguiente sin introducir datos para acceder como anonimo.");
			lblAnonimo.setHorizontalAlignment(SwingConstants.CENTER);
			lblAnonimo.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblAnonimo.setBounds(11, 87, 599, 15);
		}
		return lblAnonimo;
	}
}
