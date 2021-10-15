package common.modelo;

public class Almacenero {

	/*clase creada por Alicia para la historia #14832. Al clicar una OT, esta 
	 * quedará asignada al almacenero que la solicitó*/
	
	private String IDOT;
	private String IDAlmacenero;
	
	
	
	
	
	public Almacenero(String IDOT) {
		this.IDOT = IDOT;
	}
	
	
	public String getIDOT() {
		return IDOT;
	}
	private void setIDOT(String iDOT) {
		IDOT = iDOT;
	}
	public String getIDAlmacenero() {
		return IDAlmacenero;
	}
	private void setIDAlmacenero(String iDAlmacenero) {
		IDAlmacenero = iDAlmacenero;
	}
	
	
}
