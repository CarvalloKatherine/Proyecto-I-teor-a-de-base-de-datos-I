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
CREATE OR REPLACE PROCEDURE dba.sp_reporte3(
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