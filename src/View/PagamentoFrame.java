package View;

import java.awt.Dimension;
import Controller.HospedagemController;
import Controller.PagamentoController;
import Model.Enums.MetodoPagamento;
import Model.Hospedagem;
import Model.Pagamento;
import Util.DateUtil;
import com.toedter.calendar.JDateChooser;

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
import java.time.LocalDate;
import java.util.List;

public class PagamentoFrame extends JFrame {

    private final HospedagemController hospedagemController;
    private final PagamentoController pagamentoController;

    private JComboBox<String> comboQuarto;
    private JLabel labelTotal;
    private JLabel labelPago;
    private JLabel labelSaldo;

    private JTextField campoValor;
    private JComboBox<MetodoPagamento> comboMetodo;
    private JDateChooser dataPagamento;

    private JTable tabela;
    private DefaultTableModel modelo;

    private Hospedagem hospedagemAtual;

    public PagamentoFrame() {
        this.hospedagemController = new HospedagemController();
        this.pagamentoController = new PagamentoController();

        setTitle("Pagamentos");
        setSize(820, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregarQuartosOcupados();
    }

    private void construir() {
        JPanel painelTopo = new JPanel(new GridLayout(4, 2, 8, 8));
        painelTopo.setBorder(javax.swing.BorderFactory.createTitledBorder("Quarto"));

        painelTopo.add(new JLabel("Quarto ocupado:"));
        comboQuarto = new JComboBox<>();
        comboQuarto.addActionListener(e -> selecionarQuarto());
        painelTopo.add(comboQuarto);

        labelTotal = new JLabel("Total fatura: -");
        painelTopo.add(labelTotal);

        labelPago = new JLabel("Total pago: -");
        painelTopo.add(labelPago);

        labelSaldo = new JLabel("Saldo em falta: -");
        painelTopo.add(labelSaldo);

        painelTopo.add(new JLabel(""));

        JPanel painelForm = new JPanel(new GridLayout(3, 4, 8, 8));
        painelForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Registar pagamento"));

        painelForm.add(new JLabel("Data:"));
        dataPagamento = new JDateChooser();
        dataPagamento.setDateFormatString("dd/MM/yyyy");
        dataPagamento.setDate(new java.util.Date());
        painelForm.add(dataPagamento);

        painelForm.add(new JLabel("Valor:"));
        campoValor = new JTextField();
        painelForm.add(campoValor);

        painelForm.add(new JLabel("Método:"));
        comboMetodo = new JComboBox<>(MetodoPagamento.values());
        painelForm.add(comboMetodo);

        painelForm.add(new JLabel(""));
        painelForm.add(new JLabel(""));

        JButton btnRegistar = new JButton("Registar pagamento");
        btnRegistar.setPreferredSize(new Dimension(160, 32));
        btnRegistar.addActionListener(e -> registarPagamento());
        painelForm.add(btnRegistar);

        JButton btnPagarTotal = new JButton("Pagar saldo total");
        btnPagarTotal.setPreferredSize(new Dimension(160, 32));
        btnPagarTotal.addActionListener(e -> pagarSaldoTotal());
        painelForm.add(btnPagarTotal);

        JPanel painelNorte = new JPanel(new BorderLayout(5, 5));
        painelNorte.add(painelTopo, BorderLayout.NORTH);
        painelNorte.add(painelForm, BorderLayout.CENTER);

        add(painelNorte, BorderLayout.NORTH);

        modelo = new DefaultTableModel(new Object[]{"ID", "Data", "Valor", "Método", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(javax.swing.BorderFactory.createTitledBorder("Pagamentos registados"));
        painelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel painelSul = new JPanel(new BorderLayout());
        painelSul.add(painelTabela, BorderLayout.CENTER);

        JPanel rodape = new JPanel();
        JButton btnFechar = new JButton("Fechar");
        btnFechar.setPreferredSize(new Dimension(120, 32));
        btnFechar.addActionListener(e -> dispose());
        rodape.add(btnFechar);
        painelSul.add(rodape, BorderLayout.SOUTH);

        add(painelSul, BorderLayout.CENTER);
    }


    private void carregarQuartosOcupados() {
        comboQuarto.removeAllItems();
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
            hospedagemAtual = null;
            labelTotal.setText("Total fatura: -");
            labelPago.setText("Total pago: -");
            labelSaldo.setText("Saldo em falta: -");
        } else {
            selecionarQuarto();
        }
    }

    private void selecionarQuarto() {
        Object item = comboQuarto.getSelectedItem();
        if (item == null) {
            hospedagemAtual = null;
            return;
        }
        String texto = item.toString();
        int espaco = texto.indexOf(" - ");
        if (espaco < 0) return;
        int numeroQuarto = Integer.parseInt(texto.substring(0, espaco).trim());

        List<Hospedagem> ativas = hospedagemController.listarAtivas();
        hospedagemAtual = null;
        for (Hospedagem h : ativas) {
            if (h.getReserva().getQuarto().getNumeroQuarto() == numeroQuarto) {
                hospedagemAtual = h;
                break;
            }
        }
        if (hospedagemAtual == null) return;

        atualizarSaldos();
        carregarPagamentos();
    }

    private void atualizarSaldos() {
        if (hospedagemAtual == null) return;
        try {
            double pago = pagamentoController.totalPago(hospedagemAtual);
            double saldo = pagamentoController.saldoDevido(hospedagemAtual);
            double total = pago + saldo;

            labelTotal.setText(String.format("Total fatura: %.2f", total));
            labelPago.setText(String.format("Total pago: %.2f", pago));
            labelSaldo.setText(String.format("Saldo em falta: %.2f", saldo));
        } catch (RuntimeException ex) {
            labelTotal.setText("Total fatura: (erro)");
            labelPago.setText("Total pago: (erro)");
            labelSaldo.setText("Saldo em falta: (erro)");
        }
    }

    private void carregarPagamentos() {
        modelo.setRowCount(0);
        if (hospedagemAtual == null) return;
        List<Pagamento> lista = pagamentoController.listarPorHospedagem(hospedagemAtual);
        for (Pagamento p : lista) {
            modelo.addRow(new Object[]{
                    p.getIdPagamento(),
                    p.getDataPagamento(),
                    p.getValor(),
                    p.getMetodo(),
                    p.getEstado()
            });
        }
    }

    private void registarPagamento() {
        if (hospedagemAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um quarto ocupado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            LocalDate data = DateUtil.toLocalDate(dataPagamento.getDate());
            if (data == null) {
                throw new IllegalArgumentException("Selecione a data do pagamento.");
            }
            double valor = Double.parseDouble(campoValor.getText().trim());
            MetodoPagamento metodo = (MetodoPagamento) comboMetodo.getSelectedItem();

            pagamentoController.registar(hospedagemAtual, data, valor, metodo);
            campoValor.setText("");
            atualizarSaldos();
            carregarPagamentos();
            JOptionPane.showMessageDialog(this, "Pagamento registado.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pagarSaldoTotal() {
        if (hospedagemAtual == null) {
            JOptionPane.showMessageDialog(this, "Selecione um quarto ocupado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double saldo = pagamentoController.saldoDevido(hospedagemAtual);
            if (saldo <= 0.001) {
                JOptionPane.showMessageDialog(this, "Não há saldo em falta.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            LocalDate data = DateUtil.toLocalDate(dataPagamento.getDate());
            if (data == null) {
                data = LocalDate.now();
            }
            MetodoPagamento metodo = (MetodoPagamento) comboMetodo.getSelectedItem();
            pagamentoController.registar(hospedagemAtual, data, saldo, metodo);
            atualizarSaldos();
            carregarPagamentos();
            JOptionPane.showMessageDialog(this, "Saldo liquidado.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
