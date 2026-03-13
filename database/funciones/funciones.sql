--1
CREATE OR REPLACE FUNCTION fn_calcular_monto_ejecutado(
p_id_subcategoria INT, 
p_id_presupuesto INT,
p_anio INT, 
p_mes INT
)
RETURNS numeric(15,2)
BEGIN 
declare v_monto_ejecutado numeric(15,2); 
SELECT ISNULL(sum(t.monto),0) into v_monto_ejecutado
FROM dba.transaccion t 
INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
WHERE pd.id_presupuesto = p_id_presupuesto
AND pd.id_subcategoria = p_id_subcategoria
AND t.anio = p_anio 
AND t.mes = p_mes 
AND t.tipo_transaccion = 'gasto';
return v_monto_ejecutado;
END; 

--2
CREATE OR REPLACE FUNCTION fn_calcular_porcentaje_ejecutado(
p_id_subcategoria INT, 
p_id_presupuesto INT, 
p_anio INT, 
p_mes INT
)
RETURNS NUMERIC(15,2) 
BEGIN
DECLARE v_porcentaje numeric(15,2);
DECLARE v_monto_ejecutado NUMERIC(15,2);
DECLARE v_monto_mensual_presupuestado NUMERIC(15,2);

SET v_monto_ejecutado = dba.fn_calcular_monto_ejecutado(p_id_subcategoria, p_id_presupuesto, p_anio, p_mes);

SELECT ISNULL(monto_mensual_asignado,0) INTO v_monto_mensual_presupuestado
FROM dba.presupuesto_detalle 
WHERE id_presupuesto = p_id_presupuesto
AND id_subcategoria = p_id_subcategoria;

IF(v_monto_mensual_presupuestado > 0) THEN 
SET v_porcentaje = (v_monto_ejecutado/v_monto_mensual_presupuestado)*100; 
ELSE 
SET v_porcentaje = 0; 
END IF; 

return v_porcentaje; 
END; 


--3 
CREATE OR REPLACE FUNCTION fn_obtener_balance_subcategoria(
p_id_presupuesto INT,
p_id_subcategoria INT, 
p_anio INT, 
p_mes INT
)
RETURNS NUMERIC(15,2)
BEGIN

DECLARE v_balance NUMERIC(15,2);
DECLARE v_monto_presupuestado NUMERIC(15,2); 
DECLARE v_ejecutado NUMERIC(15,2); 

SELECT ISNULL(monto_mensual_asignado,0) INTO v_monto_presupuestado
FROM dba.presupuesto_detalle 
WHERE id_presupuesto = p_id_presupuesto
AND id_subcategoria = p_id_subcategoria;

SET v_ejecutado = fn_calcular_monto_ejecutado(
p_id_subcategoria, p_id_presupuesto, p_anio, p_mes);

SET v_balance = v_monto_presupuestado - v_ejecutado;

RETURN v_balance;
END; 