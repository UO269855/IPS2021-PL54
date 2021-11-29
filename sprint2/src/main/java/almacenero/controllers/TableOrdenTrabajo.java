package almacenero.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import common.modelo.Almacenero;

public class TableOrdenTrabajo extends AbstractTableModel {

	
//	private String[] columnNames ={"FECHA","ALMACENERO 1"}; ;//same as before...
	private Object[][] data = {new Object[] { "hoy", "Ali" , "asd"}};;//same as before...

	
	private String[] columnNames  ;//same as before...
//	private Object[][] data;//same as before...

	
	
	public TableOrdenTrabajo(ResultSet listAlmaceneros, Almacenero[] array) throws SQLException {
		setColumnNames(listAlmaceneros, array);
		
	}

	

	@Override
	public int getRowCount() {
		return data.length;
	}

	public void setColumnNames(ResultSet listAlmaceneros, Almacenero[] array) throws SQLException {
		columnNames = new String[array.length +1]; //+1 para la columna de la fecha
		
		columnNames[0] = "FECHA";
		System.out.println("columnName: 0 " + columnNames[0]);

		for (int i = 0; i < array.length; i++) {
			columnNames[i+1] = String.valueOf(array[i].getIDAlmacenero());
			System.out.println("columnName: " + i+1 + " " +columnNames[i+1]);
		}
//		int cont = 1;
//		columnNames[0] = "FECHA";
//		while(listAlmaceneros.next()) {
//			int idAlmacenero = listAlmaceneros.getInt(1);
//			columnNames[cont] = String.valueOf(idAlmacenero);
//			cont++;
//		
//		}
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	public String getColumnName(int col) {
        return columnNames[col];
    }

	
}
