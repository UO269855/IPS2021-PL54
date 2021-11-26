package common.modelo;

public class Producto {
	
	private int idProducto;
	private String nombre;
	private String descripcion;
	private double precio;
	private int unidades;
	private int stock;
	private int stock_min;
	private int stock_rep;
	private int pasillo;
	private String categoria;
	private String subcategoria;
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
			int stock, int stock_min, double iva) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.stock_min = stock_min;
		this.iva = iva;
	}
	
	public Producto(int id, String nombre, String descripcion, double precio,
			int stock, int stock_min, int stock_rep, double iva) {
		this.idProducto = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.stock_min = stock_min;
		this.stock_rep = stock_rep;
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


	public Producto(int id, String nombre, String descripcion, double precio,
			int stock, int stock_min, int stock_rep, double iva, String categoria, String subcategoria) {
		this.idProducto = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.stock_min = stock_min;
		this.stock_rep = stock_rep;
		this.iva = iva;
		this.categoria = categoria;
		this.subcategoria = subcategoria;
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

	public void setUnidades(int stock) {
		this.stock = stock;
	}	
	

	public void restaUnidades() {
		if (stock > 0) {
			stock--;
		}
	}

	public int getUnidades() {
		return unidades;
	}
	
	public String toString(double precio) {
		return getNombre() + " stock: " + getUnidades() + " Total: " + String.valueOf(precio * getUnidades()) + "€";
		
	}

	public void addUnidades(int increment) {
		this.stock += increment;
	}
	
	public int getStock() {
		return stock;
	}


	public void setStockMin(int stock) {
		this.stock_min = stock;
	}
	
	public int getStockMin() {
		return stock_min;
	}


	public void setStockRep(int stock) {
		this.stock_rep = stock;
	}
	
	public int getStockRep() {
		return stock_rep;
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

	public String getCategoria() {
		return this.categoria;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getSubcategoria() {
		return this.subcategoria;
	}


	public void setSubcategoria(String subcategoria) {
		this.subcategoria = subcategoria;
	}

	public String getIvaPercentage() {
		return iva + "%";
	}

	private double getIva() {
		return (iva/100) + 1;
	}

	
}