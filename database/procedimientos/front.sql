CREATE OR REPLACE PROCEDURE sp_validar_login(
    p_email varchar(200),
    p_clave varchar(30)
)
BEGIN
    SELECT id_usuario, primer_nombre, primer_apellido, correo_electronico, salario_mensual_base
    FROM dba.Usuario
    WHERE correo_electronico = p_email 
    AND clave = p_clave
    AND estado = 1;
END;