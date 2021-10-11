--Datos para carga inicial de la base de datos

delete from Producto;
insert into Producto(nombre,precio,descripcion) values 
	('Café',5, 'Despierta con energía'),
	('Azucar',2.80,'Endulza tu día a día'),
	('Lejía', 1.75,'La limpieza comienza aquí' ),
	('Patatas fritas', 1.85, 'El mejor aperitivo');