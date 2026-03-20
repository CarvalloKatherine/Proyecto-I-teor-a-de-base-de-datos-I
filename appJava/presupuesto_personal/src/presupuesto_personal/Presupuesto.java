package presupuesto_personal;

import java.sql.Date;

public class Presupuesto {
    int id_presupuesto;        
    int id_usuario;              
    String nombre_descriptivo;    
    int anio_inicio;          
    int mes_inicio;              
    int anio_fin;                
    int mes_fin;                 
    double total_ingresos;          
    double total_gastos;             
    double total_ahorro;            
    Date fecha_hora_creacion;    
    String estado_presupuesto;   
    String creado_por;               
    Date creado_en;                
    String modificado_por;        
    Date modificado_en; 

    public Presupuesto() {
    }

    public Presupuesto(int id_presupuesto, int id_usuario, String nombre_descriptivo, int anio_inicio, int mes_inicio, int anio_fin, int mes_fin, double total_ingresos, double total_gastos, double total_ahorro, Date fecha_hora_creacion, String estado_presupuesto, String creado_por, Date creado_en, String modificado_por, Date modificado_en) {
        this.id_presupuesto = id_presupuesto;
        this.id_usuario = id_usuario;
        this.nombre_descriptivo = nombre_descriptivo;
        this.anio_inicio = anio_inicio;
        this.mes_inicio = mes_inicio;
        this.anio_fin = anio_fin;
        this.mes_fin = mes_fin;
        this.total_ingresos = total_ingresos;
        this.total_gastos = total_gastos;
        this.total_ahorro = total_ahorro;
        this.fecha_hora_creacion = fecha_hora_creacion;
        this.estado_presupuesto = estado_presupuesto;
        this.creado_por = creado_por;
        this.creado_en = creado_en;
        this.modificado_por = modificado_por;
        this.modificado_en = modificado_en;
    }

    public int getId_presupuesto() {
        return id_presupuesto;
    }

    public void setId_presupuesto(int id_presupuesto) {
        this.id_presupuesto = id_presupuesto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre_descriptivo() {
        return nombre_descriptivo;
    }

    public void setNombre_descriptivo(String nombre_descriptivo) {
        this.nombre_descriptivo = nombre_descriptivo;
    }

    public int getAnio_inicio() {
        return anio_inicio;
    }

    public void setAnio_inicio(int anio_inicio) {
        this.anio_inicio = anio_inicio;
    }

    public int getMes_inicio() {
        return mes_inicio;
    }

    public void setMes_inicio(int mes_inicio) {
        this.mes_inicio = mes_inicio;
    }

    public int getAnio_fin() {
        return anio_fin;
    }

    public void setAnio_fin(int anio_fin) {
        this.anio_fin = anio_fin;
    }

    public int getMes_fin() {
        return mes_fin;
    }

    public void setMes_fin(int mes_fin) {
        this.mes_fin = mes_fin;
    }

    public double getTotal_ingresos() {
        return total_ingresos;
    }

    public void setTotal_ingresos(double total_ingresos) {
        this.total_ingresos = total_ingresos;
    }

    public double getTotal_gastos() {
        return total_gastos;
    }

    public void setTotal_gastos(double total_gastos) {
        this.total_gastos = total_gastos;
    }

    public double getTotal_ahorro() {
        return total_ahorro;
    }

    public void setTotal_ahorro(double total_ahorro) {
        this.total_ahorro = total_ahorro;
    }

    public Date getFecha_hora_creacion() {
        return fecha_hora_creacion;
    }

    public void setFecha_hora_creacion(Date fecha_hora_creacion) {
        this.fecha_hora_creacion = fecha_hora_creacion;
    }

    public String getEstado_presupuesto() {
        return estado_presupuesto;
    }

    public void setEstado_presupuesto(String estado_presupuesto) {
        this.estado_presupuesto = estado_presupuesto;
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

