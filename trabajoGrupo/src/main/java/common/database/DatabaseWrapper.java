package common.database;

import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.table.TableModel;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import common.modelo.Orden;
import common.modelo.Pedido;
import common.modelo.Producto;
import usuario.controllers.BusinessLogicUtil;


public class DatabaseWrapper {
	private Connection conn;
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

	// Crea una orden nueva en la bd
	public static void createOrder(Orden orden, List<Producto> productos, TableModel table) throws UnexpectedException {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL);
			
			String sql = String.format(Locale.US, "INSERT INTO OrdenTrabajo(UnidadesTotales,PrecioTotal,Fecha,Valido) VALUES (%d, %f, datetime('now'), false);",
					orden.getUnidadesTotales(),orden.getPrecioTotal());
			
			new QueryRunner().update(conn, sql);
			
			for(Producto p : productos) {
				sql = String.format(
						"INSERT INTO ProductoOrden(IdProducto,unidades) VALUES (%d, %d, %d);",
						p.getIdProducto(), BusinessLogicUtil.getUnidades(table, p) ); //Aquí queda contar productos
				new QueryRunner().update(conn, sql);
			}
			

		} catch (SQLException e) {
			throw new UnexpectedException("Error",e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	// Devuelve una orden con su ID
	public static Orden getOrden(int id_orden) throws UnexpectedException {
		Connection conn = null;
		Orden orden;

		try {
			conn = DriverManager.getConnection(URL);
			ResultSetHandler<Orden> resultHandler = new BeanHandler<Orden>(Orden.class);

			String sql = String.format("SELECT * FROM Ordenes WHERE IdOrden= %", id_orden);
			orden = new QueryRunner().query(conn, sql, resultHandler);

		} catch (SQLException e) {
			throw new UnexpectedException(e.getMessage());
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return orden;
	}

	// Devuelve una lista de todas las órdenes
	public static List<Orden> getOrdenes() throws UnexpectedException {
		Connection conn = null;
		List<Orden> ordenes;

		try {
			conn = DriverManager.getConnection(URL);
			BeanListHandler<Orden> beanListHandler = new BeanListHandler<>(Orden.class);

			String sql = "SELECT * FROM OrdenTrabajo";
			ordenes = new QueryRunner().query(conn, sql, beanListHandler);

		} catch (SQLException e) {
			throw new UnexpectedException(e.getMessage());
			
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return ordenes;
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
	
//	public static List<Producto> getProductosPedido(int idPedido) throws UnexpectedException {
//		Connection conn = null;
//		List<Producto> productos = new ArrayList<Producto>();
//		List<IntegerWrapper> idsProductos;
//
//		try {
//			conn = DriverManager.getConnection(URL);
//			
//			// Sacar IDS de productos
//			BeanListHandler<IntegerWrapper> beanListHandler = new BeanListHandler<>(IntegerWrapper.class);
//			String sql = String.format("SELECT IdProducto, Unidades FROM ProductoPedido WHERE IdPedido = %d ORDER BY IdProducto DESC;", idPedido);
//			idsProductos = new QueryRunner().query(conn, sql, beanListHandler);
//			
//			// Mapear los IDS de producto a objetos producto
//			for(IntegerWrapper iw : idsProductos) {
//				int value = iw.getIdProducto(); // Sacar el valor encapsulado
//				Producto tempProduct = getProducto(value);
//				tempProduct.setUnidades(iw.getUnidades());
//				productos.add(tempProduct);
//			}
//			
//		} catch (SQLException e) {
//			throw new UnexpectedException(e.getMessage());
//			
//		} finally {
//			DbUtils.closeQuietly(conn);
//		}
//		
//		return productos;
//	}
//
//	public static List<Orden> getOrdenesValidas() throws UnexpectedException {
//		Connection conn = null;
//		List<Orden> ordenes;
//
//		try {
//			conn = DriverManager.getConnection(URL);
//			BeanListHandler<Orden> beanListHandler = new BeanListHandler<>(Orden.class);
//
//			String sql = "SELECT * FROM OrdenTrabajo WHERE Valido = 1;";
//			ordenes = new QueryRunner().query(conn, sql, beanListHandler);
//
//		} catch (SQLException e) {
//			throw new UnexpectedException(e.getMessage());
//		} finally {
//			DbUtils.closeQuietly(conn);
//		}
//
//		return ordenes;
//	}
	
	public static void setOrderValid(int IdOrden) throws UnexpectedException {
		Connection conn = null;
		
		String sqlOrden = String.format("UPDATE OrdenTrabajo SET Valido = 1 WHERE IdOrden = %d;", IdOrden);
		try {
			conn = DriverManager.getConnection(URL);
			new QueryRunner().update(conn, sqlOrden);
			
		} catch (SQLException e) {
			throw new UnexpectedException("Error",e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	// Devuelve los productos asociados a una orden concreta
	public static List<Producto> getProductos(int id_orden) throws UnexpectedException {
		Connection conn = null;
		List<Producto> productos;

		try {
			conn = DriverManager.getConnection(URL);
			BeanListHandler<Producto> beanListHandler = new BeanListHandler<>(Producto.class);

			String sql = String.format(
					"SELECT * FROM Producto WHERE IdProducto IN (SELECT IdProducto FROM ProductoOrden WHERE IdOrden=%d)",
					id_orden);
			productos = new QueryRunner().query(conn, sql, beanListHandler);

		} catch (SQLException e) {
			throw new UnexpectedException("Error",e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return productos;
	}
	
	public static List<Producto> getProductos() throws UnexpectedException, SQLException {
		Connection conn = Jdbc.getConnection();
		List<Producto> productos;
		
		try {
			conn = Jdbc.getConnection();
			BeanListHandler<Producto> beanListHandler = new BeanListHandler<Producto>(Producto.class);

			String sql = "SELECT * FROM PRODUCTO";
			productos = new QueryRunner().query(conn, sql, beanListHandler);

		} catch (SQLException e) {
			throw new UnexpectedException(e.getMessage());
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return productos;
	}

	public static void createOrder(Orden orden, List<Producto> orderProducts, List<Producto> model) throws UnexpectedException {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(URL);
			
			// Crear orden de trabajo, necesaria para poder crear los pedidos
			String sqlOrden = String.format(Locale.US, "INSERT INTO OrdenTrabajo(UnidadesTotales,PrecioTotal,Fecha,Valido) VALUES (%d, %f,datetime('now'), false);",
					orden.getUnidadesTotales(),orden.getPrecioTotal());
			new QueryRunner().update(conn, sqlOrden);
			
			// Buscar el id de la última orden creada
			String sqlBuscaOrden = String.format("SELECT IdOrden FROM OrdenTrabajo ORDER BY Fecha DESC LIMIT 1;");
			Integer idUltimaOrden = new QueryRunner().query(conn, sqlBuscaOrden, new ScalarHandler<Integer>());
			
			// Crear un pedido por cada orden (de momento)
			// En el futuro se podrán crear más de una orden por pedido (aka: fraccionar)
			System.out.println("CREANDO PEDIDO...");
			String sqlPedido = String.format("INSERT INTO Pedido(IdOrden,UnidadesTotales, PrecioTotal, Fecha) VALUES (%d, %d, %s, datetime('now'));",
					idUltimaOrden, BusinessLogicUtil.calculateUnits(model), BusinessLogicUtil.calculateSubtotal(model));
			new QueryRunner().update(conn, sqlPedido);
			
			// Buscar el id del último pedido creado para emplearlo abajo
			String sqlBuscaPedido = String.format("SELECT IdPedido FROM Pedido ORDER BY Fecha DESC LIMIT 1;");
			Integer idUltimoPedido = new QueryRunner().query(conn, sqlBuscaPedido, new ScalarHandler<Integer>());
			
			// Meter productos en el pedido
			for(Producto p : orderProducts) {
				String sqlProductoPedido = String.format("INSERT INTO ProductoPedido(IdProducto, IdPedido, Unidades) VALUES ( %d, %d, %d );",
						p.getIdProducto(), idUltimoPedido, BusinessLogicUtil.calculateUnits(model));
				new QueryRunner().update(conn, sqlProductoPedido);
			}

		} catch (SQLException e) {
			throw new UnexpectedException("Error",e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
//	@SuppressWarnings("finally")
//	public static boolean checkCliente(String usuario, String constraseña) throws UnexpectedException {
//		Connection conn = null;
//		Usuario user;
//
//		try {
//			conn = DriverManager.getConnection(URL);
//			ResultSetHandler<Usuario> resultHandler = new BeanHandler<Usuario>(Usuario.class);
//
//			String sqlOrden = String.format(Locale.US, "Select * from Cliente c where c.Correo = %s and c.ClaveAcceso = %s;",
//					usuario,constraseña);
//			user = new QueryRunner().query(conn, sqlOrden, resultHandler);
//			if (usuario != null) {
//				return true;
//			}
//
//		} catch (SQLException e) {
//			throw new UnexpectedException(e.getMessage());
//		} finally {
//			DbUtils.closeQuietly(conn);
//		}
//
//		return false;
//	}
	
	public static List<Producto> getProductosLowStock() throws UnexpectedException {
		Connection conn = null;
		List<Producto> productos;
		
		try {
			conn = DriverManager.getConnection(URL);
			BeanListHandler<Producto> beanListHandler = new BeanListHandler<Producto>(Producto.class);

			String sql = "SELECT * FROM Producto WHERE Unidades < StockMinimo";
			productos = new QueryRunner().query(conn, sql, beanListHandler);

		} catch (SQLException e) {
			throw new UnexpectedException(e.getMessage());
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return productos;
	}
	
//	public static List<Empaquetado> getEmpaquetados() throws UnexpectedException {
//		Connection conn = null;
//		List<Empaquetado> empaquetados;
//		
//		try {
//			conn = DriverManager.getConnection(URL);
//			BeanListHandler<Empaquetado> beanListHandler = new BeanListHandler<Empaquetado>(Empaquetado.class);
//
//			String sql = "SELECT * FROM Empaquetado;";
//			empaquetados = new QueryRunner().query(conn, sql, beanListHandler);
//
//		} catch (SQLException e) {
//			throw new UnexpectedException(e.getMessage());
//		} finally {
//			DbUtils.closeQuietly(conn);
//		}
//		return empaquetados;
//	}
}
