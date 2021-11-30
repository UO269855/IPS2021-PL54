package common.modelo;

public class Pedido {
	private int idPedido;
	private double precioTotal;
	private String fecha;
	private String albaran;
	private String direccion;
	private String metodoPago;
	private String tipoCliente;
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
	public double getPrecioTotal() {
		return precioTotal;
	}
	public void setPrecioTotal(double precioTotal) {
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
	
	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	
	public int getUnidadesTotales() {
		return unidadesTotales;
	}
	public void setUnidadesTotales(int unidadesTotales) {
		this.unidadesTotales = unidadesTotales;
	}


	
}
