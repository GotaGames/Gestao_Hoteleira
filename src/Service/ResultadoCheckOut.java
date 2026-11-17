package Service;

import Model.Hospedagem;

public class ResultadoCheckOut {

    private Hospedagem hospedagem;
    private long dias;
    private double totalAlojamento;
    private double totalConsumos;
    private double subtotal;
    private double desconto;
    private double totalFinal;

    public Hospedagem getHospedagem() {
        return hospedagem;
    }

    public void setHospedagem(Hospedagem hospedagem) {
        this.hospedagem = hospedagem;
    }

    public long getDias() {
        return dias;
    }

    public void setDias(long dias) {
        this.dias = dias;
    }

    public double getTotalAlojamento() {
        return totalAlojamento;
    }

    public void setTotalAlojamento(double totalAlojamento) {
        this.totalAlojamento = totalAlojamento;
    }

    public double getTotalConsumos() {
        return totalConsumos;
    }

    public void setTotalConsumos(double totalConsumos) {
        this.totalConsumos = totalConsumos;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public double getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(double totalFinal) {
        this.totalFinal = totalFinal;
    }
}
