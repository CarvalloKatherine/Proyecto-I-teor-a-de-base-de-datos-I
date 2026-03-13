/*
que debe hacer el trigger: 
- se hace despues de insertar
- crea automaticamente una subcategoria con nombre "general"
- la marca como por defecto 
- garantizar que toda categoria tenga min una sub

INVESTIGACIÓN 
para obtener el id de la categoria, al hacerlo automaticamente el sistema ya sabe cual es y lo guarda en una tabla virtual REFERENCING NEW 
solamente lo llamamos y obtenemos el valor. 
referencing new es una tabla que contene el registro del dato que acaba de ser insertado
*/

CREATE TRIGGER tr_subcategoria_defecto_ai
AFTER INSERT ON dba.Categoria 
REFERENCING NEW AS cat
FOR EACH ROW 
BEGIN 
    INSERT INTO dba.SubCategoria (id_categoria, nombre_subcategoria, descripcion_detallada_sub, por_defecto, creado_por
    )
    VALUES (cat.id_categoria, 'General', 'Categoria por defecto',1,'Sistema')
END; 