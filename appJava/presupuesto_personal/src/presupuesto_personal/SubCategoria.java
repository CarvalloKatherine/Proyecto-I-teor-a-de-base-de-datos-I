package presupuesto_personal;

import java.sql.Date;

public class SubCategoria {
    private int id_subcategoria;
    private int id_categoria;
    private String nombre_subcategoria;
    private String descripcion_detallada_sub;
    private boolean estado_sub;
    private boolean por_defecto;
    private String creado_por;
    private Date creado_en;
    private String modificado_por;
    private Date modificado_en;
    private String tipo_categoria;

    public SubCategoria() {
    }

    public SubCategoria(int id_subcategoria, int id_categoria, String nombre_subcategoria, String descripcion_detallada_sub, boolean estado_sub, boolean por_defecto, String creado_por, Date creado_en, String modificado_por, Date modificado_en) {
        this.id_subcategoria = id_subcategoria;
        this.id_categoria = id_categoria;
        this.nombre_subcategoria = nombre_subcategoria;
        this.descripcion_detallada_sub = descripcion_detallada_sub;
        this.estado_sub = estado_sub;
        this.por_defecto = por_defecto;
        this.creado_por = creado_por;
        this.creado_en = creado_en;
        this.modificado_por = modificado_por;
        this.modificado_en = modificado_en;
    }

    public int getId_subcategoria() {
        return id_subcategoria;
    }

    public void setId_subcategoria(int id_subcategoria) {
        this.id_subcategoria = id_subcategoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombre_subcategoria() {
        return nombre_subcategoria;
    }

    public void setNombre_subcategoria(String nombre_subcategoria) {
        this.nombre_subcategoria = nombre_subcategoria;
    }

    public String getDescripcion_detallada_sub() {
        return descripcion_detallada_sub;
    }

    public void setDescripcion_detallada_sub(String descripcion_detallada_sub) {
        this.descripcion_detallada_sub = descripcion_detallada_sub;
    }

    public boolean isEstado_sub() {
        return estado_sub;
    }

    public void setEstado_sub(boolean estado_sub) {
        this.estado_sub = estado_sub;
    }

    public boolean isPor_defecto() {
        return por_defecto;
    }

    public void setPor_defecto(boolean por_defecto) {
        this.por_defecto = por_defecto;
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

    public String getTipo_categoria() {
        return tipo_categoria;
    }

    public void setTipo_categoria(String tipo_categoria) {
        this.tipo_categoria = tipo_categoria;
    }
    
    
    @Override
    public String toString() {
    return nombre_subcategoria + " (" + tipo_categoria + ")";
}
}
