package Service;

import Model.Cliente;
import Repository.ClienteRepository;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
    }

    public Cliente criar(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (cliente.getDocumentoIdentificacao() == null || cliente.getDocumentoIdentificacao().isBlank()) {
            throw new IllegalArgumentException("O documento de identificação é obrigatório.");
        }

        Cliente existente = clienteRepository.findByDocumento(cliente.getDocumentoIdentificacao());
        if (existente != null) {
            throw new IllegalArgumentException("Já existe um cliente com este documento.");
        }

        int id = clienteRepository.inserir(cliente);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível registar o cliente.");
        }
        return cliente;
    }

    public Cliente atualizar(Cliente cliente) {
        boolean ok = clienteRepository.atualizar(cliente);
        if (!ok) {
            throw new RuntimeException("Não foi possível atualizar o cliente.");
        }
        return cliente;
    }

    public Cliente buscarPorId(int idPessoa) {
        Cliente c = clienteRepository.findById(idPessoa);
        if (c == null) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        return c;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public void remover(int idPessoa) {
        boolean ok = clienteRepository.eliminar(idPessoa);
        if (!ok) {
            throw new RuntimeException("Não foi possível remover o cliente.");
        }
    }
}
