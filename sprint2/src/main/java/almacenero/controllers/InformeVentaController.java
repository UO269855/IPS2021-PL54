package almacenero.controllers;

import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import common.database.DatabaseWrapper;

public class InformeVentaController {
	public JTable tablaInformeTipoCliente() throws SQLException {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Fecha");
		tableModel.addColumn("Cliente");
		tableModel.addColumn("Empresa");
		tableModel.addColumn("Anonimo");
		JTable table = new JTable(tableModel);
		List<String[]> listPedidos = new ArrayList<>();

		try {
			listPedidos = DatabaseWrapper.getPedidosInformeTipoCliente();
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!listPedidos.isEmpty()) {
			for (String[] arrayDatos : listPedidos) {
				tableModel.addRow(arrayDatos);
			}
		}
		return table;	 
	}
	
	public JTable tablaInformeTipoPago() throws SQLException {
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Fecha");
		tableModel.addColumn("Total de Contrareembolso");
		tableModel.addColumn("Total de Empresa");
		tableModel.addColumn("Total de Tarjeta");
		tableModel.addColumn("Total de Transferencia");
		JTable table = new JTable(tableModel);
		List<String[]> listPedidos = new ArrayList<>();

		try {
			listPedidos = DatabaseWrapper.getPedidosInformeTipoPago();
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!listPedidos.isEmpty()) {
			for (String[] arrayDatos : listPedidos) {
				tableModel.addRow(arrayDatos);
			}
		}
		return table;	 
	}
}
