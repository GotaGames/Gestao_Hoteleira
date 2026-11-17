package View;

import Controller.ClienteController;
import Model.Cliente;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class ClientesFrame extends JFrame {

    private final ClienteController clienteController;
    private JTextField campoPesquisa;
    private JTable tabela;
    private DefaultTableModel modelo;

    public ClientesFrame() {
        this.clienteController = new ClienteController();

        setTitle("Clientes");
        setSize(820, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregar();
    }

    private void construir() {
        JPanel topo = new JPanel(new GridLayout(1, 4, 8, 8));
        topo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 5, 10));

        topo.add(new JLabel("Pesquisar (nome ou documento):"));
        campoPesquisa = new JTextField();
        topo.add(campoPesquisa);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> filtrar());
        topo.add(btnFiltrar);

        JButton btnLimpar = new JButton("Limpar filtro");
        btnLimpar.addActionListener(e -> {
            campoPesquisa.setText("");
            carregar();
        });
        topo.add(btnLimpar);

        add(topo, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Documento", "Contacto", "Endereço", "Sexo", "Nacionalidade"}, 0) {
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
    }

    private void carregar() {
        modelo.setRowCount(0);
        List<Cliente> lista = clienteController.listarTodos();
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{
                    c.getIdPessoa(),
                    c.getNome(),
                    c.getDocumentoIdentificacao(),
                    c.getContacto(),
                    c.getEndereco(),
                    c.getSexo(),
                    c.getNacionalidade()
            });
        }
    }

    private void filtrar() {
        String filtro = campoPesquisa.getText().trim().toLowerCase();
        modelo.setRowCount(0);
        List<Cliente> lista = clienteController.listarTodos();
        for (Cliente c : lista) {
            boolean bate = filtro.isEmpty()
                    || (c.getNome() != null && c.getNome().toLowerCase().contains(filtro))
                    || (c.getDocumentoIdentificacao() != null && c.getDocumentoIdentificacao().toLowerCase().contains(filtro));
            if (bate) {
                modelo.addRow(new Object[]{
                        c.getIdPessoa(),
                        c.getNome(),
                        c.getDocumentoIdentificacao(),
                        c.getContacto(),
                        c.getEndereco(),
                        c.getSexo(),
                        c.getNacionalidade()
                });
            }
        }
    }
}
