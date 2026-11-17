package Service;

import Model.Hospedagem;
import Model.Servico;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FaturaService implements RegrasFatura {

    public long calcularDiasHospedagem(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("As datas de check-in e check-out são obrigatórias.");
        }
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("A data de check-out não pode ser anterior à de check-in.");
        }
        long dias = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (dias == 0) {
            return 1;
        }
        return dias;
    }

    public double calcularTotalAlojamento(Hospedagem hospedagem, LocalDate dataCheckOut) {
        long dias = calcularDiasHospedagem(hospedagem.getDataCheckIn(), dataCheckOut);
        double precoDiario = hospedagem.getReserva().getQuarto().getPrecoDiario();
        return dias * precoDiario;
    }

    public double calcularTotalConsumos(Hospedagem hospedagem) {
        double total = 0.0;
        for (Servico s : hospedagem.getServicos()) {
            total += s.calcularServico();
        }
        return total;
    }

    public double calcularSubtotal(Hospedagem hospedagem, LocalDate dataCheckOut) {
        return calcularTotalAlojamento(hospedagem, dataCheckOut) + calcularTotalConsumos(hospedagem);
    }

    @Override
    public double calcularDesconto(long diasHospedagem, double valorBase) {
        if (diasHospedagem > 7) {
            return valorBase * 0.10;
        }
        return 0.0;
    }

    public double calcularTotalFinal(Hospedagem hospedagem, LocalDate dataCheckOut) {
        long dias = calcularDiasHospedagem(hospedagem.getDataCheckIn(), dataCheckOut);
        double subtotal = calcularSubtotal(hospedagem, dataCheckOut);
        double desconto = calcularDesconto(dias, subtotal);
        return subtotal - desconto;
    }
}
