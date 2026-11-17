package Controller;

import Service.FuncionarioService;

import java.util.List;
import Model.Funcionario;

public class LoginController {

    private final FuncionarioService funcionarioService;

    public LoginController() {
        this.funcionarioService = new FuncionarioService();
    }

    public boolean autenticarFuncionario(String documento, String nivelPermissao) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("O documento é obrigatório.");
        }
        if (nivelPermissao == null || nivelPermissao.isBlank()) {
            throw new IllegalArgumentException("O nível de permissão é obrigatório.");
        }

        List<Funcionario> todos = funcionarioService.listarTodos();
        for (Funcionario f : todos) {
            if (documento.equalsIgnoreCase(f.getDocumentoIdentificacao())
                    && nivelPermissao.equalsIgnoreCase(f.getNivelPermissao())) {
                return true;
            }
        }
        return false;
    }

    public boolean autenticarCliente(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("O documento é obrigatório.");
        }
        ClienteController cc = new ClienteController();
        try {
            cc.buscarPorDocumento(documento);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}