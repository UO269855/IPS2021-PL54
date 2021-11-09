package common.modelo;

public class Pedido {
	private int idPedido;
	private int precioTotal;
	private String fecha;
	private String albaran;
	private String direccion;
	private String metodoPago;
	private int unidadesTotales;


	public Pedido(int idPedido, int precioTotal, String fecha, String albaran, String direccion, String metodoPago) {
		super();
		this.idPedido = idPedido;
		this.precioTotal = precioTotal;
		this.fecha = fecha;
		this.albaran = albaran;
		this.direccion = direccion;
		this.metodoPago = metodoPago;
	}
	
	public Pedido() {/**/}
	
	public int getIdPedido() {
		return idPedido;
	}
	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}
	public int getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(int precioTotal) {
		this.precioTotal = precioTotal;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getAlbaran() {
		return albaran;
	}

	public void setAlbaran(String albaran) {
		this.albaran = albaran;
	}
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public String getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	public int getUnidadesTotales() {
		return unidadesTotales;
	}
	public void setUnidadesTotales(int unidadesTotales) {
		this.unidadesTotales = unidadesTotales;
	}


	
}
