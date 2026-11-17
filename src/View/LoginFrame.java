package View;

import Controller.LoginController;

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
        setSize(420, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void construirComponentes() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Documento do funcionário:"));
        campoDocumento = new JTextField();
        painel.add(campoDocumento);

        painel.add(new JLabel("Nível de permissão:"));
        campoPermissao = new JPasswordField();
        painel.add(campoPermissao);

        JButton btnEntrar = new JButton("Entrar como Funcionário");
        btnEntrar.addActionListener(e -> entrarComoFuncionario());
        painel.add(btnEntrar);

        JButton btnCliente = new JButton("Entrar como Cliente");
        btnCliente.addActionListener(e -> entrarComoCliente());
        painel.add(btnCliente);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        painel.add(btnSair);

        painel.add(new JLabel(""));

        add(painel);
    }

    private void entrarComoFuncionario() {
        String documento = campoDocumento.getText().trim();
        String permissao = new String(campoPermissao.getPassword()).trim();
        try {
            boolean ok = loginController.autenticarFuncionario(documento, permissao);
            if (ok) {
                new MenuFuncionarioFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas.", "Login", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void entrarComoCliente() {
        new MenuClienteFrame().setVisible(true);
        dispose();
    }
}