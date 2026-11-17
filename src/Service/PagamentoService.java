package Service;

import Model.Enums.EstadoPagamento;
import Model.Enums.MetodoPagamento;
import Model.Hospedagem;
import Model.Pagamento;
import Repository.PagamentoRepository;

import java.time.LocalDate;
import java.util.List;

public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService() {
        this.pagamentoRepository = new PagamentoRepository();
    }

    public Pagamento registar(Hospedagem hospedagem, LocalDate data, double valor, MetodoPagamento metodo) {
        if (hospedagem == null) {
            throw new IllegalArgumentException("A hospedagem é obrigatória.");
        }
        if (data == null) {
            throw new IllegalArgumentException("A data do pagamento é obrigatória.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser maior que zero.");
        }
        if (metodo == null) {
            throw new IllegalArgumentException("O método de pagamento é obrigatório.");
        }

        Pagamento p = new Pagamento();
        p.setHospedagem(hospedagem);
        p.setDataPagamento(data);
        p.setValor(valor);
        p.setMetodo(metodo);

        double jaPago = pagamentoRepository.totalPagoPorHospedagem(hospedagem.getIdHospedagem());
        double saldoAntes = calcularSaldoDevido(hospedagem);
        double saldoDepois = saldoAntes - valor;

        if (saldoDepois <= 0.001) {
            p.setEstado(EstadoPagamento.PAGO);
        } else if (jaPago + valor > 0) {
            p.setEstado(EstadoPagamento.PARCIAL);
        } else {
            p.setEstado(EstadoPagamento.PENDENTE);
        }

        int id = pagamentoRepository.inserir(p);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível registar o pagamento.");
        }
        return p;
    }

    public double calcularSaldoDevido(Hospedagem hospedagem) {
        FaturaService fs = new FaturaService();
        LocalDate checkOut = hospedagem.getDataCheckOut() != null
                ? hospedagem.getDataCheckOut()
                : LocalDate.now();
        double total = fs.calcularTotalFinal(hospedagem, checkOut);
        double pago = pagamentoRepository.totalPagoPorHospedagem(hospedagem.getIdHospedagem());
        return total - pago;
    }

    public double totalPago(Hospedagem hospedagem) {
        return pagamentoRepository.totalPagoPorHospedagem(hospedagem.getIdHospedagem());
    }

    public List<Pagamento> listarPorHospedagem(Hospedagem hospedagem) {
        return pagamentoRepository.findByHospedagem(hospedagem.getIdHospedagem());
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public List<Pagamento> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return pagamentoRepository.findByPeriodo(inicio, fim);
    }
}
