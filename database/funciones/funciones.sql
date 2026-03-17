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

--4
CREATE OR REPLACE FUNCTION fn_obtener_total_categoria_mes(
p_id_categoria INT, 
p_id_presupuesto INT, 
p_anio INT, 
p_mes INT)
RETURNS NUMERIC(15,2)
BEGIN
    DECLARE v_total_presupuestado NUMERIC(15,2);
    SELECT ISNULL(sum(monto_mensual_asignado),0) into v_total_presupuestado
    FROM dba.presupuesto_detalle p INNER JOIN SubCategoria cs 
    ON p.id_subcategoria = cs.id_subcategoria 
    Where id_categoria = p_id_categoria
    and pd.id_presupuesto = p_id_presupuesto; 

    return v_total_presupuestado; 
END; 

--5
CREATE OR REPLACE FUNCTION fn_obtener_total_ejecutado_categoria_mes(p_id_categoria INT, 
p_anio INT, 
p_mes INT
)
RETURNS NUMERIC(15,2)
BEGIN 
DECLARE v_total_ejecutado NUMERIC(15,2); 

select isnull(sum(t.monto),0) into v_totoal_ejecutado
from dba.transaccion t
INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
INNER JOIN dba.subcategoria s ON pd.id_subcategoria = s.id_subcategoria
WHERE s.id_categoria = p_id_categoria
AND pd.id_presupuesto = p_id_presupuesto
AND t.anio = p_anio 
AND t.mes = p_mes 
AND t.tipo_transaccion = 'gasto';

RETURN v_totoal_ejecutado;
END; 

--6
CREATE OR REPLACE FUNCTION fn_dias_hasta_vencimiento(
p_id_obligacion INT)
RETURNS INT
BEGIN 
DECLARE v_vencimiento DATE; 
DECLARE v_dias_restantes int; 

SELECT fecha_vencimiento INTO v_vencimiento 
FROM dba.obligacion_fija
WHERE id_obligacion = p_id_obligacion;

SET v_dias_restantes = Days(v_vencimiento, CURRENT DATE); 

RETURN v_dias_restantes; 
END; 

--7 
CREATE OR REPLACE FUNCTION fn_validar_vigencia_presupuesto(
fecha DATE, 
p_id_presupuesto INT)
RETURNS INT
BEGIN 
DECLARE v_es_valido INT;
DECLARE v_anio_inicio INT;
DECLARE v_mes_inicio INT;
DECLARE v_anio_fin INT;
DECLARE v_mes_fin INT;

SELECT anio_inicio, mes_inicio, anio_fin, mes_fin into 
v_anio_inicio, v_mes_inicio, v_anio_fin, v_mes_fin
FROM dba.Presupuesto 
where id_presupuesto = p_id_presupuesto ;
IF ((YEAR(fecha) > v_anio_inicio OR (YEAR(fecha) = v_anio_inicio AND MONTH(fecha) >= v_mes_inicio))
AND 
(v_anio_fin IS NULL OR (YEAR(fecha) < v_anio_fin OR (YEAR(fecha) = v_anio_fin AND MONTH(fecha) <= v_mes_fin)))) 
THEN SET v_es_valido = 1;
ELSE SET v_es_valido = 0;
END IF;
RETURN v_es_valido;  
END; 

--8
CREATE OR REPLACE FUNCTION fn_obtener_categoria_por_subcategoria(p_id_subcategoria INT)
RETURNS INT
BEGIN 
DECLARE v_id_categoria INT; 

    IF NOT EXISTS (SELECT 1 FROM dba.SubCategoria WHERE id_subcategoria = p_id_subcategoria) THEN
        RAISERROR 99045 'El id de la subcategoria no existe.';
    END IF;

    SELECT id_categoria into v_id_categoria
    FROM dba.SubCategoria 
    WHERE id_subcategoria = p_id_subcategoria; 

    return v_id_categoria; 
END; 

--9 (corregir)
CREATE OR REPLACE FUNCTION fn_calcular_proyeccion_gasto_mensual(p_id_subcategoria int, 
p_anio int, 
p_mes int,
p_dias_totales int
)
returns NUMERIC(15,2)
BEGIN 
DECLARE v_gasto_total NUMERIC(15,2);
DECLARE v_proyeccion NUMERIC(15,2); 
DECLARE v_dias_transcurridos int; 

SET v_dias_transcurridos = DAY(CURRENT DATE);

SELECT SUM(monto) INTO V_gasto_total
FROM dba.transaccion
WHERE id_subcategoria = p_id_subcategoria
AND YEAR(fecha) = p_anio
AND MONTH(fecha) = p_mes;

if(v_gasto_total IS NULL) THEN 
set v_gasto_total = 0; 
END IF; 

if (p_dias_totales > 0)THEN 
SET v_proyeccion = (v_gasto_total/v_dias_transcurridos)*p_dias_totales; 
else SET v_proyeccion = 0; 
end if; 

RETURN v_proyeccion; 
END; 


--10 
CREATE OR REPLACE FUNCTION fn_obtener_promedio_gasto_subcategoria(p_id_usuario int, 
p_id_subcategoria int, 
p_cantidad_meses int)
RETURNS NUMERIC(15,2)
BEGIN
DECLARE v_promedio NUMERIC(15,2); 
DECLARE v_gastado NUMERIC(15,2); 
DECLARE v_anio_inicio int;
DECLARE v_mes_inicio int; 

SET v_mes_inicio = MONTH(DATEADD(month, -p_cantidad_meses, CURRENT DATE));
SET v_anio_inicio = YEAR(DATEADD(month, -p_cantidad_meses, CURRENT DATE));

SELECT ISNULL(sum(t.monto),0)INTO v_gastado FROM dba.transaccion t
INNER JOIN dba.presupuesto_detalle pd ON t.id_presupuesto_detalle = pd.id_presupuesto_detalle
INNER JOIN dba.Presupuesto p ON pd.id_presupuesto = p.id_presupuesto
WHERE p.id_usuario = p_id_usuario
AND pd.id_subcategoria = p_id_subcategoria
AND t.tipo_transaccion = 'gasto'
AND (t.anio > v_anio_inicio OR (t.anio = v_anio_inicio AND t.mes >= v_mes_inicio));

SET v_promedio = v_gastado/p_cantidad_meses; 
RETURN v_promedio; 
END; 


/*
Funciones de date investigadas: 
MONTH(), YEAR(), DAYS(): Extrae la parte especifica de la fecha
DATEADD(datepart, number, date): agrega o resta la cantidad de tiempo en la fecha 
*/