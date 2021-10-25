package common.modelo;

import java.sql.SQLException;

import almacenero.controllers.OrdenTrabajoController;
import almacenero.controllers.OrdenTrabajoModel;
import almacenero.controllers.OrdenTrabajoView;
import almacenero.controllers.ReferenciasQueFaltanView;
import almacenero.controllers.algortimoAlmacenero;

/**
 * Programa que lanza el codigo para obtener una orden de trabajo y después comprueba 
 * las referencias de esta
 * @author Alicia Fernández Pushkina -UO275727
 *
 */
public class Main {

	private static OrdenTrabajoModel model;
	private static OrdenTrabajoController control;
	private static OrdenTrabajoView view;
	
	private static ReferenciasQueFaltanView refView;

	public static void main(String[] args) throws SQLException {
		
		
		model = new OrdenTrabajoModel();
		view = new OrdenTrabajoView();
		
		refView = new ReferenciasQueFaltanView();
		control = new OrdenTrabajoController(model, view,refView);
		control.initController(); 
		

	}

}
