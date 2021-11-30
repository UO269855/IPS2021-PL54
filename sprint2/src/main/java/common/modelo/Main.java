package common.modelo;

import java.lang.ProcessHandle.Info;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import almacenero.controllers.AlmacenView;
import almacenero.controllers.OrdenTrabajoController;
import almacenero.controllers.OrdenTrabajoModel;
import almacenero.controllers.OrdenTrabajoView;
import almacenero.controllers.TableOrdenTrabajo;
import almacenero.controllers.VentanaInformeOrden;

/**
 * Programa que lanza el codigo para obtener una orden de trabajo y después comprueba 
 * las referencias de esta
 * @author Alicia Fernández Pushkina -UO275727
 *
 */
public class Main {

	private static OrdenTrabajoModel model;
	private static OrdenTrabajoController control;
	private static OrdenTrabajoView view;//segunda ventana
	private static AlmacenView almacenView; //primera ventana
	
	private static VentanaInformeOrden informeView ;
	
	

	public static void main(String[] args) throws SQLException {
		
		model = new OrdenTrabajoModel();
		view = new OrdenTrabajoView();
		almacenView = new AlmacenView();
		informeView = new VentanaInformeOrden();
		
		control = new OrdenTrabajoController(model, view, almacenView);
//		control = new OrdenTrabajoController(model, view, almacenView, informeView);
		control.initController();
		
	
		//INFORME ORDENES DE TRABAJO POR CLIENTE Y FECHA
		JTable tab = control.tablaInformeOT();
		informeView.getSpInforme().setViewportView(tab);
		informeView.getLbInforme().setText("ORDENES DE TRABAJO POR CLIENTE Y FECHA");
		
//		//INFORME TOTAL DE PRODUCTOS EN ORDEN DE TRABAJO POR  CLIENTE Y FECHA
//		JTable tab2 = control.tablaInformeProductosOT();
//		informeView.getSpInforme().setViewportView(tab2);
//		informeView.getLbInforme().setText("TOTAL DE PRODUCTOS DE OT POR CLIENTE Y FECHA");

		
		
		//pruebas para el informe de stock
		control.generarInformeStock();
		
		

	}

}
