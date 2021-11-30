package almacenero.controllers;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import common.database.DbUtil;
import common.database.Fichero;
import common.modelo.Producto;
import giis.demo.util.SwingUtil;

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

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
	private String URL = "jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	private String sqlBuscarPedidos= "select * from  ordentrabajo where incidencia is null";
	private JScrollPane scrollPane;
	private JTable table;
	private List<String> listaProductosEscaneado;
	private int idPedidoPaquete ;
	private int idAlmacenero;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaEmpaquetado frame = new VentanaEmpaquetado(3333);
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
	public VentanaEmpaquetado(int idAlmacenero ) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 421, 565);
		setResizable(false);
		setTitle("Empaquetado");
		pnlPrincipal = new JPanel();
		pnlPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pnlPrincipal);
		pnlPrincipal.setLayout(new BorderLayout(0, 0));
		pnlPrincipal.add(getPanelTop(), BorderLayout.NORTH);
		pnlPrincipal.add(getPanelCenter(), BorderLayout.CENTER);
		pnlPrincipal.add(getPanelBot(), BorderLayout.SOUTH);
		this.listaProductosEscaneado = new ArrayList<String>();
		this.idAlmacenero = idAlmacenero;
		
		//AQUI SE INCLUYEN LOS METODOS QUE INICIALIZAN LA VENTANA
		try {
			rellenarComboBoxPedidos();
		} catch (SQLException e) {
			System.out.println("fallo al rellenar el combobox");
			e.printStackTrace();
		}
	}
	
	
	private void generarAlbaran() throws SQLException {
		int idPedido = Integer.parseInt(getComboBox().getSelectedItem().toString().split(":")[1]);
		String sql=		"Select * from pedido where idpedido = ?"	;
		

		Connection cn = DriverManager.getConnection(URL,username,password);
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idPedido);
		
		ResultSet pedidos =pstmt.executeQuery();
		pedidos.next();
		
		
		
		
			try {
			String albaran = "ALBARAN\n";
			albaran += "---------------------\n";
			albaran += "PEDIDO:" + " " + idPedido + "\n";
			albaran += "PRECIO TOTAL:" + " " + pedidos.getInt(2) + "\n";
			albaran += "UNIDADES TOTALES:" + " " + pedidos.getInt(4) + "\n";
			albaran += "FECHA:" + " " + pedidos.getDate(3) + "\n";
			albaran += "DIRECCION DE ENVIO:" + " " + pedidos.getString(6) + "\n";
			albaran +=	new GenerarDocumentacionAction().execute(idPedido);
			Fichero.albaran(albaran, "" + idPedido);
			JOptionPane.showMessageDialog(this, "ImprimiendoAlbaran");
			this.setVisible(false);
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
		
		
	
		
		
	}
	@SuppressWarnings("unchecked")
	private void rellenarComboBoxPedidos() throws SQLException {
		
		
		
		Connection c = DriverManager.getConnection(URL,username,password);
		PreparedStatement pst = c.prepareStatement(sqlBuscarPedidos);
		
		 ResultSet rs = pst.executeQuery();
		
		
		while(rs.next()) {
			getComboBox().addItem("IDPEDIDO:" + rs.getInt(2)  );
		}
		
	}
	
	private void finalizarEmpaquetado() {
		try {
			generarPaquete(listaProductosEscaneado);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listaProductosEscaneado.clear();
		
	}
	
	private void generarPaquete(List<String> listaIds) throws SQLException {
		int idPaquete = new Random().nextInt(100);
		while(contienePaquete(idPaquete)) {
			idPaquete = new Random().nextInt(100);
		}
		
		Connection c = DriverManager.getConnection(URL,username,password);
		PreparedStatement pst = c.prepareStatement("insert into paquete values (?,?,?,?,?)");
		
		pst.setInt(1, idPaquete);
		
		pst.setInt(3, idPedidoPaquete);
		pst.setInt(4, idAlmacenero);
		pst.setString(5, LocalDate.now().toString());
		for (int i = 0; i < listaIds.size(); i++) {
			pst.setInt(2, Integer.parseInt(listaIds.get(i)));
			System.out.println(idPaquete  + " " + Integer.parseInt(listaIds.get(i)) + " " + idPedidoPaquete);
			pst.executeUpdate();
			System.out.println("pàso");
		}
		
		
		
		
		
	}
	private boolean contienePaquete(int n) throws SQLException {
		Connection c = DriverManager.getConnection(URL,username,password);
		PreparedStatement pst = c.prepareStatement("select idpaquete from paquete");
		ResultSet rs = pst.executeQuery();
		
		while(rs.next()) {
			if(n == rs.getInt(1)) {
				return true;
			}
		}
		return false;
	}
	
	
	private void mostrarPedido() throws SQLException {
		
		OrdenTrabajoModel model = new OrdenTrabajoModel();
		int idPedido = Integer.parseInt(getComboBox().getSelectedItem().toString().split(":")[1]);
		this.idPedidoPaquete = idPedido;
		String sql=		
				
				"SELECT  distinct p.fk_idproducto, pr.nombre, p.idorden " 
				+ "FROM productoorden p inner join producto pr on p.fk_idproducto = pr.idproducto "
				+ "WHERE p.fk_idpedido = ? "
				+ "group by p.fk_idproducto, pr.nombre, p.idorden "
				
		;
		

		Connection cn = DriverManager.getConnection(URL,username,password);
		PreparedStatement pstmt=cn.prepareStatement(sql);
		pstmt.setInt(1, idPedido);
		
		ResultSet pedidos =pstmt.executeQuery(); // 
		TableModel tmodel = DbUtil.resultSetToTableModel(pedidos);
		getTable().setModel(tmodel);
		
		
	}
	
	
	private void escanear() {
		String idProd = getTextField().getText().trim();
		
		for(int i = 0; i < getTable().getRowCount(); i++) {
			
			if(getTable().getModel().getValueAt(i, 0).toString().trim().equals(idProd)) {
				System.out.println("eliminando producto :" + idProd);
				if(!listaProductosEscaneado.contains(idProd))
					listaProductosEscaneado.add(idProd);
				getTable().getModel().setValueAt( "Escaneado",i,0);;
			}
		}
		
		
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
			panelCenter.setLayout(new BorderLayout(0, 0));
			panelCenter.add(getScrollPane());
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
			btnMostrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						mostrarPedido();
						btnMostrar.setEnabled(false);
						getComboBox().setEnabled(false);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return btnMostrar;
	}
	private JPanel getPanelEscanear() {
		if (panelEscanear == null) {
			panelEscanear = new JPanel();
			panelEscanear.setLayout(new GridLayout(0, 2, 0, 0));
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
			btnEscanear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					escanear();
				}
			});
		}
		return btnEscanear;
	}
	private JButton getBtnFinalizar() {
		if (btnFinalizar == null) {
			btnFinalizar = new JButton("FINALIZAR");
			btnFinalizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(comprobarCompleto()) {
						System.out.println("todos escaneados");
						finalizarEmpaquetado();
						try {
							generarAlbaran();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else {
						System.out.println("faltan escaneos");
						finalizarEmpaquetado();
					}
					
				}
			});
		}
		return btnFinalizar;
	}
	
	private boolean comprobarCompleto() {
		boolean todosEscaneados = true;
		for(int i = 0; i < getTable().getRowCount(); i++) {
			
			if(!getTable().getModel().getValueAt(i, 0).toString().trim().equals("Escaneado")) {
				todosEscaneados = false;
			}
		}
		return todosEscaneados;
	}
	
	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
		}
		return comboBox;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setColumnHeaderView(getTable());
		}
		return scrollPane;
	}
	private JTable getTable() {
		if (table == null) {
			table = new JTable();
		}
		return table;
	}
}
