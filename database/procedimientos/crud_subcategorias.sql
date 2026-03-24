CREATE OR REPLACE PROCEDURE sp_insertar_subcategoria(
    p_id_categoria int, 
    p_nombre varchar(30), 
    p_descripcion varchar(300), 
    p_creado_por varchar(200),
    p_por_defecto bit
)
BEGIN
    
    IF NOT EXISTS (SELECT 1 FROM dba.Categoria WHERE id_categoria = p_id_categoria) THEN
        RAISERROR 99005 'la categoría seleccionada no existe.';
        RETURN; 
    END IF;

    
    INSERT INTO dba.SubCategoria (
        id_categoria, 
        nombre_subcategoria, 
        descripcion_detallada_sub, 
        por_defecto, 
        creado_por
    )
    VALUES (
        p_id_categoria, 
        p_nombre, 
        p_descripcion, 
        p_por_defecto,
        p_creado_por
    );

    COMMIT;
END;

------------------
CREATE OR REPLACE PROCEDURE sp_actualizar_subcategoria(p_id_subcategoria int, 
p_nombre VARCHAR(30), 
p_descripcion VARCHAR(300), 
p_modificado_por VARCHAR(200)
)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM dba.SubCategoria 
    WHERE id_subcategoria = p_id_subcategoria AND estado_sub = 1) THEN
        RAISERROR 99003 'subcategoria no existe';
        RETURN; 
    END IF;

    UPDATE dba.SubCategoria SET 
    nombre_subcategoria = p_nombre,
    descripcion_detallada_sub = p_descripcion,
    modificado_por = p_modificado_por,
    modificado_en = CURRENT TIMESTAMP
    WHERE id_subcategoria = p_id_subcategoria;

    COMMIT; 
END; 

---------------------
CREATE OR REPLACE PROCEDURE sp_eliminar_subcategoria(p_id_subcategoria INT)
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM dba.SubCategoria WHERE id_subcategoria = p_id_subcategoria) THEN
        RAISERROR 99006 'La subcategoria no existe.';
        RETURN; 
    END IF;

    IF EXISTS (SELECT 1 FROM dba.presupuesto_detalle WHERE id_subcategoria = p_id_subcategoria) THEN
        RAISERROR 99007 'No se puede eliminar, esta subcategoria ya esta asignada a un presupuesto.';
        RETURN;
    END IF;

    IF EXISTS (SELECT 1 FROM dba.transaccion WHERE id_presupuesto_detalle IN (
        SELECT id_presupuesto_detalle FROM dba.presupuesto_detalle WHERE id_subcategoria = p_id_subcategoria
    )) THEN
        RAISERROR 99008 'No se puede eliminar ya existen transacciones registradas con esta subcategoria.';
        RETURN;
    END IF;

    
    DELETE FROM dba.SubCategoria
    WHERE id_subcategoria = p_id_subcategoria;

    COMMIT; 
END; 
----------------------------------------
CREATE OR REPLACE PROCEDURE sp_consultar_subcategoria(p_id_subcategoria int)
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM dba.SubCategoria WHERE id_subcategoria = p_id_subcategoria) THEN
        RAISERROR 99006 'La subcategoria no existe.';
        RETURN; 
    END IF;

    SELECT 
        s.id_subcategoria,
        s.nombre_subcategoria,
        s.descripcion_detallada_sub,
        s.estado_sub,
        s.por_defecto,
        c.id_categoria,
        c.nombre_categoria, 
        c.tipo_categoria,
        s.creado_por,
        s.creado_en
    FROM dba.SubCategoria s
    INNER JOIN dba.Categoria c ON s.id_categoria = c.id_categoria
    WHERE s.id_subcategoria = p_id_subcategoria;
END; 
------------------------------
CREATE OR REPLACE PROCEDURE sp_listar_subcategorias_por_categoria(p_id_categoria INT)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM dba.Categoria WHERE id_categoria = p_id_categoria) THEN
        RAISERROR 99006 'La categoria no existe.';
        RETURN; 
    END IF;

    Select id_subcategoria,
    id_categoria, 
    nombre_subcategoria,
    descripcion_detallada_sub,
    estado_sub,
    por_defecto,
    creado_por,
    creado_en 
    from dba.SubCategoria
    WHERE id_categoria = p_id_categoria

END; 
---------------
CREATE OR REPLACE PROCEDURE sp_listar_subcategorias_presupuesto(
    p_id_presupuesto int
)
BEGIN 
    Select s.id_subcategoria,
    s.id_categoria, 
    s.nombre_subcategoria,
    c.tipo_categoria
    from dba.SubCategoria s INNER JOIN dba.presupuesto_detalle pd 
    ON s.id_subcategoria = pd.id_subcategoria
    INNER JOIN dba.Categoria c 
    ON s.id_categoria = c.id_categoria 
    where pd.id_presupuesto = p_id_presupuesto;
END; 
-------------
CREATE OR REPLACE PROCEDURE dba.sp_listar_Subcategorias_catalogo()
BEGIN
    SELECT 
        id_subcategoria,
        id_categoria,
        nombre_subcategoria
    FROM dba.subcategoria;
    
END;