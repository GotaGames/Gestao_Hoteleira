package Model;

import Model.Enums.EstadoQuarto;

public class Quarto {

    private int numeroQuarto;
    private String tipoQuarto;
    private double precoDiario;
    private EstadoQuarto estadoQuarto;

    public Quarto() {
    }

    public Quarto(int numeroQuarto, String tipoQuarto, double precoDiario, EstadoQuarto estadoQuarto) {
        this.numeroQuarto = numeroQuarto;
        this.tipoQuarto = tipoQuarto;
        this.precoDiario = precoDiario;
        this.estadoQuarto = estadoQuarto;
    }

    public int getNumeroQuarto() {
        return numeroQuarto;
    }

    public void setNumeroQuarto(int numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public String getTipoQuarto() {
        return tipoQuarto;
    }

    public void setTipoQuarto(String tipoQuarto) {
        this.tipoQuarto = tipoQuarto;
    }

    public double getPrecoDiario() {
        return precoDiario;
    }

    public void setPrecoDiario(double precoDiario) {
        this.precoDiario = precoDiario;
    }

    public EstadoQuarto getEstadoQuarto() {
        return estadoQuarto;
    }

    public void setEstadoQuarto(EstadoQuarto estadoQuarto) {
        this.estadoQuarto = estadoQuarto;
    }

    @Override
    public String toString() {
        return "Quarto{" +
                "numeroQuarto=" + numeroQuarto +
                ", tipoQuarto='" + tipoQuarto + '\'' +
                ", precoDiario=" + precoDiario +
                ", estadoQuarto=" + estadoQuarto +
                '}';
    }
}