package Util;

import Service.ResultadoCheckOut;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FaturaWriter {

    private static final String PASTA_FATURAS = "faturas";

    public static String gravar(ResultadoCheckOut resultado) {
        File pasta = new File(PASTA_FATURAS);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        int numQuarto = resultado.getHospedagem().getReserva().getQuarto().getNumeroQuarto();
        String nomeFicheiro = PASTA_FATURAS + File.separator + "fatura_quarto_" + numQuarto + "_" + System.currentTimeMillis() + ".txt";

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeFicheiro))) {
            bw.write("=============================================");
            bw.newLine();
            bw.write("                  FATURA");
            bw.newLine();
            bw.write("=============================================");
            bw.newLine();
            bw.write("Cliente: " + resultado.getHospedagem().getCliente().getNome());
            bw.newLine();
            bw.write("Documento: " + resultado.getHospedagem().getCliente().getDocumentoIdentificacao());
            bw.newLine();
            bw.write("Quarto: " + numQuarto + " (" + resultado.getHospedagem().getReserva().getQuarto().getTipoQuarto() + ")");
            bw.newLine();
            bw.write("Data check-in:  " + resultado.getHospedagem().getDataCheckIn().format(fmt));
            bw.newLine();
            bw.write("Data check-out: " + resultado.getHospedagem().getReserva().getCheckOut().format(fmt));
            bw.newLine();
            bw.write("---------------------------------------------");
            bw.newLine();
            bw.write("Dias faturados:       " + resultado.getDias());
            bw.newLine();
            bw.write("Valor quarto/dia:     " + resultado.getHospedagem().getReserva().getQuarto().getPrecoDiario());
            bw.newLine();
            bw.write("Total alojamento:     " + resultado.getTotalAlojamento());
            bw.newLine();
            bw.write("Total consumos:       " + resultado.getTotalConsumos());
            bw.newLine();
            bw.write("Subtotal:             " + resultado.getSubtotal());
            bw.newLine();
            bw.write("Desconto:             " + resultado.getDesconto());
            bw.newLine();
            bw.write("---------------------------------------------");
            bw.newLine();
            bw.write("TOTAL A PAGAR:        " + resultado.getTotalFinal());
            bw.newLine();
            bw.write("=============================================");
            bw.newLine();
            return nomeFicheiro;
        } catch (IOException e) {
            System.err.println("FaturaWriter.gravar: " + e.getMessage());
            return null;
        }
    }
}