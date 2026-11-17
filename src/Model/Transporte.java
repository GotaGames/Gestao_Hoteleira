package Model;

public class Transporte extends Servico {

    private double distanciaKm;

    public Transporte() {
    }

    public Transporte(int idServico, double precoBase, double distanciaKm) {
        super(idServico, precoBase);
        this.distanciaKm = distanciaKm;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    @Override
    public double calcularServico() {
        return this.precoBase * this.distanciaKm;
    }

    @Override
    public String toString() {
        return "Transporte{" +
                "idServico=" + idServico +
                ", precoBase=" + precoBase +
                ", distanciaKm=" + distanciaKm +
                '}';
    }
}