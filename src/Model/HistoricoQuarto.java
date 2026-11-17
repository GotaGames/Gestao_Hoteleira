package Model;

import java.time.LocalDateTime;

public class HistoricoQuarto {

    private int idHistorico;
    private int numeroQuarto;
    private String estadoAnterior;
    private String estadoNovo;
    private LocalDateTime dataAlteracao;
    private Integer idReserva;
    private String observacao;

    public HistoricoQuarto() {
    }

    public HistoricoQuarto(int idHistorico, int numeroQuarto, String estadoAnterior, String estadoNovo,
                           LocalDateTime dataAlteracao, Integer idReserva, String observacao) {
        this.idHistorico = idHistorico;
        this.numeroQuarto = numeroQuarto;
        this.estadoAnterior = estadoAnterior;
        this.estadoNovo = estadoNovo;
        this.dataAlteracao = dataAlteracao;
        this.idReserva = idReserva;
        this.observacao = observacao;
    }

    public int getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(int idHistorico) {
        this.idHistorico = idHistorico;
    }

    public int getNumeroQuarto() {
        return numeroQuarto;
    }

    public void setNumeroQuarto(int numeroQuarto) {
        this.numeroQuarto = numeroQuarto;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNovo() {
        return estadoNovo;
    }

    public void setEstadoNovo(String estadoNovo) {
        this.estadoNovo = estadoNovo;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "HistoricoQuarto{" +
                "numeroQuarto=" + numeroQuarto +
                ", estadoAnterior='" + estadoAnterior + '\'' +
                ", estadoNovo='" + estadoNovo + '\'' +
                ", dataAlteracao=" + dataAlteracao +
                '}';
    }
}
