package usuario.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.ListModel;
import javax.swing.table.TableModel;

import common.modelo.Pedido;
import common.modelo.Producto;

public class BusinessLogicUtil {

	private JFrame mainWindow;

	public BusinessLogicUtil(JFrame mainWindow) {
		super();
		this.mainWindow = mainWindow;
	}
	
	public static String calculateSubtotal(TableModel table) {
		double res = 0;
		int priceCol = 3;
		int multiCol = 1;
		
		for(int i = 0; i< table.getRowCount(); i++) {
			res += ((double)table.getValueAt(i, priceCol)) * ((int)table.getValueAt(i, multiCol));
		}
		return String.format(Locale.US, "%.2f", res);
	}

	

	public static int getUnidades(TableModel table, Producto p) {
		int res = 0;
		
		for(int i = 0; i< table.getRowCount(); i++) {
			if((table.getValueAt(i, 0)) == p.getNombre())
				res = (int) table.getValueAt(i, 1);
		}
		return res;
	}
	

	public static String calculateSubtotal(ListModel<Producto> model) {
		double res = 0;
		
		for(int i = 0; i< model.getSize(); i++) {
			res += (model.getElementAt(i).getPrecio() * model.getElementAt(i).getUnidades());
		}
		return String.format(Locale.US, "%.2f", res);
	}

	public static int calculateUnits(List<Producto> model) {
		int res = 0;
		
		for(int i = 0; i< model.size(); i++) {
			res += model.get(i).getUnidades();
		}
		return res;
	}

	public static List<Producto> getPedidoProductos(TableModel tableModel, List<Producto> carrito) {
		List<Producto> productos = new ArrayList<>();
		List<Producto> model = parseTable(tableModel, carrito);
		for(int i = 0; i< model.size(); i++) {
			Producto current = model.get(i);
			productos.add(new Producto(current.getIdProducto(), current.getNombre(), current.getDescripcion(), current.getPrecio()));
		}
		
		return productos;
		
	}
	
	public static List<Producto> parseTable(TableModel modelo, List<Producto> carrito) {
		List<Producto> lista = new ArrayList<>();
		for (int i = 1; i < modelo.getRowCount(); i++) {
			lista.add(getProducto((String) modelo.getValueAt(i, 0), carrito));
		}
		return lista;
	}

	private static Producto getProducto(String name, List<Producto> carrito) {
		for (Producto p: carrito) {
			if (p.getNombre() == name) {
				return p;
			}
		}
		return null;
	}

	public static double calculateSubtotal(List<Producto> model) {
		double result = 0.0;
		for (Producto producto : model) {
			result+= producto.getPrecio();
		}
		return result;
	}

	public static Pedido createPedido(TableModel tableModel, List<Producto> carrito, int value) {
		Pedido pedido = new Pedido();
		List<Producto> productos = new ArrayList<>();
		List<Producto> model = parseTable(tableModel, carrito);
		for(int i = 0; i< model.size(); i++) {
			Producto current = model.get(i);
			productos.add(new Producto(current.getIdProducto(), current.getNombre(), current.getDescripcion(), current.getPrecio()));
		}
		int number= UUID.randomUUID().hashCode();
		if (number <= 0) {
			number *= -1;
		}
		pedido.setIdPedido(number);
		pedido.setPrecioTotal(value);
		pedido.setFecha(LocalDate.now().toString());
		pedido.setAlbaran(null);
		return pedido;
	}
}
