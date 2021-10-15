package common.modelo;

import java.sql.Date;

public class Pedido {
	private int IdPedido;
	private int IdOrden;
	private int UnidadesTotales;
	private double PrecioTotal;
//	private String Fecha;
	private Date Fecha;
	private String MetodoPago;
	private String Nombre;
	//private String TipoUsuario;

	public Pedido(int idPedido, int idOrden, int unidadesTotales, double precioTotal, Date fecha, String MetodoPago, String Nombre /*String TipoUsuario*/) {
		super();
		IdPedido = idPedido;
		IdOrden = idOrden;
		UnidadesTotales = unidadesTotales;
		PrecioTotal = precioTotal;
		Fecha = fecha; //DD/MM/AAAA
		this.MetodoPago = MetodoPago;
		this.Nombre = Nombre;
		//this.TipoUsuario = TipoUsuario;
	}
	
	public Pedido (int idPedido, Date fecha) {
		this.IdPedido = idPedido;
		this.Fecha = fecha;
	}
	
	public Pedido() {/**/}
	
	public int getIdPedido() {
		return IdPedido;
	}
	public void setIdPedido(int idPedido) {
		IdPedido = idPedido;
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
	public Date getFecha() {
		return Fecha;
	}
	public void setFecha(Date fecha) {
		Fecha = fecha;
	}
	
	public String getMetodoPago() {
		return MetodoPago;
	}

	public void setMetodoPago(String metodoPago) {
		MetodoPago = metodoPago;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	@Override
	public String toString() {
		return "Pedido [IdPedido=" + IdPedido + ", UnidadesTotales=" + UnidadesTotales + ", Fecha=" + Fecha + "]";
	}


	/*
	public String getTipoUsuario() {
		return TipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		TipoUsuario = tipoUsuario;
	}
	*/
	
	
}