package View;

import Controller.GestaoFuncionarioController;
import Model.Funcionario;

import javax.swing.JButton;
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

public class GestaoFuncionarioFrame extends JFrame {

    private final GestaoFuncionarioController controller;
    private DefaultTableModel modelo;
    private JTable tabela;

    private JTextField campoNome;
    private JTextField campoContacto;
    private JTextField campoEndereco;
    private JTextField campoSexo;
    private JTextField campoDocumento;
    private JTextField campoCargo;
    private JTextField campoHorario;
    private JTextField campoPermissao;
    private JTextField campoSalario;

    public GestaoFuncionarioFrame() {
        this.controller = new GestaoFuncionarioController();
        setTitle("Gestão de Funcionários");
        setSize(880, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregar();
    }

    private void construir() {
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Documento", "Cargo", "Permissão", "Salário"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelForm = new JPanel(new GridLayout(10, 2, 6, 6));
        painelForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelForm.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelForm.add(campoNome);

        painelForm.add(new JLabel("Contacto:"));
        campoContacto = new JTextField();
        painelForm.add(campoContacto);

        painelForm.add(new JLabel("Endereço:"));
        campoEndereco = new JTextField();
        painelForm.add(campoEndereco);

        painelForm.add(new JLabel("Sexo:"));
        campoSexo = new JTextField();
        painelForm.add(campoSexo);

        painelForm.add(new JLabel("Documento:"));
        campoDocumento = new JTextField();
        painelForm.add(campoDocumento);

        painelForm.add(new JLabel("Cargo:"));
        campoCargo = new JTextField();
        painelForm.add(campoCargo);

        painelForm.add(new JLabel("Horário (horas):"));
        campoHorario = new JTextField();
        painelForm.add(campoHorario);

        painelForm.add(new JLabel("Nível de permissão:"));
        campoPermissao = new JTextField();
        painelForm.add(campoPermissao);

        painelForm.add(new JLabel("Salário:"));
        campoSalario = new JTextField();
        painelForm.add(campoSalario);

        JButton btnCriar = new JButton("Adicionar");
        btnCriar.addActionListener(e -> criar());
        painelForm.add(btnCriar);

        JButton btnRemover = new JButton("Remover selecionado");
        btnRemover.addActionListener(e -> remover());
        painelForm.add(btnRemover);

        add(painelForm, BorderLayout.EAST);
    }

    private void criar() {
        try {
            String nome = campoNome.getText().trim();
            String contacto = campoContacto.getText().trim();
            String endereco = campoEndereco.getText().trim();
            String sexo = campoSexo.getText().trim();
            String documento = campoDocumento.getText().trim();
            String cargo = campoCargo.getText().trim();
            String permissao = campoPermissao.getText().trim();
            int horario = Integer.parseInt(campoHorario.getText().trim());
            double salario = Double.parseDouble(campoSalario.getText().trim());

            controller.criar(nome, contacto, endereco, sexo, documento, cargo, horario, permissao, salario);

            limpar();
            carregar();
            JOptionPane.showMessageDialog(this, "Funcionário adicionado.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Horário e salário devem ser números.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void remover() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (Integer) modelo.getValueAt(linha, 0);
        int resposta = JOptionPane.showConfirmDialog(this, "Remover o funcionário com ID " + id + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            try {
                controller.remover(id);
                carregar();
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregar() {
        modelo.setRowCount(0);
        List<Funcionario> lista = controller.listarTodos();
        for (Funcionario f : lista) {
            modelo.addRow(new Object[]{
                    f.getIdPessoa(),
                    f.getNome(),
                    f.getDocumentoIdentificacao(),
                    f.getCargo(),
                    f.getNivelPermissao(),
                    f.getSalario()
            });
        }
    }

    private void limpar() {
        campoNome.setText("");
        campoContacto.setText("");
        campoEndereco.setText("");
        campoSexo.setText("");
        campoDocumento.setText("");
        campoCargo.setText("");
        campoHorario.setText("");
        campoPermissao.setText("");
        campoSalario.setText("");
    }
}