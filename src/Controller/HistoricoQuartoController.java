package Controller;

import Model.HistoricoQuarto;
import Repository.HistoricoQuartoRepository;

import java.util.List;

public class HistoricoQuartoController {

    private final HistoricoQuartoRepository historicoQuartoRepository;

    public HistoricoQuartoController() {
        this.historicoQuartoRepository = new HistoricoQuartoRepository();
    }

    public List<HistoricoQuarto> listarPorQuarto(int numeroQuarto) {
        return historicoQuartoRepository.findByQuarto(numeroQuarto);
    }

    public List<HistoricoQuarto> listarTodos() {
        return historicoQuartoRepository.findAll();
    }
}
