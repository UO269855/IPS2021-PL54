package common.database;

import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.table.TableModel;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import common.modelo.Cliente;
import common.modelo.Empresa;
import common.modelo.Pedido;
import common.modelo.Producto;


public class DatabaseWrapper {
	public static final String DRIVER = "org.sqlite.JDBC";
	public static final String URL = "jdbc:sqlite:DB.db";

	// Añade un producto a la BD
	public void addProduct(Producto producto) {
		/* A implementar */
	}

	// Devuelve un producto de la BD con su ID
	public static Producto getProducto(int id_producto) throws UnexpectedException {

		Connection conn = null;
		Producto producto = null;

		try {
			conn = DriverManager.getConnection(URL);
			ResultSetHandler<Producto> resultHandler = new BeanHandler<Producto>(Producto.class);

			String sql = String.format("SELECT * FROM Producto WHERE IdProducto= %d;", id_producto);
			producto = new QueryRunner().query(conn, sql, resultHandler);


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return producto;
	}

	// Devuelve un cliente de la BD 
	public static Cliente getCliente(String dni) throws UnexpectedException {

		Connection conn = null;
		Cliente cliente = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			String check = "\'" + dni + "\'";
			conn = Jdbc.getConnection();
			pst = conn.prepareStatement("SELECT * FROM Cliente where dni = " + check);
			rs = pst.executeQuery();
			while(rs.next()&& cliente == null) {
				cliente = new Cliente(rs.getString("dni"), rs.getString("direccion"));
			}

		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return cliente;
	}

	// Devuelve un cliente de la BD 
	public static Empresa getEmpresa(String email) throws UnexpectedException{

		Connection conn = null;
		Empresa empresa = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = Jdbc.getConnection();
			String check = "\'" + email + "\'";
			pst = conn.prepareStatement("SELECT * FROM Empresa where email = " + check);
			rs = pst.executeQuery();
			while(rs.next() && empresa == null) {
				empresa = new Empresa(rs.getString("email"), rs.getString("direccion"));
			}

		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return empresa;
	}

	// Crea una orden nueva en la bd
	public static void createPedido(Pedido pedido, List<Producto> productos, Hashtable<Producto, Integer> carrito, TableModel table) throws UnexpectedException {
		Connection conn = null;

		try {
			conn = Jdbc.getConnection();

			String sql = String.format(Locale.US, "INSERT INTO Pedido(idpedido, PrecioPedido,Fecha,Albaran, unidadesTotales, direccion, metodoPago) VALUES (%d, %f, '%s', '%s', %d, '%s', '%s');", 
					pedido.getIdPedido(), pedido.getPrecioTotal(), pedido.getFecha(), "NULL", pedido.getUnidadesTotales(), pedido.getDireccion(), pedido.getMetodoPago());

			new QueryRunner().update(conn, sql);

			for(Producto p : productos) {
				if (carrito.containsKey(p)) {
					sql = String.format(
							"INSERT INTO ProductoPedido(fk_IdProducto, fk_IdPedido, unidadespedido, unidadesPorRecoger) VALUES (%d, %d, %d, %d);", //AÑADIDO unidadesPorRecoger POR ALICIA
							p.getIdProducto(), pedido.getIdPedido(), carrito.get(p), carrito.get(p) ); //Aquí queda contar productos AÑADIDO EL SEGUNDO caarrito.get(p) por ALICIA
					new QueryRunner().update(conn, sql);
				}

			}


		} catch (SQLException e) {
			throw new UnexpectedException("Error",e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	// Devuelve una lista de todas las órdenes ordenadas por fecha
	public static List<Pedido> getPedidosByDate() throws UnexpectedException {
		Connection conn = null;
		List<Pedido> pedidos;

		try {
			conn = DriverManager.getConnection(URL);
			BeanListHandler<Pedido> beanListHandler = new BeanListHandler<>(Pedido.class);

			String sql = "SELECT * FROM Pedido ORDER BY Fecha DESC";
			pedidos = new QueryRunner().query(conn, sql, beanListHandler);

		} catch (SQLException e) {
			throw new UnexpectedException(e.getMessage());

		} finally {
			DbUtils.closeQuietly(conn);
		}

		return pedidos;
	}




	public static List<Producto> getProductos() throws UnexpectedException, SQLException {
		Connection conn = Jdbc.getConnection();
		List<Producto> productos = new ArrayList<>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			conn = Jdbc.getConnection();
			pst = conn.prepareStatement("SELECT * FROM PRODUCTO");
			rs = pst.executeQuery();
			while(rs.next()) {
				Producto producto = new Producto(rs.getInt("idproducto"), rs.getString("nombre"), 
						rs.getString("descripcion"), rs.getDouble("precio"), rs.getInt("stock"),  
						rs.getInt("stock_min"), rs.getInt("stock_rep"), rs.getDouble("iva"));
				productos.add(producto);
			}


		} catch (SQLException e) {
			throw new UnexpectedException(e.getMessage());
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return productos;
	}



}
