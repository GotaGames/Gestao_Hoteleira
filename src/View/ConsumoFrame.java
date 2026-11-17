package View;

import Controller.HospedagemController;
import Controller.ServicoController;
import Model.Hospedagem;
import Model.Servico;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.util.List;

public class ConsumoFrame extends JFrame {

    private final ServicoController servicoController;
    private final HospedagemController hospedagemController;

    private JComboBox<String> comboQuarto;
    private JComboBox<String> comboTipo;
    private JTextField campoPrecoBase;

    private JPanel painelExtraRestaurante;
    private JPanel painelExtraLavandaria;
    private JPanel painelExtraTransporte;
    private JPanel painelExtraEvento;

    private JTextField campoRestaurante;
    private JTextField campoLavandaria;
    private JTextField campoTransporte;
    private JTextField campoEvento;

    private JPanel painelCamposExtra;

    public ConsumoFrame() {
        this.servicoController = new ServicoController();
        this.hospedagemController = new HospedagemController();

        setTitle("Adicionar Consumo");
        setSize(480, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        construir();
        carregarQuartosOcupados();
        atualizarCamposExtra();
    }

    private void construir() {
        JPanel principal = new JPanel(new GridLayout(7, 1, 8, 8));
        principal.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel linhaQuarto = new JPanel(new GridLayout(1, 2, 8, 8));
        linhaQuarto.add(new JLabel("Quarto ocupado:"));
        comboQuarto = new JComboBox<>();
        linhaQuarto.add(comboQuarto);
        principal.add(linhaQuarto);

        JPanel linhaTipo = new JPanel(new GridLayout(1, 2, 8, 8));
        linhaTipo.add(new JLabel("Tipo de serviço:"));
        comboTipo = new JComboBox<>(new String[]{"Restaurante", "Lavandaria", "Transporte", "Evento"});
        comboTipo.addActionListener(e -> atualizarCamposExtra());
        linhaTipo.add(comboTipo);
        principal.add(linhaTipo);

        JPanel linhaPreco = new JPanel(new GridLayout(1, 2, 8, 8));
        linhaPreco.add(new JLabel("Preço base:"));
        campoPrecoBase = new JTextField();
        linhaPreco.add(campoPrecoBase);
        principal.add(linhaPreco);

        painelCamposExtra = new JPanel(new GridLayout(1, 1));

        painelExtraRestaurante = new JPanel(new GridLayout(1, 2, 8, 8));
        painelExtraRestaurante.add(new JLabel("Tipo de refeição:"));
        campoRestaurante = new JTextField();
        painelExtraRestaurante.add(campoRestaurante);

        painelExtraLavandaria = new JPanel(new GridLayout(1, 2, 8, 8));
        painelExtraLavandaria.add(new JLabel("Quantidade de peças:"));
        campoLavandaria = new JTextField();
        painelExtraLavandaria.add(campoLavandaria);

        painelExtraTransporte = new JPanel(new GridLayout(1, 2, 8, 8));
        painelExtraTransporte.add(new JLabel("Distância (km):"));
        campoTransporte = new JTextField();
        painelExtraTransporte.add(campoTransporte);

        painelExtraEvento = new JPanel(new GridLayout(1, 2, 8, 8));
        painelExtraEvento.add(new JLabel("Número de dias:"));
        campoEvento = new JTextField();
        painelExtraEvento.add(campoEvento);

        principal.add(painelCamposExtra);

        JButton btnConfirmar = new JButton("Registar Consumo");
        btnConfirmar.addActionListener(e -> registar());
        principal.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        principal.add(btnCancelar);

        add(principal);
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

    private void atualizarCamposExtra() {
        painelCamposExtra.removeAll();
        String tipo = (String) comboTipo.getSelectedItem();
        if ("Restaurante".equals(tipo)) {
            painelCamposExtra.add(painelExtraRestaurante);
        } else if ("Lavandaria".equals(tipo)) {
            painelCamposExtra.add(painelExtraLavandaria);
        } else if ("Transporte".equals(tipo)) {
            painelCamposExtra.add(painelExtraTransporte);
        } else if ("Evento".equals(tipo)) {
            painelCamposExtra.add(painelExtraEvento);
        }
        painelCamposExtra.revalidate();
        painelCamposExtra.repaint();
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

    private void registar() {
        try {
            int numeroQuarto = extrairNumeroQuarto();
            double precoBase = Double.parseDouble(campoPrecoBase.getText().trim());
            String tipo = (String) comboTipo.getSelectedItem();
            if (tipo == null) {
                throw new IllegalStateException("Selecione o tipo de serviço.");
            }

            Servico s;
            if ("Restaurante".equals(tipo)) {
                String refeicao = campoRestaurante.getText().trim();
                if (refeicao.isEmpty()) {
                    throw new IllegalArgumentException("Indique o tipo de refeição.");
                }
                s = servicoController.adicionarRestaurante(numeroQuarto, refeicao, precoBase);
            } else if ("Lavandaria".equals(tipo)) {
                int pecas = Integer.parseInt(campoLavandaria.getText().trim());
                s = servicoController.adicionarLavandaria(numeroQuarto, pecas, precoBase);
            } else if ("Transporte".equals(tipo)) {
                double km = Double.parseDouble(campoTransporte.getText().trim());
                s = servicoController.adicionarTransporte(numeroQuarto, km, precoBase);
            } else {
                int dias = Integer.parseInt(campoEvento.getText().trim());
                s = servicoController.adicionarEvento(numeroQuarto, dias, precoBase);
            }

            JOptionPane.showMessageDialog(this,
                    "Consumo registado.\nTotal: " + s.calcularServico(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            campoPrecoBase.setText("");
            campoRestaurante.setText("");
            campoLavandaria.setText("");
            campoTransporte.setText("");
            campoEvento.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor numérico inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
