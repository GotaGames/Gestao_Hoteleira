package View;

import Controller.RelatorioController;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public class RelatorioFinanceiroFrame extends JFrame {

    private final RelatorioController relatorioController;

    private JLabel labelTotalMes;
    private JLabel labelNumeroPagamentos;
    private JLabel labelTicketMedio;
    private JTable tabela;
    private DefaultTableModel modelo;
    private PainelBarras painelBarras;

    public RelatorioFinanceiroFrame() {
        this.relatorioController = new RelatorioController();

        setTitle("Relatório Financeiro");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregar();
    }

    private void construir() {
        JPanel painelTopo = new JPanel(new GridLayout(1, 3, 10, 10));
        painelTopo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelTotalMes = new JLabel("Total mês atual: -", JLabel.CENTER);
        labelNumeroPagamentos = new JLabel("Nº pagamentos: -", JLabel.CENTER);
        labelTicketMedio = new JLabel("Ticket médio: -", JLabel.CENTER);

        painelTopo.add(labelTotalMes);
        painelTopo.add(labelNumeroPagamentos);
        painelTopo.add(labelTicketMedio);

        add(painelTopo, BorderLayout.NORTH);

        painelBarras = new PainelBarras();
        JPanel painelCentro = new JPanel(new BorderLayout());
        painelCentro.setBorder(javax.swing.BorderFactory.createTitledBorder("Total dos últimos 6 meses"));
        painelCentro.add(painelBarras, BorderLayout.CENTER);

        modelo = new DefaultTableModel(new Object[]{"Mês", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modelo);

        JPanel painelSul = new JPanel(new BorderLayout());
        painelSul.setBorder(javax.swing.BorderFactory.createTitledBorder("Resumo mensal"));
        painelSul.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel rodape = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> carregar());
        rodape.add(btnAtualizar);
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        rodape.add(btnFechar);
        painelSul.add(rodape, BorderLayout.SOUTH);

        add(painelCentro, BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);
    }

    private void carregar() {
        try {
            double total = relatorioController.totalMesAtual();
            int n = relatorioController.numeroPagamentosMesAtual();
            double ticket = relatorioController.ticketMedioMesAtual();

            labelTotalMes.setText(String.format("Total mês atual: %.2f", total));
            labelNumeroPagamentos.setText("Nº pagamentos: " + n);
            labelTicketMedio.setText(String.format("Ticket médio: %.2f", ticket));

            modelo.setRowCount(0);
            Map<YearMonth, Double> totais = relatorioController.totalUltimosMeses(6);
            for (Map.Entry<YearMonth, Double> e : totais.entrySet()) {
                modelo.addRow(new Object[]{e.getKey().toString(), e.getValue()});
            }

            painelBarras.setDados(totais);
        } catch (RuntimeException ex) {
            labelTotalMes.setText("Total mês atual: erro");
            labelNumeroPagamentos.setText("Nº pagamentos: erro");
            labelTicketMedio.setText("Ticket médio: erro");
        }
    }

    private static class PainelBarras extends JPanel {

        private Map<YearMonth, Double> dados;

        public PainelBarras() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(860, 200));
        }

        public void setDados(Map<YearMonth, Double> dados) {
            this.dados = dados;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (dados == null || dados.isEmpty()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            int largura = getWidth();
            int altura = getHeight();
            int margem = 40;
            int alturaBarra = altura - 2 * margem;
            int larguraTotal = largura - 2 * margem;
            int n = dados.size();
            int espaco = larguraTotal / n;
            int larguraCada = (int) (espaco * 0.6);

            double max = 0;
            for (double v : dados.values()) {
                if (v > max) max = v;
            }
            if (max == 0) {
                max = 1;
            }

            int i = 0;
            for (Map.Entry<YearMonth, Double> e : dados.entrySet()) {
                int x = margem + i * espaco + (espaco - larguraCada) / 2;
                int alturaAtual = (int) ((e.getValue() / max) * alturaBarra);
                int y = altura - margem - alturaAtual;

                g2.setColor(new Color(70, 130, 180));
                g2.fillRect(x, y, larguraCada, alturaAtual);
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, larguraCada, alturaAtual);
                g2.drawString(String.format("%.0f", e.getValue()), x, y - 4);
                g2.drawString(e.getKey().toString(), x, altura - margem + 15);

                i++;
            }
            g2.drawLine(margem, altura - margem, largura - margem, altura - margem);
        }
    }
}
