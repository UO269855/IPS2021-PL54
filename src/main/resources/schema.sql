--Primero se deben borrar todas las tablas (de detalle a maestro) y lugo anyadirlas (de maestro a detalle)
--(en este caso en cada una de las aplicaciones (tkrun y descuento) se usa solo una tabla, por lo que no hace falta)

--Para giis.demo.tkrun:
drop table Producto;
drop table ProductoPedido;
drop table Pedido;
drop table OrdenTrabajo;



		
-- Producto
CREATE TABLE IF NOT EXISTS Producto (
	IdProducto INTEGER PRIMARY KEY AUTOINCREMENT, 
	nombre TEXT NOT NULL,
	precio REAL NOT NULL,
	descripcion TEXT NOT NULL,
	unidades INTEGER,
	check(precio>0));
);




		
create table Pedido (
		IDPedido INTEGER primary key not null,
		IDOrden INTEGER not null,
		precioPedido DOUBLE(6,2) not null;
		fecha date not null,
		FOREIGN KEY (IDOrden) REFERENCES OrdenTrabajo(IDOrden)
);






create table OrdenTrabajo (
		IDOrden INTEGER primary key not null,
		localizacion varchar(32) not null,
		fechaOT date not null
);


create table ProductoPedido(
	IDProductoPedido INTEGER PRIMARY KEY not null,
	IDProducto INTEGER NOT NULL,
	IDPedido INTEGER NOT NULL,
	unidadesPedido INTEGER Not NULL,
	
	FOREIGN KEY (IDProducto) REFERENCES Producto(IDProducto),
	FOREIGN KEY (IDPedido) REFERENCES Pedido(IDPedido)
);