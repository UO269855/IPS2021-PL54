package usuario.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.ListModel;
import javax.swing.table.TableModel;

import common.modelo.Pedido;
import common.modelo.Producto;

public class BusinessLogicUtil {

//	private JFrame mainWindow;

	public BusinessLogicUtil(JFrame mainWindow) {
		super();
//		this.mainWindow = mainWindow;
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
	

	public static String calculateSubtotal(ListModel<Producto> model, String type) {
		double res = 0;
		double precio = 0.0;
		
		for(int i = 0; i< model.getSize(); i++) {
			if (type.equals("Empresa")) {
				precio = model.getElementAt(i).getPrecioEmpresa();
			}
			else {
				precio = model.getElementAt(i).getPrecioCliente();
			}
			
			res += (precio * model.getElementAt(i).getUnidades());
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

	public static double calculateSubtotal(List<Producto> model, String type) {
		double result = 0.0;
		for (Producto producto : model) {
			if (type.equals("Empresa")) {
				result+= producto.getPrecioEmpresa();
			}
			else {
				result+= producto.getPrecioCliente();
			}
		}
		return result;
	}

	public static Pedido createPedido(double precio, int totalUnidades, String direccion, String metodoPago, String tipocliente) {
		Pedido pedido = new Pedido();
		int number= UUID.randomUUID().hashCode();
		if (number <= 0) {
			number *= -1;
		}
		pedido.setIdPedido(number);
		pedido.setPrecioTotal(precio);
		pedido.setFecha(LocalDate.now().toString());
		pedido.setAlbaran(null);
		pedido.setUnidadesTotales(totalUnidades);
		pedido.setDireccion(direccion);
		pedido.setMetodoPago(metodoPago);
		pedido.setTipoCliente(tipocliente);
		return pedido;
	}
}
