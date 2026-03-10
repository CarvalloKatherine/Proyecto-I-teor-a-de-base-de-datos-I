/*
CONSULTAR!
sp_crear_presupuesto_completo(p_id_usuario, p_nombre, p_descripcion, p_periodo_inicio, p_periodo_fin, p_lista_subcategorias_json, p_creado_por)
*/

/*CREATE OR REPLACE PROCEDURE sp_registrar_transaccion_completa(
p_id_usuario, 
p_id_detalle, 
p_anio, 
p_mes, 
p_id_subcategoria, 
p_tipo, 
p_descripcion, 
p_monto, 
p_fecha, 
p_metodo_pago, 
p_creado_por)
BEGIN 

END; */

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

    
    SELECT o.nombre_obligacion, o.monto, o.dia AS vencimiento from 
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