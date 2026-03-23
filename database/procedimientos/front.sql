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
-------------
CREATE OR REPLACE FUNCTION fn_obtener_id_detalle(
    p_id_presupuesto  int,
    p_id_subcategoria INT
) RETURNS INT
BEGIN
    DECLARE v_id int;
    SELECT id_presupuesto_detalle INTO v_id
    FROM dba.presupuesto_detalle
    WHERE id_presupuesto = p_id_presupuesto
    AND id_subcategoria = p_id_subcategoria;
    RETURN v_id;
END;
----------
CREATE OR REPLACE FUNCTION fn_ultimo_id_obligacion()
RETURNS INT
BEGIN
    RETURN @@IDENTITY;
END;
COMMIT;