
package View;

import Controller.CheckOutController;
import Service.ResultadoCheckOut;
import Util.FaturaWriter;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CheckOutFrame extends JFrame {

    private final CheckOutController checkOutController;
    private JTextField campoNumeroQuarto;
    private JTextField campoDataCheckOut;

    public CheckOutFrame() {
        this.checkOutController = new CheckOutController();
        setTitle("Check-out");
        setSize(420, 240);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Número do quarto:"));
        campoNumeroQuarto = new JTextField();
        painel.add(campoNumeroQuarto);

        painel.add(new JLabel("Data check-out (AAAA-MM-DD):"));
        campoDataCheckOut = new JTextField(LocalDate.now().toString());
        painel.add(campoDataCheckOut);

        JButton btnConfirmar = new JButton("Confirmar Check-out");
        btnConfirmar.addActionListener(e -> realizar());
        painel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        painel.add(btnCancelar);

        add(painel);
    }

    private void realizar() {
        try {
            int numeroQuarto = Integer.parseInt(campoNumeroQuarto.getText().trim());
            LocalDate data;
            try {
                data = LocalDate.parse(campoDataCheckOut.getText().trim());
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Data inválida. Use o formato AAAA-MM-DD.");
            }

            ResultadoCheckOut resultado = checkOutController.realizar(numeroQuarto, data);
            String ficheiro = FaturaWriter.gravar(resultado);

            StringBuilder sb = new StringBuilder();
            sb.append("Check-out concluído.\n\n");
            sb.append("Dias: ").append(resultado.getDias()).append("\n");
            sb.append("Alojamento: ").append(resultado.getTotalAlojamento()).append("\n");
            sb.append("Consumos: ").append(resultado.getTotalConsumos()).append("\n");
            sb.append("Subtotal: ").append(resultado.getSubtotal()).append("\n");
            sb.append("Desconto: ").append(resultado.getDesconto()).append("\n");
            sb.append("Total a pagar: ").append(resultado.getTotalFinal()).append("\n");

            if (ficheiro != null) {
                sb.append("\nFatura gravada em: ").append(ficheiro);
            }

            JOptionPane.showMessageDialog(this, sb.toString(),
                    "Fatura", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número de quarto inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}