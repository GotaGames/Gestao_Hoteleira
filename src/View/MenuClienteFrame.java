package View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class MenuClienteFrame extends JFrame {

    public MenuClienteFrame() {
        setTitle("Menu do Cliente");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel titulo = new JLabel("Área do Cliente", JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(18f));
        titulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridLayout(4, 1, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 40, 20, 40));

        JButton btnListar = new JButton("Ver quartos disponíveis");
        btnListar.addActionListener(e -> {
            new ListarQuartosFrame().setVisible(true);
        });
        painel.add(btnListar);

        JButton btnReservar = new JButton("Fazer nova reserva");
        btnReservar.addActionListener(e -> {
            new ReservaFrame().setVisible(true);
        });
        painel.add(btnReservar);

        JButton btnVoltar = new JButton("Voltar ao login");
        btnVoltar.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        painel.add(btnVoltar);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        painel.add(btnSair);

        add(painel, BorderLayout.CENTER);
    }
}