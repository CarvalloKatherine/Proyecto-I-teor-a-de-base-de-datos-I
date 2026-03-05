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
        id_usuario, 
        nombre_presupuesto, 
        anio_inicio, 
        mes_inicio, 
        anio_fin, 
        mes_fin,
        total_ingresos, 
        total_gastos, 
        total_ahorro, 
        estado_presupuesto,
        fecha_hora_creacion, 
        creado_por
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