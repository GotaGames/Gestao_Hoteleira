package Controller;

import Model.Enums.MetodoPagamento;
import Model.Hospedagem;
import Model.Pagamento;
import Service.PagamentoService;

import java.time.LocalDate;
import java.util.List;

public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController() {
        this.pagamentoService = new PagamentoService();
    }

    public Pagamento registar(Hospedagem hospedagem, LocalDate data, double valor, MetodoPagamento metodo) {
        return pagamentoService.registar(hospedagem, data, valor, metodo);
    }

    public double saldoDevido(Hospedagem hospedagem) {
        return pagamentoService.calcularSaldoDevido(hospedagem);
    }

    public double totalPago(Hospedagem hospedagem) {
        return pagamentoService.totalPago(hospedagem);
    }

    public List<Pagamento> listarPorHospedagem(Hospedagem hospedagem) {
        return pagamentoService.listarPorHospedagem(hospedagem);
    }

    public List<Pagamento> listarTodos() {
        return pagamentoService.listarTodos();
    }
}
