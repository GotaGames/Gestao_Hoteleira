package View;

import Controller.QuartoController;
import Controller.ReservaController;
import Model.Enums.EstadoQuarto;
import Model.Enums.EstadoReserva;
import Model.Funcionario;
import Model.Quarto;
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
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

public class MenuFuncionarioFrame extends JFrame {

    private final Funcionario funcionario;
    private final QuartoController quartoController;
    private final ReservaController reservaController;

    private JLabel labelDisponiveis;
    private JLabel labelReservados;
    private JLabel labelOcupados;
    private JLabel labelChegadas;
    private JLabel labelSaidas;

    private DefaultTableModel modeloChegadas;

    public MenuFuncionarioFrame(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.quartoController = new QuartoController();
        this.reservaController = new ReservaController();

        setTitle("Sistema de Gestão Hoteleira - " + funcionario.getNome());
        setSize(1000, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        atualizarDashboard();
    }

    private void construir() {
        JPanel topo = new JPanel(new GridLayout(2, 3, 10, 10));
        topo.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 10, 15));

        labelDisponiveis = new JLabel("Disponíveis: -", JLabel.CENTER);
        labelReservados = new JLabel("Reservados: -", JLabel.CENTER);
        labelOcupados = new JLabel("Ocupados: -", JLabel.CENTER);
        labelChegadas = new JLabel("Chegadas hoje: -", JLabel.CENTER);
        labelSaidas = new JLabel("Saídas hoje: -", JLabel.CENTER);

        topo.add(labelDisponiveis);
        topo.add(labelReservados);
        topo.add(labelOcupados);
        topo.add(labelChegadas);
        topo.add(labelSaidas);
        topo.add(new JLabel(""));

        add(topo, BorderLayout.NORTH);

        String nivel = funcionario.getNivelPermissao();

        JPanel botoes = new JPanel(new GridLayout(0, 1, 6, 6));
        botoes.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton btnNovaReserva = new JButton("Nova Reserva");
        btnNovaReserva.addActionListener(e -> new ReservaFrame().setVisible(true));
        botoes.add(btnNovaReserva);

        JButton btnCheckIn = new JButton("Check-in");
        btnCheckIn.addActionListener(e -> new CheckInFrame().setVisible(true));
        botoes.add(btnCheckIn);

        JButton btnConsumo = new JButton("Adicionar Consumo");
        btnConsumo.addActionListener(e -> new ConsumoFrame().setVisible(true));
        botoes.add(btnConsumo);

        JButton btnCheckOut = new JButton("Check-out");
        btnCheckOut.addActionListener(e -> new CheckOutFrame().setVisible(true));
        botoes.add(btnCheckOut);

        JButton btnClientes = new JButton("Clientes");
        btnClientes.addActionListener(e -> new ClientesFrame().setVisible(true));
        botoes.add(btnClientes);

        JButton btnHistorico = new JButton("Histórico de Quartos");
        btnHistorico.addActionListener(e -> new HistoricoQuartoFrame().setVisible(true));
        botoes.add(btnHistorico);

        if ("ADMIN".equalsIgnoreCase(nivel) || "RECEPCAO".equalsIgnoreCase(nivel)) {
            JButton btnPagamentos = new JButton("Pagamentos");
            btnPagamentos.addActionListener(e -> new PagamentoFrame().setVisible(true));
            botoes.add(btnPagamentos);

            JButton btnQuartos = new JButton("Gerir Quartos");
            btnQuartos.addActionListener(e -> new ListarQuartosFrame().setVisible(true));
            botoes.add(btnQuartos);
        }

        if ("ADMIN".equalsIgnoreCase(nivel)) {
            JButton btnRelatorio = new JButton("Relatório Financeiro");
            btnRelatorio.addActionListener(e -> new RelatorioFinanceiroFrame().setVisible(true));
            botoes.add(btnRelatorio);

            JButton btnFuncionarios = new JButton("Gerir Funcionários");
            btnFuncionarios.addActionListener(e -> new GestaoFuncionarioFrame().setVisible(true));
            botoes.add(btnFuncionarios);
        }

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarDashboard());
        botoes.add(btnAtualizar);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        botoes.add(btnSair);

        add(botoes, BorderLayout.WEST);

        modeloChegadas = new DefaultTableModel(new Object[]{"Reserva", "Quarto", "Cliente", "Check-in", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabela = new JTable(modeloChegadas);
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(javax.swing.BorderFactory.createTitledBorder("Reservas pendentes"));
        painelTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(painelTabela, BorderLayout.CENTER);
    }

    private void atualizarDashboard() {
        try {
            List<Quarto> quartos = quartoController.listarTodos();
            int disponiveis = 0, reservados = 0, ocupados = 0;
            for (Quarto q : quartos) {
                if (q.getEstadoQuarto() == EstadoQuarto.DISPONIVEL) disponiveis++;
                else if (q.getEstadoQuarto() == EstadoQuarto.RESERVADO) reservados++;
                else if (q.getEstadoQuarto() == EstadoQuarto.OCUPADO) ocupados++;
            }
            labelDisponiveis.setText("Disponíveis: " + disponiveis);
            labelReservados.setText("Reservados: " + reservados);
            labelOcupados.setText("Ocupados: " + ocupados);

            List<Reserva> pendentes = reservaController.listarTodas();
            int chegadas = 0;
            int saidas = 0;
            LocalDate hoje = LocalDate.now();

            modeloChegadas.setRowCount(0);
            for (Reserva r : pendentes) {
                if (r.getEstadoReserva() == EstadoReserva.PENDENTE) {
                    if (hoje.equals(r.getCheckIn())) chegadas++;
                    if (hoje.equals(r.getCheckOut())) saidas++;
                    modeloChegadas.addRow(new Object[]{
                            r.getIdReserva(),
                            r.getQuarto().getNumeroQuarto(),
                            r.getCliente().getNome(),
                            r.getCheckIn(),
                            r.getEstadoReserva()
                    });
                }
            }
            labelChegadas.setText("Chegadas hoje: " + chegadas);
            labelSaidas.setText("Saídas hoje: " + saidas);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar dashboard: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
