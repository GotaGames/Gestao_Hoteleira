package Controller;

import Model.Cliente;
import Repository.ClienteRepository;
import Service.ClienteService;

import java.util.List;

public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    public ClienteController() {
        this.clienteService = new ClienteService();
        this.clienteRepository = new ClienteRepository();
    }

    public Cliente criar(String nome, String contacto, String endereco, String sexo,
                         String documento, String nacionalidade) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setContacto(contacto);
        c.setEndereco(endereco);
        c.setSexo(sexo);
        c.setDocumentoIdentificacao(documento);
        c.setNacionalidade(nacionalidade);
        return clienteService.criar(c);
    }

    public Cliente buscarPorId(int idPessoa) {
        return clienteService.buscarPorId(idPessoa);
    }

    public Cliente buscarPorDocumento(String documento) {
        Cliente c = clienteRepository.findByDocumento(documento);
        if (c == null) {
            throw new IllegalArgumentException("Cliente não encontrado para o documento indicado.");
        }
        return c;
    }

    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    public void remover(int idPessoa) {
        clienteService.remover(idPessoa);
    }
}
