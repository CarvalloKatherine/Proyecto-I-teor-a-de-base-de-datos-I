CREATE OR REPLACE PROCEDURE sp_insertar_transaccion(
p_id_detalle INT, 
p_anio INT, 
p_mes INT, 
p_tipo VARCHAR (30), 
p_descripcion VARCHAR(100), 
p_monto NUMERIC (15,2), 
p_fecha Date, 
p_metodo_pago varchar(50), 
p_num_factura int, 
p_observaciones varchar(100), 
p_creado_por varchar(200),
p_id_obligacion INT
)
BEGIN 
    DECLARE v_id_transaccion INT;

    IF NOT EXISTS (SELECT 1 FROM dba.presupuesto_detalle WHERE id_presupuesto_detalle = p_id_detalle) THEN
        RAISERROR 99045 'El detalle de presupuesto no existe.';
        RETURN;
    END IF;

    INSERT INTO dba.transaccion (
        id_presupuesto_detalle, 
        anio, 
        mes, 
        tipo_transaccion, 
        descripcion, 
        monto, 
        fecha, 
        metodo_pago, 
        num_factura, 
        observaciones, 
        fecha_hora_registro, 
        creado_por
    )
    VALUES (
        p_id_detalle, 
        p_anio, 
        p_mes, 
        p_tipo, 
        p_descripcion, 
        p_monto, 
        p_fecha, 
        p_metodo_pago, 
        p_num_factura, 
        p_observaciones, 
        CURRENT TIMESTAMP, 
        p_creado_por
    );

    SET  v_id_transaccion = @@identity; 

    IF p_id_obligacion IS NOT NULL THEN
        
    IF EXISTS (SELECT 1 FROM dba.obligacion_fija WHERE id_obligacion = p_id_obligacion) THEN
    INSERT INTO dba.obligacion_transaccion (id_transaccion, id_obligacion)
    VALUES (v_id_transaccion, p_id_obligacion);
        ELSE
    RAISERROR 99046 'La obligación fija especificada no existe.';
    ROLLBACK; 
    RETURN;
        END IF;
        
    END IF;


    COMMIT; 
END; 

CREATE OR REPLACE PROCEDURE sp_actualizar_transaccion(
p_id_transaccion INT, 
p_anio INT, 
p_mes INT, 
p_descripcion VARCHAR(100), 
p_monto NUMERIC(15,2), 
p_fecha DATE, 
p_metodo_pago VARCHAR(50), 
p_num_factura INT, 
p_observaciones VARCHAR(100), 
p_modificado_por VARCHAR(200)
)
BEGIN 
IF NOT EXISTS (SELECT 1 FROM dba.transaccion t
INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
INNER JOIN dba.Presupuesto p ON pd.id_presupuesto = p.id_presupuesto
WHERE t.id_transaccion = p_id_transaccion 
AND p.estado_presupuesto != 'cerrado') 
THEN
RAISERROR 99050 'La transacción no existe o pertenece a un presupuesto Cerrado no se puede editar.';
RETURN;
END IF;

IF (p_monto <= 0) THEN
RAISERROR 99051 'el monto de la transacción debe ser mayor a cero.';
RETURN;
END IF;

UPDATE dba.transaccion SET 
anio = p_anio,
mes = p_mes,
descripcion = p_descripcion,
monto = p_monto,
fecha = p_fecha,
metodo_pago = p_metodo_pago,
num_factura = p_num_factura,
observaciones = p_observaciones,
modificado_por = p_modificado_por,
modificado_en = CURRENT TIMESTAMP
WHERE id_transaccion = p_id_transaccion;

COMMIT; 
END; 
------------------

CREATE OR REPLACE PROCEDURE sp_eliminar_transaccion(p_id_transaccion INT)
BEGIN

IF NOT EXISTS (SELECT 1 FROM dba.transaccion t
INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
INNER JOIN dba.Presupuesto p ON pd.id_presupuesto = p.id_presupuesto
WHERE t.id_transaccion = p_id_transaccion AND 
p.estado_presupuesto != 'cerrado') THEN
RAISERROR 99052 'No se puede eliminar ya que la transacción no existe o el presupuesto ya cerro.';
RETURN;
END IF;

DELETE FROM dba.obligacion_transaccion WHERE id_transaccion = p_id_transaccion;

DELETE FROM dba.transaccion WHERE id_transaccion = p_id_transaccion;

    COMMIT; 
END; 
-----------------------
CREATE OR REPLACE PROCEDURE sp_consultar_transaccion(
p_id_transaccion INT)
BEGIN
IF NOT EXISTS (SELECT 1 FROM dba.transacciones WHERE id_transaccion = p_id_transaccion) THEN
RAISERROR 99035 'La transaccion no existe.';
RETURN;
END IF;

SELECT 
t.id_transaccion,
t.id_presupuesto_detalle,
t.anio,
t.mes,
t.tipo_transaccion,
t.descripcion,
t.monto,
t.fecha,
t.metodo_pago,
t.observaciones,
t.fecha_hora_registro, 
s.nombre_subcategoria, 
c.nombre_categoria,
c.tipo_categoria,
o.nombre_obligacion AS vinculada_a_obligacion
FROM dba.transaccion t
INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
INNER JOIN dba.SubCategoria s ON pd.id_subcategoria = s.id_subcategoria
INNER JOIN dba.Categoria c ON s.id_categoria = c.id_categoria
LEFT JOIN dba.obligacion_transaccion ot ON t.id_transaccion = ot.id_transaccion
LEFT JOIN dba.obligacion_fija o ON ot.id_obligacion = o.id_obligacion
WHERE t.id_transaccion = p_id_transaccion;

END; 
----------------
CREATE OR REPLACE PROCEDURE sp_listar_transacciones_presupuesto(p_id_presupuesto INT, 
p_anio INT, 
p_mes INT, 
p_tipo VARCHAR(30))
BEGIN

SELECT 
t.id_transaccion,
t.fecha,
t.descripcion,
t.monto,
t.tipo_transaccion,
s.nombre_subcategoria,
t.mes AS mes_imputado
FROM dba.transaccion t
INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
INNER JOIN dba.SubCategoria s ON pd.id_subcategoria = s.id_subcategoria
WHERE pd.id_presupuesto = p_id_presupuesto;
    
END; 
