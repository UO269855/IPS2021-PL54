package common.modelo;

import java.rmi.UnexpectedException;
import java.util.List;

import common.database.DatabaseWrapper;

public class Orden {
	private int IdOrden;
    private int UnidadesTotales;
    private double PrecioTotal;
    private String Fecha;
    private List<Producto> Productos;
    private boolean Valido;
    private String Nombre;
    
	public Orden(int idOrden, int unidadesTotales, double precioTotal, String fecha, List<Producto> productos, boolean valido, String Nombre) {
		IdOrden = idOrden;
		UnidadesTotales = unidadesTotales;
		PrecioTotal = precioTotal;
		Fecha = fecha;
		Productos = productos;
		Valido = valido;
		this.setNombre(Nombre);
	}
	
	public Orden() {/**/}

	public Orden(int i, int calculateUnits, double parseDouble, String format, boolean b) {
		
	}

	public int getIdOrden() {
		return IdOrden;
	}
	public void setIdOrden(int idOrden) {
		IdOrden = idOrden;
	}
	public int getUnidadesTotales() {
		return UnidadesTotales;
	}
	public void setUnidadesTotales(int unidadesTotales) {
		UnidadesTotales = unidadesTotales;
	}
	public double getPrecioTotal() {
		return PrecioTotal;
	}
	public void setPrecioTotal(double precioTotal) {
		PrecioTotal = precioTotal;
	}
	public String getFecha() {
		return Fecha;
	}
	public void setFecha(String fecha) {
		Fecha = fecha;
	}
//	 public List<Producto> getProductos() throws UnexpectedException {
//			return DatabaseWrapper.getProductosPedido(this.IdOrden); // TODO: usar un método específico para las ordenes
//	}
	public void setProductos(List<Producto> productos) {
		Productos = productos;
	}
	public boolean isValido() {
		return Valido;
	}
	public void setValido(boolean valido) {
		Valido = valido;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}
}

/*

 */