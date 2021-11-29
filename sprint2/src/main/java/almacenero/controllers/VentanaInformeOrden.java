package almacenero.controllers;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.ScrollPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.JLabel;
import java.awt.Font;

public class VentanaInformeOrden extends JFrame {

	
	private JPanel contentPane;
	
	
	public JTable getTabla() {
		return tabla;
	}



	private JTable tabla;
	private JTable table_1;
	private JScrollPane spInforme;
	private JTable tabInforme;
	private JLabel lbInforme;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
//				DefaultTableModel tableModel = new DefaultTableModel();
//			      JTable table = new JTable(tableModel);
//			      //AÑADIR LAS COLUMNAS
//			      anadirColunmnas(tableModel);
//
//			      //INSERTA FILA A FILA(ES DECIR, POR FECHA)
//			      anadirFilas(tableModel);
			      
			      
//			      JTable t = new JTable(new TableOrdenTrabajo());
			      
			      
			      JFrame f = new JFrame();
			      f.setSize(550, 350);
//			      f.getContentPane().add(new JScrollPane(table));				descomentar cuando arriba
			      f.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

			
		});
	}
	


	private static void anadirColunmnas(DefaultTableModel tableModel) {
		tableModel.addColumn("FECHA");
		  tableModel.addColumn("ALMACENERO 1");
		  tableModel.addColumn("ALMACENERO 2");
	}
	
	private static void anadirFilas(DefaultTableModel tableModel) {
		tableModel.insertRow(0, new Object[] { "19/11/2021",25, 23 });
		  tableModel.insertRow(1, new Object[] { "20/11/2021",15, 22 });
		  tableModel.insertRow(2, new Object[] { "21/11/2021",14, 20 });
	}

	/**
	 * Create the frame.
	 */
	public VentanaInformeOrden() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 710, 435);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getSpInforme());////////////////
		contentPane.add(getLbInforme());
		
	}
	private void añadirTabla(JScrollPane scrollPane) {
		DefaultTableModel tableModel = new DefaultTableModel();
      JTable table = new JTable(tableModel);
      tableModel.addColumn("Languages");
      tableModel.insertRow(0, new Object[] { "CSS" });
      tableModel.insertRow(0, new Object[] { "HTML5" });
      tableModel.insertRow(0, new Object[] { "JavaScript" });
      tableModel.insertRow(0, new Object[] { "jQuery" });
      tableModel.insertRow(0, new Object[] { "AngularJS" });
      tableModel.insertRow(tableModel.getRowCount(), new Object[] { "ExpressJS" });
      
//      scInforme.setColumnHeader(table);
		
	}



	public void getTabla(TableOrdenTrabajo tab) {
//		JTable t = new JTable(tab);
//		t.setBounds(117, 100, 533, 212);
//		contentPane.add( t);
		getTable_1().setModel(tab);

		
	}




	private JTable getTable_1() {
		if (table_1 == null) {
			table_1 = new JTable();
			table_1.setBounds(117, 100, 533, 212);
		}
		return table_1;
	}
	public  JScrollPane getSpInforme() {
		if (spInforme == null) {
			spInforme = new JScrollPane();
			spInforme.setBounds(66, 83, 316, 226);
			spInforme.setViewportView(getTabInforme());
		}
		return spInforme;
	}
	public JTable getTabInforme() {
		if (tabInforme == null) {
			tabInforme = new JTable();
		}
		return tabInforme;
	}
	public JLabel getLbInforme() {
		if (lbInforme == null) {
			lbInforme = new JLabel("New label");
			lbInforme.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lbInforme.setBounds(66, 35, 388, 38);
		}
		return lbInforme;
	}
}
