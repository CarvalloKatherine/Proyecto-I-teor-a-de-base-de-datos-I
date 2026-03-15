CALL sp_insertar_usuario('clave123','Ana','Maria','Lopez','Martinez','ana@email.com',25000,'system');

CALL sp_insertar_categoria('Salario','Ingresos laborales','ingreso','money','#4CAF50','1','system');
CALL sp_insertar_categoria('Alimentacion','Gastos de comida','gasto','restaurant','#FF9800','2','system');
CALL sp_insertar_categoria('Transporte','Gastos de transporte','gasto','car','#03A9F4','3','system');
CALL sp_insertar_categoria('Servicios','Servicios del hogar','gasto','home','#9C27B0','4','system');
CALL sp_insertar_categoria('Ahorro','Ahorro personal','ahorro','savings','#009688','5','system');

CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Salario'),'Salario Mensual','Pago mensual','system',0);
CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Alimentacion'),'Supermercado','Compra supermercado','system',0);
CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Alimentacion'),'Restaurantes','Comida fuera','system',0);
CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Transporte'),'Gasolina','Combustible','system',0);
CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Transporte'),'Taxi','Servicios taxi','system',0);
CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Servicios'),'Electricidad','Pago energia','system',0);
CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Servicios'),'Internet','Servicio internet','system',0);
CALL sp_insertar_subcategoria((SELECT id_categoria FROM Categoria WHERE nombre_categoria='Ahorro'),'Ahorro Banco','Deposito ahorro','system',0);

CALL sp_insertar_presupuesto(1,'Presupuesto Enero-Febrero',2025,1,2025,2,25000,18000,2000,'system');

CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Salario Mensual'),25000,NULL,'system');
CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Supermercado'),4000,NULL,'system');
CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Restaurantes'),2000,NULL,'system');
CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Gasolina'),2500,NULL,'system');
CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Taxi'),800,NULL,'system');
CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Electricidad'),900,NULL,'system');
CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Internet'),700,NULL,'system');
CALL sp_insertar_presupuesto_detalle(1,(SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Ahorro Banco'),2000,NULL,'system');

CALL sp_insertar_obligacion((SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Electricidad'),'Pago Energia','Factura energia mensual',900,10,'2025-01-01',NULL,'system');
CALL sp_insertar_obligacion((SELECT id_subcategoria FROM SubCategoria WHERE nombre_subcategoria='Internet'),'Pago Internet','Servicio internet mensual',700,15,'2025-01-01',NULL,'system');

CALL sp_insertar_transaccion(1,2025,1,'ingreso','Salario Enero',25000,'2025-01-02','transferencia',1001,NULL,'system',NULL);
CALL sp_insertar_transaccion(2,2025,1,'gasto','Supermercado quincena',1800,'2025-01-05','tarjeta',2001,NULL,'system',NULL);
CALL sp_insertar_transaccion(3,2025,1,'gasto','Cena restaurante',450,'2025-01-08','tarjeta',2002,NULL,'system',NULL);
CALL sp_insertar_transaccion(6,2025,1,'gasto','Pago energia enero',900,'2025-01-10','transferencia',2003,NULL,'system',(SELECT id_obligacion FROM obligacion_fija WHERE nombre_obligacion='Pago Energia'));
CALL sp_insertar_transaccion(7,2025,1,'gasto','Pago internet enero',700,'2025-01-15','transferencia',2004,NULL,'system',(SELECT id_obligacion FROM obligacion_fija WHERE nombre_obligacion='Pago Internet'));
CALL sp_insertar_transaccion(4,2025,1,'gasto','Gasolina semana',600,'2025-01-18','efectivo',2005,NULL,'system',NULL);
CALL sp_insertar_transaccion(5,2025,1,'gasto','Taxi trabajo',150,'2025-01-20','efectivo',2006,NULL,'system',NULL);
CALL sp_insertar_transaccion(8,2025,1,'ahorro','Deposito ahorro enero',1500,'2025-01-25','transferencia',2007,NULL,'system',NULL);

CALL sp_insertar_transaccion(1,2025,2,'ingreso','Salario Febrero',25000,'2025-02-02','transferencia',3001,NULL,'system',NULL);
CALL sp_insertar_transaccion(2,2025,2,'gasto','Supermercado quincena',2100,'2025-02-06','tarjeta',3002,NULL,'system',NULL);
CALL sp_insertar_transaccion(3,2025,2,'gasto','Restaurante fin semana',520,'2025-02-09','tarjeta',3003,NULL,'system',NULL);
CALL sp_insertar_transaccion(6,2025,2,'gasto','Pago energia febrero',950,'2025-02-10','transferencia',3004,NULL,'system',(SELECT id_obligacion FROM obligacion_fija WHERE nombre_obligacion='Pago Energia'));
CALL sp_insertar_transaccion(7,2025,2,'gasto','Pago internet febrero',700,'2025-02-15','transferencia',3005,NULL,'system',(SELECT id_obligacion FROM obligacion_fija WHERE nombre_obligacion='Pago Internet'));
CALL sp_insertar_transaccion(4,2025,2,'gasto','Gasolina semana',650,'2025-02-18','efectivo',3006,NULL,'system',NULL);
CALL sp_insertar_transaccion(5,2025,2,'gasto','Taxi trabajo',180,'2025-02-22','efectivo',3007,NULL,'system',NULL);
CALL sp_insertar_transaccion(8,2025,2,'ahorro','Deposito ahorro febrero',1700,'2025-02-25','transferencia',3008,NULL,'system',NULL);
