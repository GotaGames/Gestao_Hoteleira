package View;

import Controller.CheckInController;
import Controller.ReservaController;
import Model.Enums.EstadoReserva;
import Model.Hospedagem;
import Model.Reserva;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public class CheckInFrame extends JFrame {

    private final CheckInController checkInController;
    private final ReservaController reservaController;

    private JTable tabela;
    private DefaultTableModel modelo;

    public CheckInFrame() {
        this.checkInController = new CheckInController();
        this.reservaController = new ReservaController();

        setTitle("Check-in");
        setSize(720, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregarReservasPendentes();
    }

    private void construir() {
        JLabel titulo = new JLabel("Reservas pendentes", JLabel.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(16f));
        titulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Cliente", "Documento", "Quarto", "Check-in", "Check-out"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel();
        JButton btnCheckIn = new JButton("Realizar Check-in da reserva selecionada");
        btnCheckIn.addActionListener(e -> realizarCheckIn());
        rodape.add(btnCheckIn);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregarReservasPendentes());
        rodape.add(btnAtualizar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        rodape.add(btnFechar);

        add(rodape, BorderLayout.SOUTH);
    }

    private void carregarReservasPendentes() {
        try {
            modelo.setRowCount(0);
            List<Reserva> lista = reservaController.listarTodas();
            for (Reserva r : lista) {
                if (r.getEstadoReserva() == EstadoReserva.PENDENTE) {
                    modelo.addRow(new Object[]{
                            r.getIdReserva(),
                            r.getCliente().getNome(),
                            r.getCliente().getDocumentoIdentificacao(),
                            r.getQuarto().getNumeroQuarto(),
                            r.getCheckIn(),
                            r.getCheckOut()
                    });
                }
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar reservas: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarCheckIn() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva na tabela.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idReserva = (Integer) modelo.getValueAt(linha, 0);

        int resposta = JOptionPane.showConfirmDialog(this,
                "Confirmar check-in da reserva " + idReserva + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resposta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Hospedagem h = checkInController.realizar(idReserva);
            JOptionPane.showMessageDialog(this,
                    "Check-in realizado.\nQuarto " + h.getReserva().getQuarto().getNumeroQuarto()
                            + " agora está OCUPADO.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarReservasPendentes();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
