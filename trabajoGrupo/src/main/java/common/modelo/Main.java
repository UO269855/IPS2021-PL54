package common.modelo;

import java.sql.SQLException;

import almacenero.controllers.OrdenTrabajoController;
import almacenero.controllers.OrdenTrabajoModel;
import almacenero.controllers.OrdenTrabajoView;
import almacenero.controllers.ReferenciasQueFaltanView;

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
		
		//historia 14834
		//le paso por parametro el idOrden
//		model.getListaProductosPedidos(1);//idOrden=1
//		control.comprobarUnidades();

	}

}
