package View;

import Controller.ServicoController;
import Model.Servico;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.GridLayout;

public class ConsumoFrame extends JFrame {

    private final ServicoController servicoController;

    private JTextField campoNumeroQuarto;
    private JComboBox<String> comboTipo;
    private JTextField campoPrecoBase;

    private JPanel painelCamposExtra;
    private CardLayout cardLayout;

    private JTextField campoExtra1;
    private JLabel labelExtra1;

    public ConsumoFrame() {
        this.servicoController = new ServicoController();
        setTitle("Adicionar Consumo");
        setSize(440, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        construir();
    }

    private void construir() {
        JPanel principal = new JPanel(new GridLayout(7, 1, 8, 8));
        principal.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel linha1 = new JPanel(new GridLayout(1, 2, 8, 8));
        linha1.add(new JLabel("Número do quarto:"));
        campoNumeroQuarto = new JTextField();
        linha1.add(campoNumeroQuarto);
        principal.add(linha1);

        JPanel linha2 = new JPanel(new GridLayout(1, 2, 8, 8));
        linha2.add(new JLabel("Tipo de serviço:"));
        comboTipo = new JComboBox<>(new String[]{"Restaurante", "Lavandaria", "Transporte", "Evento"});
        comboTipo.addActionListener(e -> atualizarCamposExtra());
        linha2.add(comboTipo);
        principal.add(linha2);

        JPanel linha3 = new JPanel(new GridLayout(1, 2, 8, 8));
        linha3.add(new JLabel("Preço base:"));
        campoPrecoBase = new JTextField();
        linha3.add(campoPrecoBase);
        principal.add(linha3);

        cardLayout = new CardLayout();
        painelCamposExtra = new JPanel(cardLayout);

        painelCamposExtra.add(criarCartao("Tipo de refeição:"), "Restaurante");
        painelCamposExtra.add(criarCartao("Quantidade de peças:"), "Lavandaria");
        painelCamposExtra.add(criarCartao("Distância em km:"), "Transporte");
        painelCamposExtra.add(criarCartao("Número de dias:"), "Evento");

        principal.add(painelCamposExtra);

        JButton btnConfirmar = new JButton("Confirmar Consumo");
        btnConfirmar.addActionListener(e -> registar());
        principal.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        principal.add(btnCancelar);

        add(principal);
        atualizarCamposExtra();
    }

    private JPanel criarCartao(String labelTexto) {
        JPanel p = new JPanel(new GridLayout(1, 2, 8, 8));
        p.add(new JLabel(labelTexto));
        JTextField campo = new JTextField();
        p.add(campo);

        if (labelExtra1 == null) {
            labelExtra1 = new JLabel(labelTexto);
            campoExtra1 = campo;
        }
        return p;
    }

    private void atualizarCamposExtra() {
        String tipo = (String) comboTipo.getSelectedItem();
        if (tipo != null) {
            cardLayout.show(painelCamposExtra, tipo);
        }
    }

    private JTextField extrairCampo(String tipo) {
        java.awt.Component[] cartoes = painelCamposExtra.getComponents();
        for (java.awt.Component c : cartoes) {
            if (c.isVisible() && c instanceof JPanel) {
                java.awt.Component[] filhos = ((JPanel) c).getComponents();
                for (java.awt.Component f : filhos) {
                    if (f instanceof JTextField) {
                        return (JTextField) f;
                    }
                }
            }
        }
        return null;
    }

    private void registar() {
        try {
            int numeroQuarto = Integer.parseInt(campoNumeroQuarto.getText().trim());
            double precoBase = Double.parseDouble(campoPrecoBase.getText().trim());
            String tipo = (String) comboTipo.getSelectedItem();

            JTextField campoExtra = extrairCampo(tipo);
            if (campoExtra == null) {
                throw new IllegalArgumentException("Preencha o campo adicional.");
            }
            String textoExtra = campoExtra.getText().trim();
            if (textoExtra.isEmpty()) {
                throw new IllegalArgumentException("Preencha o campo adicional.");
            }

            Servico s;
            if ("Restaurante".equals(tipo)) {
                s = servicoController.adicionarRestaurante(numeroQuarto, textoExtra, precoBase);
            } else if ("Lavandaria".equals(tipo)) {
                int pecas = Integer.parseInt(textoExtra);
                s = servicoController.adicionarLavandaria(numeroQuarto, pecas, precoBase);
            } else if ("Transporte".equals(tipo)) {
                double km = Double.parseDouble(textoExtra);
                s = servicoController.adicionarTransporte(numeroQuarto, km, precoBase);
            } else {
                int dias = Integer.parseInt(textoExtra);
                s = servicoController.adicionarEvento(numeroQuarto, dias, precoBase);
            }

            JOptionPane.showMessageDialog(this,
                    "Consumo registado. Total: " + s.calcularServico(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor numérico inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}