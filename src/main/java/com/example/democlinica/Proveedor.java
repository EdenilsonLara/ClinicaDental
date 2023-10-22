package com.example.democlinica;

public class Proveedor {
    private int codigoProveedor;
    private String nombresProveedor;
    private String direccionProveedor;
    private String telefonoProveedor;
    private String nombreContactoProveedor;

    public Proveedor(int codigoProveedor, String nombresProveedor, String direccionProveedor, String telefonoProveedor, String nombreContactoProveedor) {
        this.codigoProveedor = codigoProveedor;
        this.nombresProveedor = nombresProveedor;
        this.direccionProveedor = direccionProveedor;
        this.telefonoProveedor = telefonoProveedor;
        this.nombreContactoProveedor = nombreContactoProveedor;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getNombresProveedor() {
        return nombresProveedor;
    }

    public void setNombresProveedor(String nombresProveedor) {
        this.nombresProveedor = nombresProveedor;
    }

    public String getDireccionProveedor() {
        return direccionProveedor;
    }

    public void setDireccionProveedor(String direccionProveedor) {
        this.direccionProveedor = direccionProveedor;
    }

    public String getTelefonoProveedor() {
        return telefonoProveedor;
    }

    public void setTelefonoProveedor(String telefonoProveedor) {
        this.telefonoProveedor = telefonoProveedor;
    }

    public String getNombreContactoProveedor() {
        return nombreContactoProveedor;
    }

    public void setNombreContactoProveedor(String nombreContactoProveedor) {
        this.nombreContactoProveedor = nombreContactoProveedor;
    }
}
