package Model;

import Model.Enums.EstadoPagamento;
import Model.Enums.MetodoPagamento;

import java.time.LocalDate;

public class Pagamento {

    private int idPagamento;
    private Hospedagem hospedagem;
    private LocalDate dataPagamento;
    private double valor;
    private MetodoPagamento metodo;
    private EstadoPagamento estado;

    public Pagamento() {
    }

    public Pagamento(int idPagamento, Hospedagem hospedagem, LocalDate dataPagamento,
                     double valor, MetodoPagamento metodo, EstadoPagamento estado) {
        this.idPagamento = idPagamento;
        this.hospedagem = hospedagem;
        this.dataPagamento = dataPagamento;
        this.valor = valor;
        this.metodo = metodo;
        this.estado = estado;
    }

    public int getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(int idPagamento) {
        this.idPagamento = idPagamento;
    }

    public Hospedagem getHospedagem() {
        return hospedagem;
    }

    public void setHospedagem(Hospedagem hospedagem) {
        this.hospedagem = hospedagem;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public EstadoPagamento getEstado() {
        return estado;
    }

    public void setEstado(EstadoPagamento estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "idPagamento=" + idPagamento +
                ", idHospedagem=" + (hospedagem != null ? hospedagem.getIdHospedagem() : 0) +
                ", dataPagamento=" + dataPagamento +
                ", valor=" + valor +
                ", metodo=" + metodo +
                ", estado=" + estado +
                '}';
    }
}
