package View;

import Controller.QuartoController;
import Model.Enums.EstadoQuarto;
import Model.Quarto;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class ListarQuartosFrame extends JFrame {

    private final QuartoController quartoController;

    private JTable tabela;
    private DefaultTableModel modelo;

    private JTextField campoNumero;
    private JTextField campoTipo;
    private JTextField campoPreco;
    private JComboBox<EstadoQuarto> comboEstadoNovo;

    public ListarQuartosFrame() {
        this.quartoController = new QuartoController();

        setTitle("Gestão de Quartos");
        setSize(900, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregar();
    }

    private void construir() {
        modelo = new DefaultTableModel(new Object[]{"Número", "Tipo", "Preço Diário", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelLateral = new JPanel(new GridLayout(10, 2, 6, 6));
        painelLateral.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelLateral.add(new JLabel("Número:"));
        campoNumero = new JTextField();
        painelLateral.add(campoNumero);

        painelLateral.add(new JLabel("Tipo:"));
        campoTipo = new JTextField();
        painelLateral.add(campoTipo);

        painelLateral.add(new JLabel("Preço diário:"));
        campoPreco = new JTextField();
        painelLateral.add(campoPreco);

        JButton btnCriar = new JButton("Adicionar Quarto");
        btnCriar.addActionListener(e -> criarQuarto());
        painelLateral.add(btnCriar);

        painelLateral.add(new JLabel(""));
        painelLateral.add(new JLabel(""));

        painelLateral.add(new JLabel("Novo estado:"));
        comboEstadoNovo = new JComboBox<>(EstadoQuarto.values());
        painelLateral.add(comboEstadoNovo);

        JButton btnAlterarEstado = new JButton("Alterar Estado");
        btnAlterarEstado.addActionListener(e -> alterarEstado());
        painelLateral.add(btnAlterarEstado);

        painelLateral.add(new JLabel(""));
        painelLateral.add(new JLabel(""));

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregar());
        painelLateral.add(btnAtualizar);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        painelLateral.add(btnFechar);

        add(painelLateral, BorderLayout.EAST);
    }

    private void carregar() {
        try {
            modelo.setRowCount(0);
            List<Quarto> lista = quartoController.listarTodos();
            for (Quarto q : lista) {
                modelo.addRow(new Object[]{
                        q.getNumeroQuarto(),
                        q.getTipoQuarto(),
                        q.getPrecoDiario(),
                        q.getEstadoQuarto()
                });
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar quartos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarQuarto() {
        try {
            int numero = Integer.parseInt(campoNumero.getText().trim());
            String tipo = campoTipo.getText().trim();
            double preco = Double.parseDouble(campoPreco.getText().trim());

            quartoController.criar(numero, tipo, preco);
            limparFormulario();
            carregar();
            JOptionPane.showMessageDialog(this, "Quarto adicionado.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número e preço devem ser valores numéricos.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarEstado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um quarto na tabela.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int numero = (Integer) modelo.getValueAt(linha, 0);
        EstadoQuarto novoEstado = (EstadoQuarto) comboEstadoNovo.getSelectedItem();
        if (novoEstado == null) {
            JOptionPane.showMessageDialog(this, "Selecione o novo estado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            quartoController.alterarEstado(numero, novoEstado);
            carregar();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        campoNumero.setText("");
        campoTipo.setText("");
        campoPreco.setText("");
    }
}
