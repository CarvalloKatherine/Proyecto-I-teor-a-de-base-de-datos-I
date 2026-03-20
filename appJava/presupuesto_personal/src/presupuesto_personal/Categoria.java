package presupuesto_personal;

import java.sql.Date;

public class Categoria {
    int id_categoria; 
    String nombre_categoria;        
    String descripcion_detallada;   
    String tipo_categoria;         
    String nombre_icono;            
    String color_hex;                
    String orden_presentacion;       
    String creado_por;               
    Date creado_en;                
    String modificado_por;           
    Date modificado_en;  

    public Categoria() {
    }

    public Categoria(int id_categoria, String nombre_categoria, String descripcion_detallada, String tipo_categoria, String nombre_icono, String color_hex, String orden_presentacion, String creado_por, Date creado_en, String modificado_por, Date modificado_en) {
        this.id_categoria = id_categoria;
        this.nombre_categoria = nombre_categoria;
        this.descripcion_detallada = descripcion_detallada;
        this.tipo_categoria = tipo_categoria;
        this.nombre_icono = nombre_icono;
        this.color_hex = color_hex;
        this.orden_presentacion = orden_presentacion;
        this.creado_por = creado_por;
        this.creado_en = creado_en;
        this.modificado_por = modificado_por;
        this.modificado_en = modificado_en;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre_categoria() {
        return nombre_categoria;
    }

    public void setNombre_categoria(String nombre_categoria) {
        this.nombre_categoria = nombre_categoria;
    }

    public String getDescripcion_detallada() {
        return descripcion_detallada;
    }

    public void setDescripcion_detallada(String descripcion_detallada) {
        this.descripcion_detallada = descripcion_detallada;
    }

    public String getTipo_categoria() {
        return tipo_categoria;
    }

    public void setTipo_categoria(String tipo_categoria) {
        this.tipo_categoria = tipo_categoria;
    }

    public String getNombre_icono() {
        return nombre_icono;
    }

    public void setNombre_icono(String nombre_icono) {
        this.nombre_icono = nombre_icono;
    }

    public String getColor_hex() {
        return color_hex;
    }

    public void setColor_hex(String color_hex) {
        this.color_hex = color_hex;
    }

    public String getOrden_presentacion() {
        return orden_presentacion;
    }

    public void setOrden_presentacion(String orden_presentacion) {
        this.orden_presentacion = orden_presentacion;
    }

    public String getCreado_por() {
        return creado_por;
    }

    public void setCreado_por(String creado_por) {
        this.creado_por = creado_por;
    }

    public Date getCreado_en() {
        return creado_en;
    }

    public void setCreado_en(Date creado_en) {
        this.creado_en = creado_en;
    }

    public String getModificado_por() {
        return modificado_por;
    }

    public void setModificado_por(String modificado_por) {
        this.modificado_por = modificado_por;
    }

    public Date getModificado_en() {
        return modificado_en;
    }

    public void setModificado_en(Date modificado_en) {
        this.modificado_en = modificado_en;
    }
    
    
}
