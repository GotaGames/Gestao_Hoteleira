package View;

import Controller.ClienteController;
import Controller.QuartoController;
import Controller.ReservaController;
import Model.Cliente;
import Model.Quarto;
import Model.Reserva;
import Util.DateUtil;
import com.toedter.calendar.JDateChooser;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

public class ReservaFrame extends JFrame {

    private final ClienteController clienteController;
    private final QuartoController quartoController;
    private final ReservaController reservaController;

    private JTextField campoPesquisaDocumento;
    private JTextField campoDocumento;
    private JTextField campoNome;
    private JTextField campoContacto;
    private JTextField campoEndereco;
    private JTextField campoSexo;
    private JTextField campoNacionalidade;

    private JComboBox<Integer> comboQuarto;
    private JDateChooser dataCheckIn;
    private JDateChooser dataCheckOut;

    private Cliente clienteEncontrado;
    private boolean modoEdicaoCliente;

    public ReservaFrame() {
        this.clienteController = new ClienteController();
        this.quartoController = new QuartoController();
        this.reservaController = new ReservaController();
        this.modoEdicaoCliente = true;

        setTitle("Nova Reserva");
        setSize(560, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregarQuartosDisponiveis();
    }

    private void construir() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel painelPesquisa = new JPanel(new GridLayout(1, 3, 10, 10));
        painelPesquisa.setBorder(javax.swing.BorderFactory.createTitledBorder("Pesquisar cliente"));
        painelPesquisa.add(new JLabel("Documento:"));
        campoPesquisaDocumento = new JTextField();
        painelPesquisa.add(campoPesquisaDocumento);
        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(e -> pesquisarCliente());
        painelPesquisa.add(btnPesquisar);

        principal.add(painelPesquisa, BorderLayout.NORTH);

        JPanel painelCliente = new JPanel(new GridLayout(6, 2, 8, 8));
        painelCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados do cliente"));

        painelCliente.add(new JLabel("Documento:"));
        campoDocumento = new JTextField();
        painelCliente.add(campoDocumento);

        painelCliente.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painelCliente.add(campoNome);

        painelCliente.add(new JLabel("Contacto:"));
        campoContacto = new JTextField();
        painelCliente.add(campoContacto);

        painelCliente.add(new JLabel("Endereço:"));
        campoEndereco = new JTextField();
        painelCliente.add(campoEndereco);

        painelCliente.add(new JLabel("Sexo:"));
        campoSexo = new JTextField();
        painelCliente.add(campoSexo);

        painelCliente.add(new JLabel("Nacionalidade:"));
        campoNacionalidade = new JTextField();
        painelCliente.add(campoNacionalidade);

        principal.add(painelCliente, BorderLayout.CENTER);

        JPanel painelReserva = new JPanel(new GridLayout(3, 2, 8, 8));
        painelReserva.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da reserva"));

        painelReserva.add(new JLabel("Quarto disponível:"));
        comboQuarto = new JComboBox<>();
        painelReserva.add(comboQuarto);

        painelReserva.add(new JLabel("Data check-in:"));
        dataCheckIn = new JDateChooser();
        dataCheckIn.setDateFormatString("dd/MM/yyyy");
        painelReserva.add(dataCheckIn);

        painelReserva.add(new JLabel("Data check-out:"));
        dataCheckOut = new JDateChooser();
        dataCheckOut.setDateFormatString("dd/MM/yyyy");
        painelReserva.add(dataCheckOut);

        JPanel painelBotoes = new JPanel(new GridLayout(1, 3, 10, 10));
        JButton btnLimparCliente = new JButton("Limpar cliente");
        btnLimparCliente.addActionListener(e -> limparCliente());
        painelBotoes.add(btnLimparCliente);

        JButton btnConfirmar = new JButton("Confirmar Reserva");
        btnConfirmar.addActionListener(e -> reservar());
        painelBotoes.add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        painelBotoes.add(btnCancelar);

        JPanel painelSul = new JPanel(new BorderLayout(5, 5));
        painelSul.add(painelReserva, BorderLayout.CENTER);
        painelSul.add(painelBotoes, BorderLayout.SOUTH);

        principal.add(painelSul, BorderLayout.SOUTH);

        add(principal);
    }

