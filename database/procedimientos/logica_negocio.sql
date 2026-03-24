/*CREATE OR REPLACE PROCEDURE sp_crear_presupuesto_completo(
p_id_usuario INT, 
p_nombre VARCHAR(100), 
p_anio_inicio INT,
p_mes_inicio INT,
p_anio_fin INT,
p_mes_fin INT,
p_ingreso_total NUMERIC(15,2),
p_gasto_total NUMERIC(15,2),
p_ahorro_total NUMERIC(15,2),
p_lista_subcategorias_json LONG VARCHAR, 
p_creado_por VARCHAR(200)
)
BEGIN 
DECLARE v_id_presupuesto int; 



COMMIT; 
END;*/

------------------
CREATE OR REPLACE PROCEDURE sp_registrar_transaccion_completa(
p_id_detalle int, 
p_anio int, 
p_mes int, 
p_tipo varchar(30), 
p_descripcion varchar (100), 
p_monto numeric(15,2), 
p_fecha date, 
p_metodo_pago varchar(50), 
p_num_factura int, 
p_observaciones varchar(100), 
p_creado_por varchar(200),
p_id_obligacion INT
)
BEGIN 
DECLARE v_fecha_inicio DATE;
DECLARE v_fecha_fin DATE;

IF (p_mes < 1 OR p_mes > 12) THEN
RAISERROR 99001 'El mes debe estar entre 1 y 12.';
RETURN;
END IF;



CALL dba.sp_insertar_transaccion(
p_id_detalle, 
p_anio, p_mes, p_tipo, 
p_descripcion, 
p_monto, p_fecha, 
p_metodo_pago, p_num_factura,p_observaciones, 
p_creado_por, p_id_obligacion);

END; 
---------
CREATE OR REPLACE PROCEDURE sp_procesar_obligaciones_mes(
p_id_usuario INT, 
p_anio INT, 
p_mes INT, 
p_id_presupuesto INT
)
BEGIN
    /*
    Revisa todas las obligaciones activas del usuario y genera alertas para las que
    vencen en el mes especificado, 
    pasos: 
    1. revisar que esten activas (vigente y la fecha durante el presupuesto)
    2. revisar si tienen una transaccion asociada 
    3. generar una alerta a las que estan activas y no tienen transaccion asociada en el mes actual
    */

    
    SELECT o.nombre_obligacion, o.monto_fijo_mensual, o.dia AS vencimiento from 
    dba.obligacion_fija o 
    INNER JOIN dba.SubCategoria sc ON o.id_subcategoria = sc.id_subcategoria
    INNER JOIN dba.presupuesto_detalle pd ON sc.id_subcategoria = pd.id_subcategoria
    INNER JOIN dba.Presupuesto p ON pd.id_presupuesto = p.id_presupuesto
    INNER JOIN dba.Usuario u ON p.id_usuario = u.id_usuario
    WHERE u.id_usuario = p_id_usuario
    --ver si esta activa 
    AND o.vigente = 1
    AND (o.fecha_fin IS NULL or 
    YEAR(o.fecha_fin) > p_anio or 
    (YEAR(o.fecha_fin) = p_anio 
    and MONTH(o.fecha_fin) >= p_mes))
    AND (YEAR(o.fecha_inicio) < p_anio OR 
    (YEAR(o.fecha_inicio) = p_anio 
    and MONTH(o.fecha_inicio) <= p_mes))
    --si no tiene transaccion asociada
    AND NOT EXISTS(SELECT 1 FROM dba.obligacion_transaccion ot
    INNER JOIN dba.transaccion t ON ot.id_transaccion = t.id_transaccion
    WHERE ot.id_obligacion = o.id_obligacion
    AND t.anio = p_anio
    AND t.mes = p_mes
    )
    --muestra las que estan pendientes de pago en el mes actual

END; 

-------------

CREATE OR REPLACE PROCEDURE sp_calcular_balance_mensual(
p_id_usuario INT, 
p_id_presupuesto INT, 
p_anio INT, 
p_mes INT, 
OUT p_total_ingresos NUMERIC(15,2), 
OUT p_total_gastos NUMERIC(15,2), 
OUT p_total_ahorros NUMERIC(15,2), 
OUT p_balance_final NUMERIC(15,2)
)
BEGIN 
    SELECT ISNULL(sum(t.monto),0) into p_total_ingresos
    FROM dba.transaccion t 
    INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
    WHERE pd.id_presupuesto = p_id_presupuesto
    AND t.anio = p_anio 
    AND t.mes = p_mes 
    AND tipo_transaccion = 'ingreso';

    SELECT ISNULL(sum(t.monto),0) into p_total_gastos
    FROM dba.transaccion t 
    INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
    WHERE pd.id_presupuesto = p_id_presupuesto
    AND t.anio = p_anio 
    AND t.mes = p_mes 
    AND tipo_transaccion = 'gasto';

    SELECT ISNULL(sum(t.monto),0) into p_total_ahorros
    FROM dba.transaccion t 
    INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
    WHERE pd.id_presupuesto = p_id_presupuesto
    AND t.anio = p_anio 
    AND t.mes = p_mes 
    AND tipo_transaccion = 'ahorro';

    SET p_balance_final = p_total_ingresos - p_total_ahorros-p_total_gastos ; 

    --prueba
    SELECT p_total_ingresos AS ingresos,
           p_total_gastos   AS gastos,
           p_total_ahorros  AS ahorros,
           p_balance_final  AS balance;

