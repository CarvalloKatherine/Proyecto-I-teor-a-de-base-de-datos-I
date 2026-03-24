CREATE OR REPLACE PROCEDURE sp_validar_login(
    p_email varchar(200),
    p_clave varchar(30)
)
BEGIN
    SELECT id_usuario, primer_nombre, primer_apellido, correo_electronico, salario_mensual_base
    FROM dba.Usuario
    WHERE correo_electronico = p_email 
    AND clave = p_clave
    AND estado = 1;
END;
-------------
CREATE OR REPLACE FUNCTION fn_obtener_id_detalle(
    p_id_presupuesto  int,
    p_id_subcategoria INT
) RETURNS INT
BEGIN
    DECLARE v_id int;
    SELECT id_presupuesto_detalle INTO v_id
    FROM dba.presupuesto_detalle
    WHERE id_presupuesto = p_id_presupuesto
    AND id_subcategoria = p_id_subcategoria;
    RETURN v_id;
END;
----------
CREATE OR REPLACE FUNCTION fn_ultimo_id_obligacion()
RETURNS INT
BEGIN
    RETURN @@IDENTITY;
END;
COMMIT;

------
--REPORTE 3 
CREATE OR REPLACE PROCEDURE sp_reporte3(
    p_id_presupuesto INT,
    p_anio INT,
    p_mes INT
)
BEGIN
    SELECT 
        c.nombre_categoria,
        s.nombre_subcategoria,
        pd.monto_mensual_asignado AS presupuestado,
        ISNULL(SUM(t.monto), 0) AS ejecutado,
        (ISNULL(SUM(t.monto), 0) / pd.monto_mensual_asignado) * 100 AS porcentaje
    FROM dba.presupuesto_detalle pd
    INNER JOIN dba.subcategoria s ON pd.id_subcategoria = s.id_subcategoria
    INNER JOIN dba.categoria c ON s.id_categoria = c.id_categoria
    LEFT JOIN dba.transaccion t ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
        AND t.anio = p_anio
        AND t.mes = p_mes
    WHERE pd.id_presupuesto = p_id_presupuesto
    GROUP BY c.nombre_categoria, s.nombre_subcategoria, pd.monto_mensual_asignado
END;

-----------------------
--REPORTE2
CREATE OR REPLACE PROCEDURE sp_reporte2(
    p_id_presupuesto INT,
    p_anio INT,
    p_mes INT
)
BEGIN 
    SELECT c.nombre_categoria, 
    ISNULL(SUM(t.monto), 0) AS total_gastado,
    COUNT(t.id_transaccion) as transacciones,
    FROM dba.presupuesto_detalle pd
    INNER JOIN dba.subcategoria s ON pd.id_subcategoria = s.id_subcategoria
    INNER JOIN dba.categoria c ON s.id_categoria = c.id_categoria
    LEFT JOIN dba.transaccion t ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
    AND t.anio = p_anio
    AND t.mes = p_mes
    AND t.tipo_transaccion = 'gasto'
    WHERE pd.id_presupuesto = p_id_presupuesto
    AND c.tipo_categoria = 'gasto'
    GROUP BY c.nombre_categoria
END; 

------REPORTE 4
CREATE OR REPLACE PROCEDURE sp_reporte4(
    p_id_presupuesto int
)
BEGIN 
    SELECT 
        c.nombre_categoria,
        t.anio,
        t.mes,
        ISNULL(SUM(t.monto), 0) AS total_gastado
    FROM dba.transaccion t
    INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
    INNER JOIN dba.subcategoria s ON pd.id_subcategoria = s.id_subcategoria
    INNER JOIN dba.categoria c ON s.id_categoria = c.id_categoria
    WHERE pd.id_presupuesto = p_id_presupuesto
      AND t.tipo_transaccion = 'gasto'
    GROUP BY c.nombre_categoria, t.anio, t.mes
END;

-------------
CREATE OR REPLACE PROCEDURE dba.sp_reporte5(
    p_id_usuario INT,
    p_anio INT,
    p_mes INT
)
BEGIN
    SELECT 
        o.nombre_obligacion,
        c.nombre_categoria,
        o.monto_fijo_mensual,
        o.dia AS dia_vencimiento,
        CASE WHEN ot.id_transaccion IS NOT NULL THEN 'Pagada' ELSE 'Pendiente' END AS estado_pago,
        MAX(t.fecha) AS ultimo_pago
    FROM dba.obligacion_fija o
    INNER JOIN dba.subcategoria s ON o.id_subcategoria = s.id_subcategoria
    INNER JOIN dba.categoria c ON s.id_categoria = c.id_categoria
    INNER JOIN dba.presupuesto_detalle pd ON pd.id_subcategoria = s.id_subcategoria
    INNER JOIN dba.presupuesto p ON p.id_presupuesto = pd.id_presupuesto
        AND p.id_usuario = p_id_usuario
    LEFT JOIN dba.obligacion_transaccion ot ON ot.id_obligacion = o.id_obligacion
    LEFT JOIN dba.transaccion t ON t.id_transaccion = ot.id_transaccion
        AND t.anio = p_anio
        AND t.mes = p_mes
    WHERE o.vigente = 1
    GROUP BY 
        o.nombre_obligacion, 
        c.nombre_categoria, 
        o.monto_fijo_mensual,
        o.dia,
        ot.id_transaccion;
END;