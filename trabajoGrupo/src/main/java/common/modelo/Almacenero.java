package common.modelo;

import java.util.ArrayList;
import java.util.List;

public class Almacenero {

	/*clase creada por Alicia para la historia #14832. Al clicar una OT, esta 
	 * quedará asignada al almacenero que la solicitó*/
	
//	private String IDOT;
//	private int IDOT; //CAMBIADA A INT POR FACILIDAD
	//un almacenero tendrá más de una orden de trabajo, por lo que usaré una lista
	private List<Integer> listOT;

	private String IDAlmacenero;
	
	
	
	
	
	public Almacenero(String IDAlmacenero) {
		this.IDAlmacenero = IDAlmacenero;
		this.listOT = new ArrayList<Integer>();
	}
	
	
	
	public List<Integer> getListOT() {
		return listOT;
	}



	private void setListOT(List<Integer> listOT) {
		this.listOT = listOT;
	}



	public String getIDAlmacenero() {
		return IDAlmacenero;
	}
	private void setIDAlmacenero(String iDAlmacenero) {
		IDAlmacenero = iDAlmacenero;
	}
	
	
	/**
	 * Añade una orden de trabajo al almacenero
	 * @param idOt
	 */
	public void addOT(int idOt) {
		//hacer comprobaciones
		if(idOt > 0) {
			this.listOT.add(idOt);
		}
		
	}
	
}
