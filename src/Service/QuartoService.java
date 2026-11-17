package Service;

import Model.Enums.EstadoQuarto;
import Model.Quarto;
import Repository.QuartoRepository;

import java.util.List;

public class QuartoService {

    private final QuartoRepository quartoRepository;

    public QuartoService() {
        this.quartoRepository = new QuartoRepository();
    }

    public Quarto criar(Quarto quarto) {
        if (quarto.getTipoQuarto() == null || quarto.getTipoQuarto().isBlank()) {
            throw new IllegalArgumentException("O tipo de quarto é obrigatório.");
        }
        if (quarto.getPrecoDiario() <= 0) {
            throw new IllegalArgumentException("O preço diário deve ser maior que zero.");
        }
        Quarto existente = quartoRepository.findById(quarto.getNumeroQuarto());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um quarto com este número.");
        }
        if (quarto.getEstadoQuarto() == null) {
            quarto.setEstadoQuarto(EstadoQuarto.DISPONIVEL);
        }
        boolean ok = quartoRepository.inserir(quarto);
        if (!ok) {
            throw new RuntimeException("Não foi possível registar o quarto.");
        }
        return quarto;
    }

    public Quarto atualizar(Quarto quarto) {
        boolean ok = quartoRepository.atualizar(quarto);
        if (!ok) {
            throw new RuntimeException("Não foi possível atualizar o quarto.");
        }
        return quarto;
    }

    public Quarto buscarPorNumero(int numeroQuarto) {
        Quarto q = quartoRepository.findById(numeroQuarto);
        if (q == null) {
            throw new IllegalArgumentException("Quarto não encontrado.");
        }
        return q;
    }

    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    }

    public List<Quarto> listarDisponiveis() {
        return quartoRepository.findByEstado(EstadoQuarto.DISPONIVEL);
    }

    public void alterarEstado(int numeroQuarto, EstadoQuarto estado) {
        boolean ok = quartoRepository.atualizarEstado(numeroQuarto, estado);
        if (!ok) {
            throw new RuntimeException("Não foi possível alterar o estado do quarto.");
        }
    }

    public void remover(int numeroQuarto) {
        boolean ok = quartoRepository.eliminar(numeroQuarto);
        if (!ok) {
            throw new RuntimeException("Não foi possível remover o quarto.");
        }
    }
}
