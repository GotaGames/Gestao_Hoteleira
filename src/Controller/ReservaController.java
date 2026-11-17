package Controller;

import Model.Cliente;
import Model.Quarto;
import Model.Reserva;
import Service.ReservaService;

import java.time.LocalDate;
import java.util.List;

public class ReservaController {

    private final ReservaService reservaService;
    private final ClienteController clienteController;
    private final QuartoController quartoController;

    public ReservaController() {
        this.reservaService = new ReservaService();
        this.clienteController = new ClienteController();
        this.quartoController = new QuartoController();
    }

    public Reserva criar(int idCliente, int numeroQuarto, LocalDate checkIn, LocalDate checkOut) {
        Cliente cliente = clienteController.buscarPorId(idCliente);
        Quarto quarto = quartoController.buscarPorNumero(numeroQuarto);
        return reservaService.criar(cliente, quarto, checkIn, checkOut);
    }

    public Reserva buscarPorId(int idReserva) {
        return reservaService.buscarPorId(idReserva);
    }

    public List<Reserva> listarTodas() {
        return reservaService.listarTodas();
    }

    public void cancelar(int idReserva) {
        reservaService.cancelar(idReserva);
    }

    public void remover(int idReserva) {
        reservaService.remover(idReserva);
    }
}