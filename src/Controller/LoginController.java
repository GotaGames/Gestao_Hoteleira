package Controller;

import Model.Funcionario;
import Service.FuncionarioService;

import java.util.List;

public class LoginController {

    private final FuncionarioService funcionarioService;

    public LoginController() {
        this.funcionarioService = new FuncionarioService();
    }

    public Funcionario autenticarFuncionario(String documento, String nivelPermissao) {
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
                return f;
            }
        }
        throw new IllegalArgumentException("Credenciais inválidas.");
    }
}
