package Model;

public abstract class Servico {

    protected int idServico;
    protected double precoBase;

    public Servico() {
    }

    public Servico(int idServico, double precoBase) {
        this.idServico = idServico;
        this.precoBase = precoBase;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public abstract double calcularServico();

    @Override
    public String toString() {
        return "Servico{" +
                "idServico=" + idServico +
                ", precoBase=" + precoBase +
                '}';
    }
}
