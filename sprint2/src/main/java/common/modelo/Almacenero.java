package common.modelo;

import java.util.ArrayList;
import java.util.List;

public class Almacenero {

	/*clase creada por Alicia para la historia #14832. Al clicar una OT, esta 
	 * quedará asignada al almacenero que la solicitó*/
	

	private int IDAlmacenero;
	private int unidadesOT[];
	private int fechaOT[];
	private List<Integer> ordenes ;
	
	
	
	
	public Almacenero(int IDAlmacenero) {
		this.IDAlmacenero = IDAlmacenero;
		ordenes = new ArrayList<Integer>();
		
	}
	
	
	


	public List<Integer> getOrdenes() {
		return ordenes;
	}





	/**
	 * Añade el idOrden de la OT al almacenero
	 * @param idOrden
	 */
	public void addOrden(int idOrden) {
		ordenes.add(idOrden);
	}
	
	

	public int getIDAlmacenero() {
		return IDAlmacenero;
	}
	private void setIDAlmacenero(int iDAlmacenero) {
		IDAlmacenero = iDAlmacenero;
	}
	

	
}