END; 
------------
CREATE OR REPLACE PROCEDURE sp_calcular_monto_ejecutado_mes(
p_id_subcategoria INT, 
p_id_presupuesto INT, 
p_anio INT, 
p_mes INT, 
OUT p_monto_ejecutado NUMERIC (15,2)
)
BEGIN 
    SELECT ISNULL(sum(t.monto),0) into p_monto_ejecutado
    FROM dba.transaccion t 
    INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
    WHERE pd.id_presupuesto = p_id_presupuesto
    AND pd.id_subcategoria = p_id_subcategoria
    AND t.anio = p_anio 
    AND t.mes = p_mes 
    AND t.tipo_transaccion = 'gasto';
END; 
--------------
CREATE OR REPLACE PROCEDURE sp_calcular_porcentaje_ejecucion_mes(
p_id_subcategoria int, 
p_id_presupuesto int, 
p_anio int, 
p_mes int, 
OUT p_porcentaje NUMERIC(15,2)
)
BEGIN 
DECLARE v_monto_ejecutado NUMERIC(15,2);
DECLARE v_monto_mensual_presupuestado NUMERIC(15,2);

CALL sp_calcular_monto_ejecutado_mes(
p_id_subcategoria, 
p_id_presupuesto, 
p_anio, 
p_mes,
v_monto_ejecutado
);

SELECT ISNULL(monto_mensual_asignado,0) INTO v_monto_mensual_presupuestado
FROM dba.presupuesto_detalle 
WHERE id_presupuesto = p_id_presupuesto
AND id_subcategoria = p_id_subcategoria;

IF(v_monto_mensual_presupuestado > 0) THEN 
SET p_porcentaje = (v_monto_ejecutado/v_monto_mensual_presupuestado)*100; 
ELSE 
SET p_porcentaje = 0; 
END IF; 

END; 

---------

CREATE OR REPLACE PROCEDURE sp_cerrar_presupuesto(
p_id_presupuesto INT, 
p_modificado_por VARCHAR(200),
p_id int
)
BEGIN 
    DECLARE v_fecha_fin date; 
    DECLARE v_ingresos NUMERIC(15,2);
    DECLARE v_gastos NUMERIC(15,2);
    DECLARE v_ahorros NUMERIC(15,2);
    DECLARE v_balance NUMERIC(15,2);

    select fecha_fin into v_fecha_fin 
    from dba.presupuesto WHERE id_presupuesto = p_id_presupuesto 
    AND id_usuario = p_id_usuario;

    if(CURRENT DATE < v_fecha_fin)THEN 
    return; 
    END IF; 
    

    CALL sp_calcular_balance_mensual(p_id, p_id_presupuesto,YEAR(v_fecha_fin), MONTH(v_fecha_fin),v_ingresos, 
    v_gastos, v_ahorros, v_balance);

    UPDATE dba.Presupuesto SET estado_presupuesto = 'cerrado',
    modificado_por = p_modificado_por,
    modificado_en = CURRENT TIMESTAMP
    WHERE id_presupuesto = p_id_presupuesto
    AND id_usuario = p_id;
    COMMIT; 
END; 

---
CREATE OR REPLACE PROCEDURE sp_obtener_resumen_categoria_mes(p_id_categoria INT, 
p_id_presupuesto INT, 
p_anio INT, 
p_mes INT, 
OUT p_monto_presupuestado NUMERIC(15,2), 
OUT p_monto_ejecutado NUMERIC(15,2), 
OUT p_porcentaje NUMERIC(15,2)
)
BEGIN
    SELECT ISNULL(SUM(pd.monto_mensual_asignado), 0) INTO p_monto_presupuestado
    FROM dba.presupuesto_detalle pd
    INNER JOIN dba.SubCategoria sc ON pd.id_subcategoria = sc.id_subcategoria
    WHERE pd.id_presupuesto = p_id_presupuesto
    AND sc.id_categoria = p_id_categoria;

    SELECT ISNULL(SUM(t.monto), 0) INTO p_monto_ejecutado
    FROM dba.transaccion t
    INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
    INNER JOIN dba.SubCategoria sc ON pd.id_subcategoria = sc.id_subcategoria
    WHERE pd.id_presupuesto = p_id_presupuesto
    AND sc.id_categoria = p_id_categoria
    AND t.anio = p_anio 
    AND t.mes = p_mes 
    AND t.tipo_transaccion = 'gasto';

    IF p_monto_presupuestado > 0 THEN
    SET p_porcentaje = (p_monto_ejecutado / p_monto_presupuestado) *100;
    ELSE
    SET p_porcentaje = 0;
    END IF;  
END; 