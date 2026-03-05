CREATE OR REPLACE PROCEDURE sp_insertar_categoria(p_nombre varchar(30), 
p_descripcion varchar(300), 
p_tipo varchar(30), 
p_icono varchar(30),
p_color varchar(30),
p_orden varchar(30),
p_creado_por varchar(200)
)
BEGIN
    INSERT INTO dba.Categoria(
    nombre_categoria, 
    descripcion_detallada, 
    tipo_categoria,
    nombre_icono,
    color_hex, 
    orden_presentacion,
    creado_por
    ) VALUES (p_nombre,
    p_descripcion,
    p_tipo,
    p_icono,
    p_color, 
    p_orden, 
    p_creado_por
    ); 

    COMMIT; 
END; 
----------------------------
CREATE OR REPLACE PROCEDURE sp_actualizar_categoria(p_id_categoria, 
p_nombre varchar(30), 
p_descripcion varchar(300), 
p_color_hex varchar(30),
p_modificado_por varchar(200))
BEGIN 
UPDATE dba.Categoria SET nombre_categoria = p_nombre, 
descripcion_detallada = p_descripcion, 
color_hex = p_color_hex,
modificado_por = p_modificado_por, 
modificado_en = CURRENT TIMESTAMP,
WHERE id_categoria = p_id_categoria;
    COMMIT; 
END;

----------------------------------

CREATE OR REPLACE PROCEDURE sp_eliminar_categoria(p_id_categoria int)
BEGIN
    IF NOT EXISTS (SELECT * FROM dba.Categoria WHERE id_categoria = p_id_categoria ) THEN
        RAISERROR 99003 'Categoria no existe';
        RETURN; 
    END IF;

    IF EXISTS (SELECT * FROM dba.SubCategoria 
               WHERE id_categoria = p_id_categoria 
               AND por_defecto = 0 
               AND estado_sub = 1) THEN
        
        RAISERROR 99004 'No se puede eliminar. La categoria tiene subcategorias personalizadas activas.';
        RETURN;
    END IF;
    
    DELETE FROM dba.SubCategoria
    WHERE id_categoria = p_id_categoria;

    DELETE FROM dba.Categoria
    WHERE id_categoria = p_id_categoria;
    COMMIT;
END;
--------------------------------------
CREATE OR REPLACE PROCEDURE sp_consultar_categoria(p_id_categoria int)
BEGIN 

    IF NOT EXISTS (SELECT * FROM dba.Categoria WHERE id_categoria = p_id_categoria) THEN
        RAISERROR 99003 'Categoria no existe';
        RETURN; 
    END IF;

    SELECT * FROM dba.Categoria WHERE id_categoria = p_id_categoria;
END; 
------------------------------------------
CREATE OR REPLACE PROCEDURE sp_listar_categorias(p_id_usuario int, p_tipo varchar(30))
BEGIN
    IF NOT EXISTS (SELECT * FROM dba.Usuario WHERE id_usuario = p_id_usuario) THEN
        RAISERROR 99003 'usuario no existe';
        RETURN; 
    END IF;

    SELECT * FROM dba.Categoria c 
    INNER JOIN dba.SubCategoria sb  ON c.id_categoria = sb.id_categoria
    INNER JOIN dba.presupuesto_detalle pd ON sb.id_subcategoria = pd.id_subcategoria
    INNER JOIN dba.Presupuesto p ON pd.id_presupuesto = p.id_presupuesto
    INNER JOIN dba.Usuario u ON p.id_usuario = u.id_usuario
    WHERE u.id_usuario = p_id_usuario AND 
    c.tipo_categoria = p_tipo; 
  
END; 
