package com.example.democlinica;

import java.time.LocalDate;

public class Empleado {
    private int codigoEmpleado;
    private String nombresEmpleado;
    private String apellidosEmpleado;
    private LocalDate fechaNacimiento;
    private String genero;
    private String telefono;
    private String dui;

    public Empleado(int codigoEmpleado, String nombresEmpleado, String apellidosEmpleado, LocalDate fechaNacimiento, String genero, String telefono, String dui) {
        this.codigoEmpleado = codigoEmpleado;
        this.nombresEmpleado = nombresEmpleado;
        this.apellidosEmpleado = apellidosEmpleado;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.telefono = telefono;
        this.dui = dui;
    }

    public Empleado() {

    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public String getNombresEmpleado() {
        return nombresEmpleado;
    }

    public void setNombresEmpleado(String nombresEmpleado) {
        this.nombresEmpleado = nombresEmpleado;
    }

    public String getApellidosEmpleado() {
        return apellidosEmpleado;
    }

    public void setApellidosEmpleado(String apellidosEmpleado) {
        this.apellidosEmpleado = apellidosEmpleado;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "codigoEmpleado=" + codigoEmpleado +
                ", nombresEmpleado='" + nombresEmpleado + '\'' +
                ", apellidosEmpleado='" + apellidosEmpleado + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", genero='" + genero + '\'' +
                ", telefono='" + telefono + '\'' +
                ", dui='" + dui + '\'' +
                '}';
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
    }
}

