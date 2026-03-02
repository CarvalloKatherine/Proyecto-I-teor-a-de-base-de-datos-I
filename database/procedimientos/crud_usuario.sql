CREATE OR REPLACE PROCEDURE sp_insertar_usuario(p_password VARCHAR (30), 
p_primer_nombre VARCHAR (100),
p_segundo_nombre VARCHAR(100),
p_primer_apellido VARCHAR(100), 
p_segundo_apellido VARCHAR (100), 
p_email VARCHAR (200), 
p_salario NUMERIC(15,2), 
p_creado_por VARCHAR(200)
)
BEGIN 
--LOGICA 

    IF LENGTH(p_password) < 5 THEN
        RAISERROR 99001 'La clave debe tener 5 caracteres o mas';
        RETURN; 
    END IF;

    IF EXISTS (SELECT 1 FROM dba.Usuario WHERE correo_electronico = p_email) THEN
        RAISERROR 99002 'Correo ya registrado';
        RETURN; 
    END IF;

    INSERT INTO dba.Usuario(clave,
    primer_nombre,
    segundo_nombre,
    primer_apellido, 
    segundo_apellido, 
    correo_electronico, 
    fecha_registro, 
    salario_mensual_base, 
    creado_por)
    VALUES (p_password, 
    p_primer_nombre, 
    p_segundo_nombre,
    p_primer_apellido,
    p_segundo_apellido,
    p_email,
    CURRENT DATE,
    p_salario,
    p_creado_por
    );

    COMMIT; 
END; 


CREATE OR REPLACE PROCEDURE sp_actualizar_usuario(p_id int , 
p_primer_nombre varchar(100), 
p_segundo_nombre varchar(100),
p_primer_apellido varchar(100), 
p_segundo_apellido varchar(100),
p_salario_mensual NUMERIC(15,2), 
p_modificado_por varchar(200)
)
BEGIN 
    UPDATE dba.Usuario SET primer_nombre = p_primer_nombre,
    segundo_nombre = p_segundo_nombre,
    primer_apellido = p_primer_apellido,
    segundo_apellido = p_segundo_apellido,
    salario_mensual_base = p_salario_mensual,
    modificado_por = p_modificado_por,
    modificado_en = CURRENT TIMESTAMP,
    WHERE id_usuario = p_id;
    COMMIT; 
END;

CREATE OR REPLACE PROCEDURE sp_eliminar_usuario(p_id int)
BEGIN
    IF NOT EXISTS (SELECT * FROM dba.Usuario WHERE id_usuario = p_id) THEN
        RAISERROR 99003 'usuario no existe';
        RETURN; 
    END IF;
    UPDATE dba.Usuario SET estado = 0,
    modificado_en = CURRENT TIMESTAMP,
    WHERE id_usuario = p_id;
    COMMIT;
END;


CREATE OR REPLACE PROCEDURE sp_consultar_usuario(p_id int)
BEGIN
    IF NOT EXISTS (SELECT * FROM dba.Usuario WHERE id_usuario = p_id) THEN
        RAISERROR 99003 'usuario no existe';
        RETURN; 
    END IF;
    SELECT * FROM dba.Usuario WHERE id_usuario = p_id;
    
END;
