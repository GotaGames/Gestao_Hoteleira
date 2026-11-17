package Service;

import Model.Funcionario;
import Repository.FuncionarioRepository;

import java.util.List;

public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService() {
        this.funcionarioRepository = new FuncionarioRepository();
    }

    public Funcionario criar(Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do funcionário é obrigatório.");
        }
        if (funcionario.getDocumentoIdentificacao() == null || funcionario.getDocumentoIdentificacao().isBlank()) {
            throw new IllegalArgumentException("O documento de identificação é obrigatório.");
        }
        if (funcionario.getSalario() < 0) {
            throw new IllegalArgumentException("O salário não pode ser negativo.");
        }

        int id = funcionarioRepository.inserir(funcionario);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível registar o funcionário.");
        }
        return funcionario;
    }

    public Funcionario atualizar(Funcionario funcionario) {
        boolean ok = funcionarioRepository.atualizar(funcionario);
        if (!ok) {
            throw new RuntimeException("Não foi possível atualizar o funcionário.");
        }
        return funcionario;
    }

    public Funcionario buscarPorId(int idPessoa) {
        Funcionario f = funcionarioRepository.findById(idPessoa);
        if (f == null) {
            throw new IllegalArgumentException("Funcionário não encontrado.");
        }
        return f;
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public void remover(int idPessoa) {
        boolean ok = funcionarioRepository.eliminar(idPessoa);
        if (!ok) {
            throw new RuntimeException("Não foi possível remover o funcionário.");
        }
    }
}
