package View;

import Controller.CheckInController;
import Model.Hospedagem;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

public class CheckInFrame extends JFrame {

    private final CheckInController checkInController;
    private JTextField campoIdReserva;

    public CheckInFrame() {
        this.checkInController = new CheckInController();
        setTitle("Check-in");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("ID da reserva:"));
        campoIdReserva = new JTextField();
        painel.add(campoIdReserva);

        JButton btnConfirmar = new JButton("Confirmar Check-in");
        btnConfirmar.addActionListener(e -> realizar());
        painel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        painel.add(btnCancelar);

        add(painel);
    }

    private void realizar() {
        try {
            int id = Integer.parseInt(campoIdReserva.getText().trim());
            Hospedagem h = checkInController.realizar(id);
            JOptionPane.showMessageDialog(this,
                    "Check-in realizado. Quarto " + h.getReserva().getQuarto().getNumeroQuarto() + " ocupado.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}