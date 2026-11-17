package Model;

public class Lavandaria extends Servico {

    private int quantPecas;

    public Lavandaria() {
    }

    public Lavandaria(int idServico, double precoBase, int quantPecas) {
        super(idServico, precoBase);
        this.quantPecas = quantPecas;
    }

    public int getQuantPecas() {
        return quantPecas;
    }

    public void setQuantPecas(int quantPecas) {
        this.quantPecas = quantPecas;
    }

    @Override
    public double calcularServico() {
        return this.quantPecas * this.precoBase;
    }

    @Override
    public String toString() {
        return "Lavandaria{" +
                "idServico=" + idServico +
                ", precoBase=" + precoBase +
                ", quantPecas=" + quantPecas +
                '}';
    }
}
