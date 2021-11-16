package common.modelo;

public class Producto {
	
	private int idProducto;
	private String nombre;
	private String descripcion;
	private double precio;
	private int unidades;
	private int stock;
	private int pasillo;
	private double iva;
	

	private int columna;
	
	public Producto() { /* Required by JDBC */ }
	
	
	public Producto(int idProducto, String nombre, String descripcion, double precio) {
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
	}
	
	public Producto(String nombre, String descripcion, double precio,
			int unidades, int stock, double iva) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.unidades = unidades;
		this.stock = stock;
		this.iva = iva;
	}

	public Producto(int pasillo ,int columna,int idProducto,String descripccion) {
		this.pasillo = pasillo;
		this.columna= columna;
		this.idProducto = idProducto;
		this.descripcion = descripccion;
		
	}

	public Producto(String nombre, String descripcion, double precio, double iva) {
		this(nombre, descripcion, precio, 0, 10, iva);
	}


	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return this.descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecioCliente() {
		return precio * getIva();
	}
	
	public double getPrecioEmpresa() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}	
	

	public void restaUnidades() {
		if (unidades > 0) {
			unidades--;
		}
	}

	public int getUnidades() {
		return unidades;
	}
	
	public String toString(double precio) {
		return getNombre() + " Unidades: " + getUnidades() + " Total: " + String.valueOf(precio * getUnidades()) + "€";
		
	}

	public void addUnidades(int increment) {
		this.unidades += increment;
	}
	
	public int getStock() {
		return stock;
	}


	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getPasillo() {
		return pasillo;
	}


	public int getColumna() {
		return columna;
	}


	public String getIvaPercentage() {
		return iva + "%";
	}

	private double getIva() {
		return (iva/100) + 1;
	}

	
}