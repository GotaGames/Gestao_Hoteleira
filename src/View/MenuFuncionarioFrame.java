package View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class MenuFuncionarioFrame extends JFrame {

    public MenuFuncionarioFrame() {
        setTitle("Menu do Funcionário");
        setSize(420, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLabel titulo = new JLabel("Área do Funcionário", JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(18f));
        titulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridLayout(5, 1, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 40, 20, 40));

        JButton btnCheckIn = new JButton("Realizar check-in");
        btnCheckIn.addActionListener(e -> new CheckInFrame().setVisible(true));
        painel.add(btnCheckIn);

        JButton btnConsumo = new JButton("Adicionar consumo");
        btnConsumo.addActionListener(e -> new ConsumoFrame().setVisible(true));
        painel.add(btnConsumo);

        JButton btnCheckOut = new JButton("Realizar check-out");
        btnCheckOut.addActionListener(e -> new CheckOutFrame().setVisible(true));
        painel.add(btnCheckOut);

        JButton btnGestao = new JButton("Gerir funcionários");
        btnGestao.addActionListener(e -> new GestaoFuncionarioFrame().setVisible(true));
        painel.add(btnGestao);

        JButton btnVoltar = new JButton("Voltar ao login");
        btnVoltar.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        painel.add(btnVoltar);

        add(painel, BorderLayout.CENTER);
    }
}