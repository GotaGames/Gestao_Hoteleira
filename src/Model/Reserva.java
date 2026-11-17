package Model;

import Model.Enums.EstadoReserva;

import java.time.LocalDate;

public class Reserva {

    private int idReserva;
    private LocalDate dataReserva;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private EstadoReserva estadoReserva;
    private Quarto quarto;
    private Cliente cliente;

    public Reserva() {
    }

    public Reserva(int idReserva, LocalDate dataReserva, LocalDate checkIn, LocalDate checkOut,
                   EstadoReserva estadoReserva, Quarto quarto, Cliente cliente) {
        this.idReserva = idReserva;
        this.dataReserva = dataReserva;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.estadoReserva = estadoReserva;
        this.quarto = quarto;
        this.cliente = cliente;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public void setEstadoReserva(EstadoReserva estadoReserva) {
        this.estadoReserva = estadoReserva;
    }

    public Quarto getQuarto() {
        return quarto;
    }

    public void setQuarto(Quarto quarto) {
        this.quarto = quarto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", dataReserva=" + dataReserva +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", estadoReserva=" + estadoReserva +
                ", numeroQuarto=" + (quarto != null ? quarto.getNumeroQuarto() : 0) +
                ", idCliente=" + (cliente != null ? cliente.getIdPessoa() : 0) +
                '}';
    }
}