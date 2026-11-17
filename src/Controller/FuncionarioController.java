package Controller;

import Model.Funcionario;
import Service.FuncionarioService;

import java.util.List;

public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController() {
        this.funcionarioService = new FuncionarioService();
    }

    public Funcionario criar(String nome, String contacto, String endereco, String sexo,
                             String documento, String cargo, int horarioTrabalho,
                             String nivelPermissao, double salario) {
        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setContacto(contacto);
        f.setEndereco(endereco);
        f.setSexo(sexo);
        f.setDocumentoIdentificacao(documento);
        f.setCargo(cargo);
        f.setHorarioTrabalho(horarioTrabalho);
        f.setNivelPermissao(nivelPermissao);
        f.setSalario(salario);
        return funcionarioService.criar(f);
    }

    public Funcionario buscarPorId(int idPessoa) {
        return funcionarioService.buscarPorId(idPessoa);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioService.listarTodos();
    }

    public void remover(int idPessoa) {
        funcionarioService.remover(idPessoa);
    }
}
