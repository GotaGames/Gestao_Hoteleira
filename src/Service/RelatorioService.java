package Service;

import Model.Pagamento;
import Repository.PagamentoRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelatorioService {

    private final PagamentoRepository pagamentoRepository;

    public RelatorioService() {
        this.pagamentoRepository = new PagamentoRepository();
    }

    public double totalMesAtual() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());
        return pagamentoRepository.totalPorPeriodo(inicio, fim);
    }

    public Map<YearMonth, Double> totalUltimosMeses(int numeroMeses) {
        Map<YearMonth, Double> resultado = new LinkedHashMap<>();
        YearMonth atual = YearMonth.now();
        for (int i = numeroMeses - 1; i >= 0; i--) {
            YearMonth ym = atual.minusMonths(i);
            LocalDate inicio = ym.atDay(1);
            LocalDate fim = ym.atEndOfMonth();
            double total = pagamentoRepository.totalPorPeriodo(inicio, fim);
            resultado.put(ym, total);
        }
        return resultado;
    }

    public List<Pagamento> pagamentosDoMes(YearMonth ym) {
        return pagamentoRepository.findByPeriodo(ym.atDay(1), ym.atEndOfMonth());
    }

    public int numeroPagamentosMesAtual() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.withDayOfMonth(1);
        LocalDate fim = hoje.withDayOfMonth(hoje.lengthOfMonth());
        return pagamentoRepository.findByPeriodo(inicio, fim).size();
    }

    public double ticketMedioMesAtual() {
        int n = numeroPagamentosMesAtual();
        if (n == 0) {
            return 0.0;
        }
        return totalMesAtual() / n;
    }

    public List<String> resumoMensalTexto(int numeroMeses) {
        List<String> linhas = new ArrayList<>();
        Map<YearMonth, Double> totais = totalUltimosMeses(numeroMeses);
        for (Map.Entry<YearMonth, Double> e : totais.entrySet()) {
            linhas.add(e.getKey() + "  ->  " + e.getValue());
        }
        return linhas;
    }
}
