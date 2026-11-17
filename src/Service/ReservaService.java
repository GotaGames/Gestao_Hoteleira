package Service;

import Model.Cliente;
import Model.Enums.EstadoQuarto;
import Model.Enums.EstadoReserva;
import Model.Quarto;
import Model.Reserva;
import Repository.ReservaRepository;

import java.time.LocalDate;
import java.util.List;

public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final QuartoService quartoService;

    public ReservaService() {
        this.reservaRepository = new ReservaRepository();
        this.quartoService = new QuartoService();
    }

    public Reserva criar(Cliente cliente, Quarto quarto, LocalDate checkIn, LocalDate checkOut) {
        if (cliente == null) {
            throw new IllegalArgumentException("O cliente é obrigatório.");
        }
        if (quarto == null) {
            throw new IllegalArgumentException("O quarto é obrigatório.");
        }
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("As datas de check-in e check-out são obrigatórias.");
        }
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new IllegalArgumentException("A data de check-out deve ser posterior à de check-in.");
        }
        if (quarto.getEstadoQuarto() != EstadoQuarto.DISPONIVEL) {
            throw new IllegalStateException("O quarto não está disponível.");
        }

        Reserva reserva = new Reserva();
        reserva.setDataReserva(LocalDate.now());
        reserva.setCheckIn(checkIn);
        reserva.setCheckOut(checkOut);
        reserva.setEstadoReserva(EstadoReserva.PENDENTE);
        reserva.setQuarto(quarto);
        reserva.setCliente(cliente);

        int id = reservaRepository.inserir(reserva);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível criar a reserva.");
        }
        return reserva;
    }

    public Reserva buscarPorId(int idReserva) {
        Reserva r = reservaRepository.findById(idReserva);
        if (r == null) {
            throw new IllegalArgumentException("Reserva não encontrada.");
        }
        return r;
    }

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> listarPorEstado(EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

    public void cancelar(int idReserva) {
        Reserva r = buscarPorId(idReserva);
        if (r.getEstadoReserva() != EstadoReserva.PENDENTE) {
            throw new IllegalStateException("Só é possível cancelar reservas pendentes.");
        }
        reservaRepository.atualizarEstado(idReserva, EstadoReserva.CANCELADA);
    }

    public void atualizar(Reserva reserva) {
        boolean ok = reservaRepository.atualizar(reserva);
        if (!ok) {
            throw new RuntimeException("Não foi possível atualizar a reserva.");
        }
    }

    public void remover(int idReserva) {
        boolean ok = reservaRepository.eliminar(idReserva);
        if (!ok) {
            throw new RuntimeException("Não foi possível remover a reserva.");
        }
    }
}