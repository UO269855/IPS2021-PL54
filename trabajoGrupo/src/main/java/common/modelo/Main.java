package common.modelo;

import java.sql.SQLException;

import almacenero.controllers.OrdenTrabajoController;
import almacenero.controllers.OrdenTrabajoModel;
import almacenero.controllers.OrdenTrabajoView;

public class Main {

	private static OrdenTrabajoModel model;
	private static OrdenTrabajoController control;
	private static OrdenTrabajoView view;

	public static void main(String[] args) throws SQLException {
		model = new OrdenTrabajoModel();
		view = new OrdenTrabajoView();
		control = new OrdenTrabajoController(model, view);
//		control.initController(); inicializa los listeners de la historia anterior
		
		//historia 14834
		//le paso por parametro el idOrden
//		model.getListaProductosPedidos(1);//idOrden=1
		control.comprobarUnidades(1);

	}

}
