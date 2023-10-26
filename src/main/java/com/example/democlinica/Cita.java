package com.example.democlinica;

import java.time.LocalDateTime;

public class Cita {
    private int codigoCita;
    private String nombresPaciente;
    private String apellidosPaciente;
    private String motivoConsulta;
    private int codigoTratamiento;
    private double costo;
    private LocalDateTime fecha; // Usamos LocalDateTime en lugar de null para la fecha
    private String estado;
    private int codigoPaciente;
    private int codigoSucursal;

    public Cita(int codigoCita, String nombresPaciente, String apellidosPaciente, String motivoConsulta,
                int codigoTratamiento, double costo, LocalDateTime fecha, String estado, int codigoPaciente, int codigoSucursal) {
        this.codigoCita = codigoCita;
        this.nombresPaciente = nombresPaciente;
        this.apellidosPaciente = apellidosPaciente;
        this.motivoConsulta = motivoConsulta;
        this.codigoTratamiento = codigoTratamiento;
        this.costo = costo;
        this.fecha = fecha; // Asignamos la fecha proporcionada
        this.estado = estado;
        this.codigoPaciente = codigoPaciente;
        this.codigoSucursal = codigoSucursal;
    }

    public int getCodigoCita() {
        return codigoCita;
    }

    public void setCodigoCita(int codigoCita) {
        this.codigoCita = codigoCita;
    }

    public String getNombresPaciente() {
        return nombresPaciente;
    }

    public void setNombresPaciente(String nombresPaciente) {
        this.nombresPaciente = nombresPaciente;
    }

    public String getApellidosPaciente() {
        return apellidosPaciente;
    }

    public void setApellidosPaciente(String apellidosPaciente) {
        this.apellidosPaciente = apellidosPaciente;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public int getCodigoTratamiento() {
        return codigoTratamiento;
    }

    public void setCodigoTratamiento(int codigoTratamiento) {
        this.codigoTratamiento = codigoTratamiento;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public int getCodigoSucursal() {
        return codigoSucursal;
    }

    public void setCodigoSucursal(int codigoSucursal) {
        this.codigoSucursal = codigoSucursal;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "codigoCita=" + codigoCita +
                ", nombresPaciente='" + nombresPaciente + '\'' +
                ", apellidosPaciente='" + apellidosPaciente + '\'' +
                ", motivoConsulta='" + motivoConsulta + '\'' +
                ", codigoTratamiento=" + codigoTratamiento +
                ", costo=" + costo +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                ", codigoPaciente=" + codigoPaciente +
                ", codigoSucursal=" + codigoSucursal +
                '}';
    }
}
