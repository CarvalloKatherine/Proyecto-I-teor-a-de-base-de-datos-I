package presupuesto_personal;

import java.sql.Date;

public class Usuario {
    
    private int idUsuario; 
    private String clave;                   
    private String primerNombre;          
    private String segundoNombre;        
    private String primerApellido;       
    private String segundoApellido;     
    private String correo;   
    private Date fecha;          
    private double salario;   
    private boolean estado;                 
    private String creadoPor;              
    private Date creadoEn;               
    private String modificadoPor;          
    private Date modificadoEn;
    
    public Usuario(){}
    
    public Usuario(int idUsuario, String clave, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String correo, Date fecha, double salario, boolean estado, String creadoPor, Date creadoEn, String modificadoPor, Date modificadoEn) {
        this.idUsuario = idUsuario;
        this.clave = clave;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correo = correo;
        this.fecha = fecha;
        this.salario = salario;
        this.estado = estado;
        this.creadoPor = creadoPor;
        this.creadoEn = creadoEn;
        this.modificadoPor = modificadoPor;
        this.modificadoEn = modificadoEn;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Date getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Date creadoEn) {
        this.creadoEn = creadoEn;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public Date getModificadoEn() {
        return modificadoEn;
    }

    public void setModificadoEn(Date modificadoEn) {
        this.modificadoEn = modificadoEn;
    }

    
    
}

