CREATE OR REPLACE PROCEDURE sp_insertar_obligacion(
p_id_subcategoria INT, 
p_nombre VARCHAR(50), 
p_descripcion VARCHAR(100), 
p_monto NUMERIC(15,2), 
p_dia_vencimiento INT, 
p_fecha_inicio DATE, 
p_fecha_fin DATE, 
p_creado_por VARCHAR(200)
)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM dba.SubCategoria WHERE id_subcategoria = p_id_subcategoria) THEN
        RAISERROR 99031 'la subcategoria no existe.';
        RETURN;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM dba.SubCategoria s
        INNER JOIN dba.Categoria c ON s.id_categoria = c.id_categoria
        WHERE s.id_subcategoria = p_id_subcategoria 
        AND c.tipo_categoria = 'gasto'
    ) THEN
        RAISERROR 99032 'una obligación fija solo puede asociarse a categorias de tipo gasto.';
        RETURN;
    END IF;

    IF (p_fecha_fin IS NOT NULL AND p_fecha_fin <= p_fecha_inicio )THEN
        RAISERROR 99033 'la fecha de fin debe ser luego a la fecha de inicio.';
        RETURN;
    END IF;

    IF (p_dia_vencimiento < 1 OR p_dia_vencimiento > 31) THEN
        RAISERROR 99034 'El diaa de vencimiento debe estar entre 1 y 31';
        RETURN;
    END IF;

    INSERT INTO dba.obligacion_fija (
        id_subcategoria,
        nombre_obligacion,
        descripcion,
        monto_fijo_mensual,
        dia,
        fecha_inicio,
        fecha_fin,
        creado_por
    )
    VALUES (
        p_id_subcategoria,
        p_nombre,
        p_descripcion,
        p_monto,
        p_dia_vencimiento,
        p_fecha_inicio,
        p_fecha_fin,
        p_creado_por
    );

    COMMIT; 
END; 

--------------


CREATE OR REPLACE PROCEDURE sp_actualizar_obligacion(
p_id_obligacion INT, 
p_nombre VARCHAR(50), 
p_descripcion VARCHAR(100), 
p_monto NUMERIC(15,2), 
p_dia_vencimiento INT, 
p_fecha_fin DATE, 
p_vigente BIT,
p_modificado_por VARCHAR(200)
)
BEGIN

    IF NOT EXISTS (SELECT 1 FROM dba.obligacion_fija WHERE id_obligacion = p_id_obligacion) THEN
        RAISERROR 99035 'La obligación no existe.';
        RETURN;
    END IF;

    UPDATE dba.obligacion_fija SET 
        nombre_obligacion = p_nombre,
        descripcion = p_descripcion,
        monto_fijo_mensual = p_monto,
        dia = p_dia_vencimiento,
        fecha_fin = p_fecha_fin,
        vigente = p_vigente,
        modificado_por = p_modificado_por,
    WHERE id_obligacion = p_id_obligacion;

    COMMIT; 
END; 

---------------

CREATE OR REPLACE PROCEDURE sp_eliminar_obligacion(
p_id_obligacion INT, 
p_modificado_por varchar(200))
BEGIN

IF NOT EXISTS (SELECT 1 FROM dba.obligacion_fija WHERE id_obligacion = p_id_obligacion) THEN
        RAISERROR 99035 'La obligación no existe.';
        RETURN;
    END IF;

UPDATE dba.obligacion_fija SET 
vigente = 0,
modificado_por = p_modificado_por,
modificado_en = CURRENT TIMESTAMP
WHERE id_obligacion = p_id_obligacion;

    COMMIT; 
END; 

--------------------

CREATE OR REPLACE PROCEDURE sp_consultar_obligacion(p_id_obligacion INT)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM dba.obligacion_fija WHERE id_obligacion = p_id_obligacion) THEN
        RAISERROR 99035 'La obligación no existe.';
        RETURN;
    END IF;

    SELECT
    o.id_obligacion,
    o.id_subcategoria,
    o.nombre_obligacion,
    o.descripcion,
    o.monto_fijo_mensual,
    o.dia,
    o.vigente,
    o.fecha_inicio,
    o.fecha_fin,
    s.nombre_subcategoria 
    FROM dba.obligacion_fija o
    INNER JOIN dba.SubCategoria s ON o.id_subcategoria = s.id_subcategoria
    WHERE o.id_obligacion = p_id_obligacion;

END; 

--------------

CREATE OR REPLACE PROCEDURE sp_listar_obligaciones_usuario(
p_id_usuario INT, 
p_activo bit)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM dba.obligacion_fija WHERE id_obligacion = p_id_obligacion) THEN
        RAISERROR 99035 'La obligación no existe.';
        RETURN;
    END IF;

    SELECT o.id_obligacion, 
    o.nombre_obligacion, 
    o.monto_fijo_mensual, 
    o.dia, 
    s.nombre_subcategoria
    FROM dba.obligacion_fija o INNER JOIN dba.SubCategoria s 
    ON o.id_subcategoria = s.id_subcategoria
    WHERE o.id_usuario = p_id_usuario 
    AND o.vigente = p_activo

END; 