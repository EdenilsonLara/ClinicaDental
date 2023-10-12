package com.example.democlinica;

public class Tratamiento {
    private int codigoTratamiento;
    private String tipoServicio;
    private int costo;

    public Tratamiento(int codigoTratamiento, String tipoServicio, int costo) {
        this.codigoTratamiento = codigoTratamiento;
        this.tipoServicio = tipoServicio;
        this.costo = costo;
    }

    public int getCodigoTratamiento() {
        return codigoTratamiento;
    }

    public void setCodigoTratamiento(int codigoTratamiento) {
        this.codigoTratamiento = codigoTratamiento;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Tratamiento{" +
                "codigoTratamiento=" + codigoTratamiento +
                ", tipoServicio='" + tipoServicio + '\'' +
                ", costo=" + costo +
                '}';
    }
}

