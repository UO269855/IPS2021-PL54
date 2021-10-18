package common.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Producto {
	
	private int idProducto;
	private String nombre;
	private String descripcion;
	private double precio;
	private int unidades;
	private int stock;
	private int posAlmacen;
	private int columna;
	
	public Producto() { /* Required by JDBC */ }
	
	
	public Producto(int idProducto, String nombre, String descripcion, double precio) {
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
	}
	
	public Producto(String nombre, String descripcion, double precio,
			int unidades, int stock,int posAlmacen,int columna) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.unidades = unidades;
		this.stock = stock;
		this.posAlmacen = posAlmacen;
		this.columna = columna;
	}


	public Producto(String nombre, String descripcion, double precio,int posAlmacen,int columna) {
		this(nombre, descripcion, precio, 0, 10,posAlmacen,columna);
	}
	
	public Producto(int posAlmacen,int columna,int idProducto, String descipccion ) {
		this.posAlmacen = posAlmacen;
		this.columna = columna;
		this.idProducto = idProducto;
		this.descripcion = descipccion;
	}


	public int getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(int idProducto) {
		idProducto = idProducto;
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

	public double getPrecio() {
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
	
	public String toString() {
		return getNombre() + " Unidades: " + getUnidades() + " Total: " + String.valueOf(getPrecio() * getUnidades()) + "€";
		
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


	public int getPosAlmacen() {
		return posAlmacen;
	}




	public int getColumna() {
		return columna;
	}



	
	
}