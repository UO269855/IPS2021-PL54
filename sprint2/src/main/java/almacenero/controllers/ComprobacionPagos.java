package almacenero.controllers;

import common.database.Fichero;

public class ComprobacionPagos {

	public void GenerarComprobanteEmpresa(String empresa) {
		String comprobante = "COMPROBANTE DE PAGO" + "\n";
		comprobante += "--------------------------------------------"+ "\n";
		comprobante += "En este documento queda reflejado que la empresa con correo ( " + empresa +" ) ha realizado un pedido el cual sera tramitado a traves del banco correspondiente." + "\n";
		comprobante += "Se ruega al trabajador encargado que guarde el impreso en la seccion de resguardos" + "\n";
		Fichero.main(empresa, comprobante);
		
	}
	
	
	public boolean comprobarSaldo(String numTarjeta,double aPagar) {
		double cantidad = Fichero.leerTarjeta(numTarjeta);
		if(cantidad >= aPagar) {
			return true;
		}
		return false;
	}
}
