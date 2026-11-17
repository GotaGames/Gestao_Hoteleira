package View;

import Controller.QuartoController;
import Model.Quarto;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.util.List;

public class ListarQuartosFrame extends JFrame {

    private final QuartoController quartoController;
    private JTable tabela;
    private DefaultTableModel modelo;

    public ListarQuartosFrame() {
        this.quartoController = new QuartoController();
        setTitle("Quartos Disponíveis");
        setSize(560, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modelo = new DefaultTableModel(new Object[]{"Número", "Tipo", "Preço Diário", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);

        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregar());
        rodape.add(btnAtualizar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        rodape.add(btnFechar);

        add(rodape, BorderLayout.SOUTH);

        carregar();
    }

    private void carregar() {
        try {
            modelo.setRowCount(0);
            List<Quarto> lista = quartoController.listarDisponiveis();
            for (Quarto q : lista) {
                modelo.addRow(new Object[]{
                        q.getNumeroQuarto(),
                        q.getTipoQuarto(),
                        q.getPrecoDiario(),
                        q.getEstadoQuarto()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar quartos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}