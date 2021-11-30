package almacenero.controllers;

import java.awt.List;
import java.awt.Robot;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GenerarInformePaquetes {
	private String URL = "jdbc:hsqldb:hsql://localhost";
	private String username = "SA";
	private String password = "";
	
	public JTable execute() throws SQLException {
		String sqlFechas =		"select distinct fecha from paquete";
		String sqlNumeroFechas =		"select count(distinct fecha)  from paquete";
		String sqlAlmaceneros =		"select distinct almacenero_id from paquete";
		String sqlNumeroAlmaceneros =		"select count(distinct almacenero_id)  from paquete";
		
		String sqlConjunta = "select count( distinct idpaquete) from paquete where almacenero_id = ? and fecha = ?";
				
		
		
		Connection cn = DriverManager.getConnection(URL,username,password);
		PreparedStatement pstmt=cn.prepareStatement(sqlNumeroFechas);
		ResultSet rs =pstmt.executeQuery();
		int numeroFechas = 0;
		while(rs.next()) {
			 numeroFechas = rs.getInt(1);
			System.out.println("numero de fechas" + numeroFechas);
		}
		
		int numeroAlmaceneros = 0;
		PreparedStatement pstmt1=cn.prepareStatement(sqlNumeroAlmaceneros);
		ResultSet rs1 =pstmt1.executeQuery();
		while(rs1.next()) {
			 numeroAlmaceneros = rs1.getInt(1);
			System.out.println("numero de almaceneros" + numeroAlmaceneros);
			
		}
		
		
		//nombres almaceneros
		PreparedStatement pstmt2=cn.prepareStatement(sqlAlmaceneros);
		ResultSet rs2 =pstmt2.executeQuery();
		ArrayList<String> almaceneros = new ArrayList<String>();
		while(rs2.next()) {
			almaceneros.add(rs2.getString(1));
			
		}
		
		
		//nombre fechas
		PreparedStatement pstmt3=cn.prepareStatement(sqlFechas);
		ResultSet rs3 =pstmt3.executeQuery();
		ArrayList<String> fechas = new ArrayList<String>();
		while(rs3.next()) {
			fechas.add(rs3.getString(1));
			System.out.println(rs3.getString(1));
		}
		
		
		
		
		DefaultTableModel tableModel = new DefaultTableModel();
		
		
		
		 String[] datosColuma = new String[numeroAlmaceneros + 1];
		 for (int column = 0; column < numeroAlmaceneros + 1; column++) {
			 if(column == 0) {
				 //tableModel.addColumn("Fecha");
				 datosColuma[column] = "Fecha";
				
				
			 }else {
				// tableModel.addColumn( " " + almaceneros.get(column - 1));
				 datosColuma[column] = " " + almaceneros.get(column - 1);
				
			 }
			 tableModel.addColumn("Fecha");
			 System.out.println(column);
			
		 }
	 
		
		 JTable table = new JTable(tableModel);
		 tableModel.addRow(datosColuma);
		 for(int y = 0 ; y < numeroFechas; y++) {
		 
		 String[] datos = new String[numeroAlmaceneros + 1];
		 for(int j=0; j < numeroAlmaceneros + 1; j ++) {
			 //primer bucle recorre la posicion de la fila
			 PreparedStatement pstmt4=cn.prepareStatement(sqlConjunta);
				
				if(j == 0) {
					datos[j] = fechas.get(y);
				}else {
					pstmt4.setInt(1, Integer.parseInt(almaceneros.get(j - 1)));
					pstmt4.setString(2, datos[0]);
					ResultSet rs4 =pstmt4.executeQuery();
					if(rs4.next()) {
						datos[j] = "" + rs4.getInt(1);
					}
					 
				}
				System.out.println(datos[j]);
			    
			 
		 }
		 
		 
		
				
	    tableModel.addRow(datos);
		 }
		 
		 return table;
		
		 
		
		
//		try {
//		    ResultSetMetaData metaData = rs.getMetaData();
//		    int numberOfColumns = metaData.getColumnCount();
//		    Vector<String> columnNames = new Vector<String>();
//
//		    // Get the column names
//		    for (int column = 0; column < numberOfColumns; column++) {
//			columnNames.addElement(metaData.getColumnLabel(column + 1));
//		    }
//
//		    // Get all rows.
//		    Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
//
//		    while (rs.next()) {
//				Vector<Object> newRow = new Vector<Object>();
//	
//				for (int i = 1; i <= numberOfColumns; i++) {
//				    newRow.addElement(rs.getObject(i));
//				}
//	
//				rows.addElement(newRow);
//		    }
//
//		    return new DefaultTableModel(rows, columnNames);
//		} catch (Exception e) {
//		    e.printStackTrace();
//
//		    return null;
//		}
//	    }
		
	}
}
	
