package common.modelo;

public class ProductoDisplayEscaner {

	private int idProducto;
	private String nombre;
//	private int unidadesPedido;
//	private int unidadesPorEscanear;//??
	
//	/**
//	 * Constructor
//	 * @param idProducto
//	 * @param nombre
//	 * @param unidadesPedido
//	 * @param unidadesProducto
//	 */
//	public ProductoDisplayEscaner(int idProducto, String nombre, int unidadesPedido, int unidadesPorEscanear) {
//		this.idProducto = idProducto;
//		this.nombre = nombre;
//		this.unidadesPedido = unidadesPedido;
//		this.unidadesPorEscanear = unidadesPorEscanear;
//	}
	
	public ProductoDisplayEscaner(int idProducto, String nombre) {
		this.idProducto = idProducto;
		this.nombre = nombre;
	}
	//GETTERS AND SETTERS
	public int getIdProducto() {
		return idProducto;
	}
	private void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}
	public String getNombre() {
		return nombre;
	}
	private void setNombre(String nombre) {
		this.nombre = nombre;
	}
//	public int getUnidadesPorEscanear() {
//		return unidadesPorEscanear;
//	}
//	private void setUnidadesPorEscanear(int unidadesPorEscanear) {
//		this.unidadesPorEscanear = unidadesPorEscanear;
//	}
	
	
	
	
}
