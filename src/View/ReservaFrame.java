package View;

import Controller.ClienteController;
import Controller.QuartoController;
import Controller.ReservaController;
import Model.Cliente;
import Model.Quarto;
import Model.Reserva;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReservaFrame extends JFrame {

    private final ClienteController clienteController;
    private final QuartoController quartoController;
    private final ReservaController reservaController;

    private JTextField campoDocumento;
    private JTextField campoNome;
    private JTextField campoContacto;
    private JTextField campoEndereco;
    private JTextField campoSexo;
    private JTextField campoNacionalidade;
    private JComboBox<Integer> comboQuarto;
    private JTextField campoCheckIn;
    private JTextField campoCheckOut;

    public ReservaFrame() {
        this.clienteController = new ClienteController();
        this.quartoController = new QuartoController();
        this.reservaController = new ReservaController();

        setTitle("Nova Reserva");
        setSize(460, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        construir();
        carregarQuartos();
    }

    private void construir() {
        JPanel painel = new JPanel(new GridLayout(10, 2, 8, 8));
        painel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        painel.add(new JLabel("Documento do cliente:"));
        campoDocumento = new JTextField();
        painel.add(campoDocumento);

        painel.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        painel.add(campoNome);

        painel.add(new JLabel("Contacto:"));
        campoContacto = new JTextField();
        painel.add(campoContacto);

        painel.add(new JLabel("Endereço:"));
        campoEndereco = new JTextField();
        painel.add(campoEndereco);

        painel.add(new JLabel("Sexo:"));
        campoSexo = new JTextField();
        painel.add(campoSexo);

        painel.add(new JLabel("Nacionalidade:"));
        campoNacionalidade = new JTextField();
        painel.add(campoNacionalidade);

        painel.add(new JLabel("Quarto:"));
        comboQuarto = new JComboBox<>();
        painel.add(comboQuarto);

        painel.add(new JLabel("Data check-in (AAAA-MM-DD):"));
        campoCheckIn = new JTextField();
        painel.add(campoCheckIn);

        painel.add(new JLabel("Data check-out (AAAA-MM-DD):"));
        campoCheckOut = new JTextField();
        painel.add(campoCheckOut);

        JButton btnReservar = new JButton("Confirmar Reserva");
        btnReservar.addActionListener(e -> reservar());
        painel.add(btnReservar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        painel.add(btnCancelar);

        add(painel);
    }

    private void carregarQuartos() {
        comboQuarto.removeAllItems();
        List<Quarto> lista = quartoController.listarDisponiveis();
        for (Quarto q : lista) {
            comboQuarto.addItem(q.getNumeroQuarto());
        }
        if (comboQuarto.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Não há quartos disponíveis neste momento.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void reservar() {
        try {
            String documento = campoDocumento.getText().trim();
            String nome = campoNome.getText().trim();
            String contacto = campoContacto.getText().trim();
            String endereco = campoEndereco.getText().trim();
            String sexo = campoSexo.getText().trim();
            String nacionalidade = campoNacionalidade.getText().trim();

            if (documento.isEmpty() || nome.isEmpty()) {
                throw new IllegalArgumentException("Documento e nome são obrigatórios.");
            }

            Object item = comboQuarto.getSelectedItem();
            if (item == null) {
                throw new IllegalArgumentException("Selecione um quarto.");
            }
            int numeroQuarto = (Integer) item;

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkIn;
            LocalDate checkOut;
            try {
                checkIn = LocalDate.parse(campoCheckIn.getText().trim(), fmt);
                checkOut = LocalDate.parse(campoCheckOut.getText().trim(), fmt);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Datas inválidas. Use o formato AAAA-MM-DD.");
            }

            Cliente existente = null;
            try {
                existente = clienteController.buscarPorDocumento(documento);
            } catch (IllegalArgumentException ignored) {
                existente = null;
            }

            Cliente cliente;
            if (existente == null) {
                cliente = clienteController.criar(nome, contacto, endereco, sexo, documento, nacionalidade);
            } else {
                cliente = existente;
            }

            Reserva reserva = reservaController.criar(cliente.getIdPessoa(), numeroQuarto, checkIn, checkOut);

            JOptionPane.showMessageDialog(this,
                    "Reserva criada com sucesso. ID: " + reserva.getIdReserva(),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            campoDocumento.setText("");
            campoNome.setText("");
            campoContacto.setText("");
            campoEndereco.setText("");
            campoSexo.setText("");
            campoNacionalidade.setText("");
            campoCheckIn.setText("");
            campoCheckOut.setText("");
            carregarQuartos();

        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}