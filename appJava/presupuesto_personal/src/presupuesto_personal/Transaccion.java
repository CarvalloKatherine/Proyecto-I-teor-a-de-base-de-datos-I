package presupuesto_personal;

import java.sql.Date;

public class Transaccion {
    private int id_transaccion; 
    private int id_presupuesto_detalle;
    private int anio;
    private int mes;
    private String tipo_transaccion;
    private String descripcion;
    private double monto;
    private Date fecha;
    private String metodo_pago;
    private Integer num_factura;
    private String observaciones;
    private String creado_por;
    private Date creado_en ;
    private String modificado_por;
    private Date modificado_en; 
    private Integer  id_obligacion; 

    public Transaccion() {
    }

    public Transaccion(int id_presupuesto_detalle, int anio, int mes, String tipo_transaccion, String descripcion, double monto, Date fecha, String metodo_pago, Integer  num_factura, String observaciones, String creado_por, Integer  id_obligacion) {
        this.id_presupuesto_detalle = id_presupuesto_detalle;
        this.anio = anio;
        this.mes = mes;
        this.tipo_transaccion = tipo_transaccion;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.metodo_pago = metodo_pago;
        this.num_factura = num_factura;
        this.observaciones = observaciones;
        this.creado_por = creado_por;
        this.id_obligacion = id_obligacion; 
    }

    public int getId_transaccion() {
        return id_transaccion;
    }

    public void setId_transaccion(int id_transaccion) {
        this.id_transaccion = id_transaccion;
    }

    public int getId_presupuesto_detalle() {
        return id_presupuesto_detalle;
    }

    public void setId_presupuesto_detalle(int id_presupuesto_detalle) {
        this.id_presupuesto_detalle = id_presupuesto_detalle;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public String getTipo_transaccion() {
        return tipo_transaccion;
    }

    public void setTipo_transaccion(String tipo_transaccion) {
        this.tipo_transaccion = tipo_transaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public Integer  getNum_factura() {
        return num_factura;
    }

    public void setNum_factura(Integer num_factura) {
        this.num_factura = num_factura;
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

    public Integer  getId_obligacion() {
        return id_obligacion;
    }

    public void setId_obligacion(Integer id_obligacion) {
        this.id_obligacion = id_obligacion;
    }
    
    
}

