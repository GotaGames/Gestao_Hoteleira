package Service;

import Model.Cliente;
import Model.Funcionario;
import Model.Hospedagem;
import Model.Quarto;
import Model.Reserva;

import java.time.LocalDate;
import java.util.List;

public class HotelService {

    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;
    private final QuartoService quartoService;
    private final ReservaService reservaService;
    private final CheckInService checkInService;
    private final CheckOutService checkOutService;
    private final FaturaService faturaService;

    public HotelService() {
        this.clienteService = new ClienteService();
        this.funcionarioService = new FuncionarioService();
        this.quartoService = new QuartoService();
        this.reservaService = new ReservaService();
        this.checkInService = new CheckInService();
        this.checkOutService = new CheckOutService();
        this.faturaService = new FaturaService();
    }

    public ClienteService clientes() {
        return clienteService;
    }

    public FuncionarioService funcionarios() {
        return funcionarioService;
    }

    public QuartoService quartos() {
        return quartoService;
    }

    public ReservaService reservas() {
        return reservaService;
    }

    public CheckInService checkIn() {
        return checkInService;
    }

    public CheckOutService checkOut() {
        return checkOutService;
    }

    public FaturaService fatura() {
        return faturaService;
    }

    public List<Quarto> listarQuartosDisponiveis() {
        return quartoService.listarDisponiveis();
    }

    public List<Reserva> listarReservasPendentes() {
        return reservaService.listarPorEstado(Model.Enums.EstadoReserva.PENDENTE);
    }

    public Hospedagem fazerCheckIn(int idReserva) {
        return checkInService.realizarCheckIn(idReserva);
    }

    public ResultadoCheckOut fazerCheckOut(int numeroQuarto, LocalDate dataCheckOut) {
        return checkOutService.realizarCheckOut(numeroQuarto, dataCheckOut);
    }
}