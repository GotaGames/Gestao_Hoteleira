package View;

import Controller.CheckOutController;
import Controller.HospedagemController;
import Model.Hospedagem;
import Service.ResultadoCheckOut;
import Util.DateUtil;
import Util.FaturaWriter;
import com.toedter.calendar.JDateChooser;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

public class CheckOutFrame extends JFrame {

    private final CheckOutController checkOutController;
    private final HospedagemController hospedagemController;

    private JComboBox<String> comboQuarto;
    private JDateChooser dataCheckOut;

    public CheckOutFrame() {
        this.checkOutController = new CheckOutController();
        this.hospedagemController = new HospedagemController();

        setTitle("Check-out");
        setSize(480, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        construir();
        carregarQuartosOcupados();
    }

    private void construir() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 10, 10));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painel.add(new JLabel("Quarto ocupado:"));
        comboQuarto = new JComboBox<>();
        painel.add(comboQuarto);

        painel.add(new JLabel("Data check-out:"));
        dataCheckOut = new JDateChooser();
        dataCheckOut.setDateFormatString("dd/MM/yyyy");
        dataCheckOut.setDate(new java.util.Date());
        painel.add(dataCheckOut);

        JButton btnConfirmar = new JButton("Confirmar Check-out");
        btnConfirmar.addActionListener(e -> realizar());
        painel.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        painel.add(btnCancelar);

        add(painel);
    }

    private void carregarQuartosOcupados() {
        comboQuarto.removeAllItems();
        try {
            List<Hospedagem> ativas = hospedagemController.listarAtivas();
            for (Hospedagem h : ativas) {
                comboQuarto.addItem(
                        h.getReserva().getQuarto().getNumeroQuarto() + " - " + h.getCliente().getNome()
                );
            }
            if (comboQuarto.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Não há quartos ocupados neste momento.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar quartos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int extrairNumeroQuarto() {
        Object item = comboQuarto.getSelectedItem();
        if (item == null) {
            throw new IllegalStateException("Nenhum quarto ocupado disponível.");
        }
        String texto = item.toString();
        int espaco = texto.indexOf(" - ");
        if (espaco < 0) {
            throw new IllegalStateException("Formato inválido do quarto.");
        }
        return Integer.parseInt(texto.substring(0, espaco).trim());
    }

    private void realizar() {
        try {
            int numeroQuarto = extrairNumeroQuarto();
            LocalDate data = DateUtil.toLocalDate(dataCheckOut.getDate());
            if (data == null) {
                throw new IllegalArgumentException("Selecione a data de check-out.");
            }

            ResultadoCheckOut resultado = checkOutController.realizar(numeroQuarto, data);
            String ficheiro = FaturaWriter.gravar(resultado);

            new FaturaFrame(resultado, ficheiro).setVisible(true);
            carregarQuartosOcupados();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
