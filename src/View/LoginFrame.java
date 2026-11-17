package View;

import Controller.LoginController;
import Model.Funcionario;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class LoginFrame extends JFrame {

    private final LoginController loginController;
    private JTextField campoDocumento;
    private JPasswordField campoPermissao;

    public LoginFrame() {
        this.loginController = new LoginController();
        configurarJanela();
        construirComponentes();
    }

    private void configurarJanela() {
        setTitle("Sistema de Gestão Hoteleira - Login");
        setSize(420, 240);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void construirComponentes() {
        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Documento do funcionário:"));
        campoDocumento = new JTextField();
        painel.add(campoDocumento);

        painel.add(new JLabel("Nível de permissão:"));
        campoPermissao = new JPasswordField();
        painel.add(campoPermissao);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> entrar());
        painel.add(btnEntrar);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        painel.add(btnSair);

        add(painel);

        getRootPane().setDefaultButton(btnEntrar);
    }

    private void entrar() {
        String documento = campoDocumento.getText().trim();
        String permissao = new String(campoPermissao.getPassword()).trim();

        try {
            Funcionario f = loginController.autenticarFuncionario(documento, permissao);
            new MenuFuncionarioFrame(f).setVisible(true);
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login", JOptionPane.WARNING_MESSAGE);
        }
    }
}
