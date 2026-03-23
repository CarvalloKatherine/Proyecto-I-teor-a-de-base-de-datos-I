package presupuesto_personal;
import java.sql.Date;

public class Obligación {
    private int id_obligacion;
    private int id_subcategoria;
    private String nombre_obligacion;
    private String descripcion;
    private double monto_fijo_mensual;
    private int dia;
    private boolean vigente;
    private Date fecha_inicio;
    private Date fecha_fin;
    private String creado_por;
    private Date creado_en;
    private String modificado_por;
    private Date modificado_en;
    private String nombreSubcategoria; 

    public Obligación() {
    }

    public Obligación(int id_subcategoria, String nombre_obligacion, String descripcion, double monto_fijo_mensual, int dia, Date fecha_inicio, Date fecha_fin, String creado_por, String nombreSubcategoria) {
        this.id_subcategoria = id_subcategoria;
        this.nombre_obligacion = nombre_obligacion;
        this.descripcion = descripcion;
        this.monto_fijo_mensual = monto_fijo_mensual;
        this.dia = dia;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.creado_por = creado_por;
        this.nombreSubcategoria =  nombreSubcategoria; 
    }

    public int getId_obligacion() {
        return id_obligacion;
    }

    public void setId_obligacion(int id_obligacion) {
        this.id_obligacion = id_obligacion;
    }

    public int getId_subcategoria() {
        return id_subcategoria;
    }

    public void setId_subcategoria(int id_subcategoria) {
        this.id_subcategoria = id_subcategoria;
    }

    public String getNombre_obligacion() {
        return nombre_obligacion;
    }

    public void setNombre_obligacion(String nombre_obligacion) {
        this.nombre_obligacion = nombre_obligacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto_fijo_mensual() {
        return monto_fijo_mensual;
    }

    public void setMonto_fijo_mensual(double monto_fijo_mensual) {
        this.monto_fijo_mensual = monto_fijo_mensual;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public boolean isVigente() {
        return vigente;
    }

    public void setVigente(boolean vigente) {
        this.vigente = vigente;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
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

    public String getNombreSubcategoria() {
        return nombreSubcategoria;
    }

    public void setNombreSubcategoria(String nombreSubcategoria) {
        this.nombreSubcategoria = nombreSubcategoria;
    }
    
    @Override
    public String toString() {
    return nombre_obligacion;
    }
    
    
}
