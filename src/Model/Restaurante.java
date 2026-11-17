package Model;

public class Restaurante extends Servico {

    private String tipoRefeicao;

    public Restaurante() {
    }

    public Restaurante(int idServico, double precoBase, String tipoRefeicao) {
        super(idServico, precoBase);
        this.tipoRefeicao = tipoRefeicao;
    }

    public String getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(String tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    @Override
    public double calcularServico() {
        return this.precoBase;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "idServico=" + idServico +
                ", precoBase=" + precoBase +
                ", tipoRefeicao='" + tipoRefeicao + '\'' +
                '}';
    }
}
