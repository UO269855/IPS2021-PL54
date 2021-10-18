package common.modelo;

public class Pedido {
	private int idPedido;
	private int precioTotal;
	private String fecha;
	private String albaran;

	public Pedido(int idPedido, int precioTotal, String fecha, String albaran /*String TipoUsuario*/) {
		super();
		this.idPedido = idPedido;
		this.precioTotal = precioTotal;
		this.fecha = fecha;
		this.albaran = albaran;
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
}
