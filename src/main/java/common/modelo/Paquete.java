package common.modelo;

import java.util.ArrayList;
import java.util.List;


public class Paquete {

	private int IDPaquete;
	private List<Producto> contenido;
	
	public Paquete(int IDPaquete ) {
		this.IDPaquete =IDPaquete ;
		this.contenido = new ArrayList<Producto>();
	}
	
	
	public void add(Producto p) {
		this.contenido.add(p);
	}
	
	public int getIDPaquete() {
		return this.IDPaquete;
	}
}
