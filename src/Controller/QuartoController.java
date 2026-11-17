package Controller;

import Model.Enums.EstadoQuarto;
import Model.Quarto;
import Service.QuartoService;

import java.util.List;

public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController() {
        this.quartoService = new QuartoService();
    }

    public Quarto criar(int numeroQuarto, String tipoQuarto, double precoDiario) {
        Quarto q = new Quarto();
        q.setNumeroQuarto(numeroQuarto);
        q.setTipoQuarto(tipoQuarto);
        q.setPrecoDiario(precoDiario);
        q.setEstadoQuarto(EstadoQuarto.DISPONIVEL);
        return quartoService.criar(q);
    }

    public Quarto buscarPorNumero(int numeroQuarto) {
        return quartoService.buscarPorNumero(numeroQuarto);
    }

    public List<Quarto> listarTodos() {
        return quartoService.listarTodos();
    }

    public List<Quarto> listarDisponiveis() {
        return quartoService.listarDisponiveis();
    }

    public void alterarEstado(int numeroQuarto, EstadoQuarto estado) {
        quartoService.alterarEstado(numeroQuarto, estado);
    }

    public void remover(int numeroQuarto) {
        quartoService.remover(numeroQuarto);
    }
}