package presupuesto_personal;

import java.sql.Date;

public class PresupuestoDetalle {
    int id_presupuesto_detalle;
    int id_presupuesto;
    int id_subcategoria;
    double monto_mensual_asignado;
    String observaciones;
    String creado_por;
    Date creado_en;
    String modificado_por;
    Date modificado_en;

    public PresupuestoDetalle() {
    }

    public PresupuestoDetalle(int id_presupuesto_detalle, int id_presupuesto, int id_subcategoria, double monto_mensual_asignado, String observaciones, String creado_por, Date creado_en, String modificado_por, Date modificado_en) {
        this.id_presupuesto_detalle = id_presupuesto_detalle;
        this.id_presupuesto = id_presupuesto;
        this.id_subcategoria = id_subcategoria;
        this.monto_mensual_asignado = monto_mensual_asignado;
        this.observaciones = observaciones;
        this.creado_por = creado_por;
        this.creado_en = creado_en;
        this.modificado_por = modificado_por;
        this.modificado_en = modificado_en;
    }

    public int getId_presupuesto_detalle() {
        return id_presupuesto_detalle;
    }

    public void setId_presupuesto_detalle(int id_presupuesto_detalle) {
        this.id_presupuesto_detalle = id_presupuesto_detalle;
    }

    public int getId_presupuesto() {
        return id_presupuesto;
    }

    public void setId_presupuesto(int id_presupuesto) {
        this.id_presupuesto = id_presupuesto;
    }

    public int getId_subcategoria() {
        return id_subcategoria;
    }

    public void setId_subcategoria(int id_subcategoria) {
        this.id_subcategoria = id_subcategoria;
    }

    public double getMonto_mensual_asignado() {
        return monto_mensual_asignado;
    }

    public void setMonto_mensual_asignado(double monto_mensual_asignado) {
        this.monto_mensual_asignado = monto_mensual_asignado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
