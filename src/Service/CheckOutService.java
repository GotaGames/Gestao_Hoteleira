package Service;

import Model.Enums.EstadoQuarto;
import Model.Enums.EstadoReserva;
import Model.Hospedagem;
import Model.Quarto;
import Model.Reserva;
import Repository.HospedagemRepository;
import Repository.PagamentoRepository;
import Repository.ReservaRepository;

import java.time.LocalDate;

public class CheckOutService {

    private final HospedagemRepository hospedagemRepository;
    private final ReservaRepository reservaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final QuartoService quartoService;
    private final FaturaService faturaService;

    public CheckOutService() {
        this.hospedagemRepository = new HospedagemRepository();
        this.reservaRepository = new ReservaRepository();
        this.pagamentoRepository = new PagamentoRepository();
        this.quartoService = new QuartoService();
        this.faturaService = new FaturaService();
    }

    public ResultadoCheckOut realizarCheckOut(int numeroQuarto, LocalDate dataCheckOut) {
        Hospedagem hospedagem = hospedagemRepository.findByQuartoAtivo(numeroQuarto);
        if (hospedagem == null) {
            throw new IllegalArgumentException("Não existe hospedagem ativa para o quarto " + numeroQuarto + ".");
        }
        if (dataCheckOut == null) {
            throw new IllegalArgumentException("A data de check-out é obrigatória.");
        }
        if (dataCheckOut.isBefore(hospedagem.getDataCheckIn())) {
            throw new IllegalArgumentException("A data de check-out não pode ser anterior à de check-in.");
        }

        long dias = faturaService.calcularDiasHospedagem(hospedagem.getDataCheckIn(), dataCheckOut);
        double totalAlojamento = faturaService.calcularTotalAlojamento(hospedagem, dataCheckOut);
        double totalConsumos = faturaService.calcularTotalConsumos(hospedagem);
        double subtotal = totalAlojamento + totalConsumos;
        double desconto = faturaService.calcularDesconto(dias, subtotal);
        double totalFinal = subtotal - desconto;

        double jaPago = pagamentoRepository.totalPagoPorHospedagem(hospedagem.getIdHospedagem());
        double saldo = totalFinal - jaPago;

        if (saldo > 0.001) {
            throw new IllegalStateException(
                    "Check-out bloqueado. Falta pagar " + String.format("%.2f", saldo)
                            + " (total " + String.format("%.2f", totalFinal)
                            + ", pago " + String.format("%.2f", jaPago) + ")."
            );
        }

        hospedagemRepository.atualizarCheckOut(hospedagem.getIdHospedagem(), dataCheckOut);

        Reserva reserva = hospedagem.getReserva();
        if (reserva != null) {
            reservaRepository.atualizarEstado(reserva.getIdReserva(), EstadoReserva.FINALIZADA);
        }

        Quarto quarto = hospedagem.getReserva().getQuarto();
        quartoService.alterarEstado(quarto.getNumeroQuarto(), EstadoQuarto.DISPONIVEL);

        ResultadoCheckOut resultado = new ResultadoCheckOut();
        resultado.setHospedagem(hospedagem);
        resultado.setDias(dias);
        resultado.setTotalAlojamento(totalAlojamento);
        resultado.setTotalConsumos(totalConsumos);
        resultado.setSubtotal(subtotal);
        resultado.setDesconto(desconto);
        resultado.setTotalFinal(totalFinal);
        return resultado;
    }

    public double calcularSaldoAtual(int numeroQuarto) {
        Hospedagem h = hospedagemRepository.findByQuartoAtivo(numeroQuarto);
        if (h == null) {
            throw new IllegalArgumentException("Não existe hospedagem ativa no quarto " + numeroQuarto + ".");
        }
        LocalDate hoje = LocalDate.now();
        double total = faturaService.calcularTotalFinal(h, hoje);
        double pago = pagamentoRepository.totalPagoPorHospedagem(h.getIdHospedagem());
        return total - pago;
    }
}
