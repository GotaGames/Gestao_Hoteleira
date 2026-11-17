package Model;

public class Eventos extends Servico {

    private int numeroDias;

    public Eventos() {
    }

    public Eventos(int idServico, double precoBase, int numeroDias) {
        super(idServico, precoBase);
        this.numeroDias = numeroDias;
    }

    public int getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(int numeroDias) {
        this.numeroDias = numeroDias;
    }

    @Override
    public double calcularServico() {
        return this.numeroDias * this.precoBase;
    }

    @Override
    public String toString() {
        return "Eventos{" +
                "idServico=" + idServico +
                ", precoBase=" + precoBase +
                ", numeroDias=" + numeroDias +
                '}';
    }
}