    private void carregarQuartosDisponiveis() {
        comboQuarto.removeAllItems();
        List<Quarto> lista = quartoController.listarDisponiveis();
        for (Quarto q : lista) {
            comboQuarto.addItem(q.getNumeroQuarto());
        }
        if (comboQuarto.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Não há quartos disponíveis neste momento.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void pesquisarCliente() {
        String documento = campoPesquisaDocumento.getText().trim();
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Indique o documento a pesquisar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Cliente c = clienteController.buscarPorDocumento(documento);
            clienteEncontrado = c;
            modoEdicaoCliente = false;
            preencherCamposCliente(c);
            bloquearCamposCliente(true);
        } catch (IllegalArgumentException ex) {
            clienteEncontrado = null;
            modoEdicaoCliente = true;
            limparCamposCliente();
            campoDocumento.setText(documento);
            bloquearCamposCliente(false);
            JOptionPane.showMessageDialog(this,
                    "Cliente não encontrado. Preencha os dados para criar um novo.",
                    "Novo cliente", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void preencherCamposCliente(Cliente c) {
        campoDocumento.setText(c.getDocumentoIdentificacao());
        campoNome.setText(c.getNome());
        campoContacto.setText(c.getContacto());
        campoEndereco.setText(c.getEndereco());
        campoSexo.setText(c.getSexo());
        campoNacionalidade.setText(c.getNacionalidade());
    }

    private void limparCamposCliente() {
        campoDocumento.setText("");
        campoNome.setText("");
        campoContacto.setText("");
        campoEndereco.setText("");
        campoSexo.setText("");
        campoNacionalidade.setText("");
    }

    private void bloquearCamposCliente(boolean bloquear) {
        campoDocumento.setEditable(!bloquear);
        campoNome.setEditable(!bloquear);
        campoContacto.setEditable(!bloquear);
        campoEndereco.setEditable(!bloquear);
        campoSexo.setEditable(!bloquear);
        campoNacionalidade.setEditable(!bloquear);
    }

    private void limparCliente() {
        clienteEncontrado = null;
        modoEdicaoCliente = true;
        campoPesquisaDocumento.setText("");
        limparCamposCliente();
        bloquearCamposCliente(false);
    }

    private void reservar() {
        try {
            if (comboQuarto.getItemCount() == 0) {
                throw new IllegalStateException("Não há quartos disponíveis.");
            }
            Object item = comboQuarto.getSelectedItem();
            if (item == null) {
                throw new IllegalStateException("Selecione um quarto.");
            }
            int numeroQuarto = (Integer) item;

            LocalDate checkIn = DateUtil.toLocalDate(dataCheckIn.getDate());
            LocalDate checkOut = DateUtil.toLocalDate(dataCheckOut.getDate());
            if (checkIn == null || checkOut == null) {
                throw new IllegalArgumentException("Selecione as datas de check-in e check-out.");
            }

            Cliente cliente;
            if (clienteEncontrado != null && !modoEdicaoCliente) {
                cliente = clienteEncontrado;
            } else {
                String documento = campoDocumento.getText().trim();
                String nome = campoNome.getText().trim();
                String contacto = campoContacto.getText().trim();
                String endereco = campoEndereco.getText().trim();
                String sexo = campoSexo.getText().trim();
                String nacionalidade = campoNacionalidade.getText().trim();

                if (documento.isEmpty() || nome.isEmpty()) {
                    throw new IllegalArgumentException("Documento e nome do cliente são obrigatórios.");
                }
                cliente = clienteController.criar(nome, contacto, endereco, sexo, documento, nacionalidade);
            }

            Reserva reserva = reservaController.criar(cliente.getIdPessoa(), numeroQuarto, checkIn, checkOut);

            JOptionPane.showMessageDialog(this,
                    "Reserva criada com sucesso.\nID: " + reserva.getIdReserva()
                            + "\nQuarto: " + numeroQuarto
                            + "\nCliente: " + cliente.getNome(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            limparCliente();
            dataCheckIn.setDate(null);
            dataCheckOut.setDate(null);
            carregarQuartosDisponiveis();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
