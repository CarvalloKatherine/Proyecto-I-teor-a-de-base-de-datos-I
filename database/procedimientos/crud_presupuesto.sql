CREATE OR REPLACE PROCEDURE dba.sp_insertar_presupuesto(
    p_id_usuario int,
    p_nombre varchar(100),
    p_anio_ini int,
    p_mes_ini int,
    p_anio_fin int,
    p_mes_fin int,
    p_ingreso_total numeric(15,2),
    p_gasto_total numeric(15,2),
    p_ahorro_total numeric(15,2),
    p_creado_por varchar(200)
)
BEGIN
    DECLARE v_existe_activo int;

    --reglas de negocio 
    -- 1. el año final debe ser mayor o igual al año de inicio 
    IF p_anio_fin < p_anio_ini THEN
        RAISERROR 99014 'el año final debe ser mayor o igual al año de inicio';
        RETURN;
    END IF;

    --2. Si el año de fin y final son el mismo, el mes final debe ser mayor o igual al mes de inicio 
    IF p_anio_fin = p_anio_ini AND p_mes_fin < p_mes_ini THEN
        RAISERROR 99015 'si el año es igual, entonces el mes findebe ser mayor o igual al mes de inicio ';
        RETURN;
    END IF;

    -- 3. IDEALMENTE : el ingreso total presupuestado debe ser mayor o igual a la suma de gastos y ahorros presupuestados 
    IF p_ingreso_total < (p_gasto_total + p_ahorro_total) THEN
        RAISERROR 99016 'el ingreso total presupuestado debe ser mayor o igual a la suma de gastos y ahorros presupuestados';
        RETURN;
    END IF;

   --4. validar que solo pueda existir un presupuesto por usuario en un periodo dado 
    IF EXISTS (
        SELECT * FROM dba.Presupuesto 
        WHERE id_usuario = p_id_usuario 
        AND estado_presupuesto = 'Activo'
        AND anio_inicio = p_anio_ini 
        AND mes_inicio = p_mes_ini
    ) THEN
        RAISERROR 99017 'ya existe un presupuesto activo para este periodo.';
        RETURN;
    END IF;

    
    INSERT INTO dba.Presupuesto (
        id_usuario INT, 
        nombre_presupuesto VARCHAR(100), 
        anio_inicio INT, 
        mes_inicio INT, 
        anio_fin INT, 
        mes_fin INT,
        total_ingresos NUMERIC(15,2), 
        total_gastos NUMERIC(15,2), 
        total_ahorro NUMERIC(15,2), 
        estado_presupuesto VARCHAR(30),
        fecha_hora_creacion TIMESTAMP, 
        creado_por VARCHAR(200)
    )
    VALUES (
        p_id_usuario, 
        p_nombre, 
        p_anio_ini, 
        p_mes_ini, 
        p_anio_fin, 
        p_mes_fin,
        p_ingreso_total, 
        p_gasto_total, 
        p_ahorro_total, 
        'activo',
        CURRENT TIMESTAMP, 
        p_creado_por
    );

    COMMIT;
END;

--------------
CREATE OR REPLACE PROCEDURE sp_actualizar_presupuesto(
p_id_presupuesto INT, 
p_nombre VARCHAR(100), 
anio_inicio INT, 
mes_inicio INT, 
anio_fin INT, 
mes_fin INT, 
p_modificado_por VARCHAR(200)
)
BEGIN 
    IF NOT EXISTS (SELECT * FROM dba.Presupuesto WHERE id_presupuesto = p_id_presupuesto AND estado_presupuesto IN ('Activo', 'Borrador')) THEN
        RAISERROR 99003 'Presupuesto no existe';
        RETURN; 
    END IF;

    --reglas de negocio 
    -- 1. el año final debe ser mayor o igual al año de inicio 
    IF p_anio_fin < p_anio_ini THEN
        RAISERROR 99014 'No se puede modificar, el año final debe ser mayor o igual al año de inicio';
        RETURN;
    END IF;

    --2. Si el año de fin y final son el mismo, el mes final debe ser mayor o igual al mes de inicio 
    IF p_anio_fin = p_anio_ini AND p_mes_fin < p_mes_ini THEN
        RAISERROR 99015 'No se puede modificar, si el año es igual, entonces el mes findebe ser mayor o igual al mes de inicio ';
        RETURN;
    END IF;

    UPDATE dba.Presupuesto SET 
    nombre_presupuesto = p_nombre, 
    anio_inicio = p_anio_inicio,
    mes_inicio = p_mes_inicio, 
    anio_fin = p_anio_fin,
    mes_fin = p_mes_fin, 
    modificado_por = p_modificado_por,
    modificado_en = CURRENT TIMESTAMP
    WHERE id_presupuesto = p_id_presupuesto;
    COMMIT; 
END; 

-----------------------------------------

CREATE OR REPLACE PROCEDURE sp_eliminar_presupuesto(p_id_presupuesto INT)
BEGIN 
    --TODO: VALIDAR LAS TRANSACCIONES Y QUE EL ID EXISTA 
    UPDATE dba.Presupuesto SET estado_presupuesto = 'cerrado'
    WHERE id_presupuesto = p_id_presupuesto;
    COMMIT; 
END; 

-------------------------
CREATE OR REPLACE PROCEDURE sp_consultar_presupuesto(
p_id_presupuesto INT)
BEGIN
    
    IF NOT EXISTS (SELECT * FROM dba.Presupuesto WHERE id_presupuesto = p_id_presupuesto) THEN
        RAISERROR 99003 'Presupuesto no existe';
        RETURN; 
    END IF;

    SELECT * FROM dba.Presupuesto 
END; 

----------------------------------
CREATE OR REPLACE PROCEDURE sp_listar_presupuestos_usuario(
p_id_usuario INT , 
p_estado VARCHAR(30))
BEGIN 
    IF NOT EXISTS (SELECT * FROM dba.Usuario WHERE id_usuario = p_id_usuario) THEN
        RAISERROR 99003 'Presupuesto no existe';
        RETURN; 
    END IF;

    Select * from dba.presupuesto WHERE
    id_usuario = p_usuario AND estado_presupuesto = p_estado; 
END; 