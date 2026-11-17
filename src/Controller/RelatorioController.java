package Controller;

import Service.RelatorioService;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController() {
        this.relatorioService = new RelatorioService();
    }

    public double totalMesAtual() {
        return relatorioService.totalMesAtual();
    }

    public Map<YearMonth, Double> totalUltimosMeses(int numeroMeses) {
        return relatorioService.totalUltimosMeses(numeroMeses);
    }

    public int numeroPagamentosMesAtual() {
        return relatorioService.numeroPagamentosMesAtual();
    }

    public double ticketMedioMesAtual() {
        return relatorioService.ticketMedioMesAtual();
    }

    public List<String> resumoMensalTexto(int numeroMeses) {
        return relatorioService.resumoMensalTexto(numeroMeses);
    }
}
