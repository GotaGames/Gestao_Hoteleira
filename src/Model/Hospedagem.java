package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hospedagem {

    private int idHospedagem;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;
    private Reserva reserva;
    private Cliente cliente;
    private List<Servico> servicos;

    public Hospedagem() {
        this.servicos = new ArrayList<>();
    }

    public Hospedagem(int idHospedagem, LocalDate dataCheckIn, LocalDate dataCheckOut,
                      Reserva reserva, Cliente cliente, List<Servico> servicos) {
        this.idHospedagem = idHospedagem;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.reserva = reserva;
        this.cliente = cliente;
        this.servicos = (servicos != null) ? servicos : new ArrayList<>();
    }

    public int getIdHospedagem() {
        return idHospedagem;
    }

    public void setIdHospedagem(int idHospedagem) {
        this.idHospedagem = idHospedagem;
    }

    public LocalDate getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(LocalDate dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public LocalDate getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(LocalDate dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public void adicionarServico(Servico servico) {
        this.servicos.add(servico);
    }

    @Override
    public String toString() {
        return "Hospedagem{" +
                "idHospedagem=" + idHospedagem +
                ", dataCheckIn=" + dataCheckIn +
                ", dataCheckOut=" + dataCheckOut +
                ", idReserva=" + (reserva != null ? reserva.getIdReserva() : 0) +
                ", idCliente=" + (cliente != null ? cliente.getIdPessoa() : 0) +
                ", servicos=" + servicos.size() +
                '}';
    }
}
