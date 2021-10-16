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
//		view.setVisible(true);
		control = new OrdenTrabajoController(model, view);
		control.initController();

	}

}
