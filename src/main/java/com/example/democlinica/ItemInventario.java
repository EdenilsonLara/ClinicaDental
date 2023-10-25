package com.example.democlinica;

public class ItemInventario {
    private int codigoInventario;
    private String nombreEquipo;
    private String descripcion;
    private double precio;
    private int cantidad;
    private int codigoProveedor;  // Cambiado a un entero para coincidir con la base de datos

    public ItemInventario(int codigoInventario, String nombreEquipo, String descripcion, double precio, int cantidad, int codigoProveedor) {
        this.codigoInventario = codigoInventario;
        this.nombreEquipo = nombreEquipo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.codigoProveedor = codigoProveedor;
    }

    // Getters y Setters
    public int getCodigoInventario() {
        return codigoInventario;
    }

    public void setCodigoInventario(int codigoInventario) {
        this.codigoInventario = codigoInventario;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }
}
