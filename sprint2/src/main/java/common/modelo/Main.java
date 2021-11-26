package common.modelo;

import java.sql.SQLException;

import almacenero.controllers.AlmacenView;
import almacenero.controllers.OrdenTrabajoController;
import almacenero.controllers.OrdenTrabajoModel;
import almacenero.controllers.OrdenTrabajoView;

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
	

	public static void main(String[] args) throws SQLException {
		
		model = new OrdenTrabajoModel();
		view = new OrdenTrabajoView();
		almacenView = new AlmacenView();
		
		control = new OrdenTrabajoController(model, view, almacenView);
		control.initController();
	
		

	}

}
