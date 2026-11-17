package View;

import Service.ResultadoCheckOut;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class FaturaFrame extends JFrame {

    public FaturaFrame(ResultadoCheckOut resultado, String caminhoFicheiro) {
        setTitle("Fatura - Quarto " + resultado.getHospedagem().getReserva().getQuarto().getNumeroQuarto());
        setSize(560, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setText(construirTexto(resultado, caminhoFicheiro));

        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel rodape = new JPanel(new GridLayout(1, 1, 10, 10));

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        rodape.add(btnFechar);

        add(rodape, BorderLayout.SOUTH);
    }

    private String construirTexto(ResultadoCheckOut r, String caminhoFicheiro) {
        StringBuilder sb = new StringBuilder();
        sb.append("=============================================\n");
        sb.append("                  FATURA                     \n");
        sb.append("=============================================\n");
        sb.append("Cliente:       ").append(r.getHospedagem().getCliente().getNome()).append("\n");
        sb.append("Documento:     ").append(r.getHospedagem().getCliente().getDocumentoIdentificacao()).append("\n");
        sb.append("Quarto:        ").append(r.getHospedagem().getReserva().getQuarto().getNumeroQuarto())
                .append(" (").append(r.getHospedagem().getReserva().getQuarto().getTipoQuarto()).append(")\n");
        sb.append("Check-in:      ").append(r.getHospedagem().getDataCheckIn()).append("\n");
        sb.append("Check-out:     ").append(r.getHospedagem().getDataCheckOut()).append("\n");
        sb.append("---------------------------------------------\n");
        sb.append("Dias faturados:       ").append(r.getDias()).append("\n");
        sb.append("Valor quarto/dia:     ").append(r.getHospedagem().getReserva().getQuarto().getPrecoDiario()).append("\n");
        sb.append("Total alojamento:     ").append(r.getTotalAlojamento()).append("\n");
        sb.append("---------------------------------------------\n");
        sb.append("Consumos:\n");
        if (r.getHospedagem().getServicos().isEmpty()) {
            sb.append("  (sem consumos registados)\n");
        } else {
            for (Model.Servico s : r.getHospedagem().getServicos()) {
                sb.append("  - ").append(descricaoServico(s))
                        .append("  ->  ").append(s.calcularServico()).append("\n");
            }
        }
        sb.append("Total consumos:       ").append(r.getTotalConsumos()).append("\n");
        sb.append("---------------------------------------------\n");
        sb.append("Subtotal:             ").append(r.getSubtotal()).append("\n");
        sb.append("Desconto:             ").append(r.getDesconto()).append("\n");
        sb.append("=============================================\n");
        sb.append("TOTAL A PAGAR:        ").append(r.getTotalFinal()).append("\n");
        sb.append("=============================================\n");
        if (caminhoFicheiro != null) {
            sb.append("\nFatura gravada em: ").append(caminhoFicheiro).append("\n");
        }
        return sb.toString();
    }

    private String descricaoServico(Model.Servico s) {
        if (s instanceof Model.Restaurante) {
            return "Restaurante (" + ((Model.Restaurante) s).getTipoRefeicao() + ")";
        }
        if (s instanceof Model.Lavandaria) {
            return "Lavandaria (" + ((Model.Lavandaria) s).getQuantPecas() + " peças)";
        }
        if (s instanceof Model.Transporte) {
            return "Transporte (" + ((Model.Transporte) s).getDistanciaKm() + " km)";
        }
        if (s instanceof Model.Eventos) {
            return "Evento (" + ((Model.Eventos) s).getNumeroDias() + " dias)";
        }
        return s.getClass().getSimpleName();
    }
}
