package View;

import Controller.HistoricoQuartoController;
import Controller.QuartoController;
import Model.HistoricoQuarto;
import Model.Quarto;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class HistoricoQuartoFrame extends JFrame {

    private final HistoricoQuartoController historicoController;
    private final QuartoController quartoController;

    private JComboBox<Integer> comboQuarto;
    private JTable tabela;
    private DefaultTableModel modelo;

    public HistoricoQuartoFrame() {
        this.historicoController = new HistoricoQuartoController();
        this.quartoController = new QuartoController();

        setTitle("Histórico de Quartos");
        setSize(820, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregarQuartos();
    }

    private void construir() {
        JPanel topo = new JPanel(new GridLayout(1, 4, 8, 8));
        topo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 5, 10));

        topo.add(new JLabel("Quarto:"));
        comboQuarto = new JComboBox<>();
        topo.add(comboQuarto);

        JButton btnVer = new JButton("Ver histórico");
        btnVer.addActionListener(e -> carregarHistorico());
        topo.add(btnVer);

        JButton btnTodos = new JButton("Ver todos (últimos 500)");
        btnTodos.addActionListener(e -> carregarTodos());
        topo.add(btnTodos);

        add(topo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Quarto", "Estado anterior", "Estado novo", "Data", "Reserva", "Observação"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel();
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        rodape.add(btnFechar);
        add(rodape, BorderLayout.SOUTH);
    }

    private void carregarQuartos() {
        comboQuarto.removeAllItems();
        List<Quarto> lista = quartoController.listarTodos();
        for (Quarto q : lista) {
            comboQuarto.addItem(q.getNumeroQuarto());
        }
    }

    private void carregarHistorico() {
        Object item = comboQuarto.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Selecione um quarto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int numero = (Integer) item;
        modelo.setRowCount(0);
        List<HistoricoQuarto> lista = historicoController.listarPorQuarto(numero);
        adicionarLinhas(lista);
    }

    private void carregarTodos() {
        modelo.setRowCount(0);
        List<HistoricoQuarto> lista = historicoController.listarTodos();
        adicionarLinhas(lista);
    }

    private void adicionarLinhas(List<HistoricoQuarto> lista) {
        for (HistoricoQuarto h : lista) {
            modelo.addRow(new Object[]{
                    h.getIdHistorico(),
                    h.getNumeroQuarto(),
                    h.getEstadoAnterior(),
                    h.getEstadoNovo(),
                    h.getDataAlteracao(),
                    h.getIdReserva() != null ? h.getIdReserva() : "",
                    h.getObservacao()
            });
        }
    }
}
