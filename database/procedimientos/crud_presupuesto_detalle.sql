CREATE OR REPLACE PROCEDURE sp_insertar_presupuesto_detalle(
p_id_presupuesto INT, 
p_id_subcategoria int, 
p_monto_mensual NUMERIC(15,2), 
p_observaciones VARCHAR(100), 
p_creado_por VARCHAR(200)
)
BEGIN
    
    IF p_id_subcategoria IS NULL THEN
        RAISERROR 99020 'error todo detalle de presupuesto DEBE tener una subcategoría asociada.';
        RETURN;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM dba.Presupuesto WHERE id_presupuesto = p_id_presupuesto AND estado_presupuesto != 'cerrado') THEN
        RAISERROR 99021 'error el presupuesto no existe o ya esta cerrado.';
        RETURN;
    END IF;

    IF EXISTS (SELECT 1 FROM dba.presupuesto_detalle WHERE id_presupuesto = p_id_presupuesto AND id_subcategoria = p_id_subcategoria) THEN
        RAISERROR 99022 'esta subcategoría ya esta configurada en este presupuesto.';
        RETURN;
    END IF;

    INSERT INTO dba.presupuesto_detalle (
        id_presupuesto,
        id_subcategoria,
        monto_mensual_asignado,
        observaciones,
        creado_por
    )
    VALUES (
        p_id_presupuesto,
        p_id_subcategoria,
        p_monto_mensual,
        p_observaciones,
        p_creado_por
    );
    
    COMMIT; 
END; 

--------------------------

CREATE OR REPLACE PROCEDURA sp_actualizar_presupuesto_detalle(p_id_detalle INT, 
p_monto_mensual NUMERIC(15,2), 
p_observaciones VARCHAR(200), 
p_modificado_por VARCHAR(200)
)
BEGIN
    IF NOT EXISTS(SELECT * FROM dba.presupuesto_detalle pd INNER JOIN dba.presupuesto_detalle p ON pd.id_presupuesto_detalle = p.id_presupuesto WHERE pd.id_presupuesto_detalle = p_id_detalle AND 
    p.estado_presupuesto IN ('activo','borrador')
    )THEN
    RAISERROR 99024 'el detalle no existe o el presupuesto ya esta cerrado y no permite cambios.';
    RETURN;
    END IF;

    IF (p_monto_mensual < 0 )THEN
        RAISERROR 99025 'tiene que ser mayor a 0';
        RETURN;
    END IF;

    UPDATE dba.presupuesto_detalle SET 
        monto_mensual_asignado = p_monto_mensual,
        observaciones = p_observaciones,
        modificado_por = p_modificado_por,
        modificado_en = CURRENT TIMESTAMP
    WHERE id_presupuesto_detalle = p_id_detalle;


    COMMIT; 
END; 

---------------------

CREATE OR REPLACE PROCEDURE sp_eliminar_presupuesto_detalle(
p_id_detalle INT
)
BEGIN 
    IF EXISTS (SELECT * FROM dba.presupuesto_detalle pd 
    INNER JOIN dba.Presupuesto p ON pd.id_presupuesto = p.id_presupuesto 
    WHERE pd.id_presupuesto_detalle = p_id_detalle AND p.estado_presupuesto = 'Cerrado') THEN
    RAISERROR 99023 'No se puede eliminar un detalle de un presupuesto cerrado.';
    RETURN;
    END IF;

    DELETE FROM dba.presupuesto_detalle WHERE id_presupuesto_detalle = p_id_detalle;
    COMMIT; 
END; 

-------------------

CREATE OR REPLACE PROCEDURE sp_consultar_presupuesto_detalle(p_id_detalle INT)
BEGIN 

    IF NOT EXISTS (SELECT * FROM dba.presupuesto_detalle WHERE id_presupuesto_detalle = p_id_detalle) THEN
        RAISERROR 99003 'detalle no existe';
        RETURN; 
    END IF;

    SELECT 
        pd.id_presupuesto_detalle,
        pd.monto_mensual_asignado,
        pd.observaciones,
        s.nombre_subcategoria,
        c.nombre_categoria,
        c.tipo_categoria,
        p.nombre_presupuesto,
        p.anio_inicio,
        p.mes_inicio
    FROM dba.presupuesto_detalle pd
    INNER JOIN dba.SubCategoria s 
    ON pd.id_subcategoria = s.id_subcategoria
    INNER JOIN dba.Categoria c 
    ON s.id_categoria = c.id_categoria
    INNER JOIN dba.Presupuesto p 
    ON pd.id_presupuesto = p.id_presupuesto
    WHERE pd.id_presupuesto_detalle = p_id_detalle;

END; 

--------------------
CREATE OR REPLACE PROCEDURE sp_listar_detalles_presupuesto(p_id_presupuesto INT )
BEGIN
    IF NOT EXISTS (SELECT * FROM dba.presupuesto_detalle WHERE id_presupuesto_detalle = p_id_detalle) THEN
        RAISERROR 99003 'detalle no existe';
        RETURN; 
    END IF; 

    SELECT 
        pd.id_presupuesto_detalle,
        c.nombre_categoria,
        s.nombre_subcategoria,
        pd.monto_mensual_asignado,
        pd.observaciones
    FROM dba.presupuesto_detalle pd
    INNER JOIN dba.SubCategoria s ON 
    pd.id_subcategoria = s.id_subcategoria
    INNER JOIN dba.Categoria c ON s.id_categoria = c.id_categoria
    WHERE pd.id_presupuesto = p_id_presupuesto;

END; 
