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