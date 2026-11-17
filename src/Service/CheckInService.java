package Service;

import Model.Enums.EstadoQuarto;
import Model.Enums.EstadoReserva;
import Model.Hospedagem;
import Model.Reserva;
import Repository.HospedagemRepository;
import Repository.ReservaRepository;

public class CheckInService {

    private final ReservaRepository reservaRepository;
    private final HospedagemRepository hospedagemRepository;
    private final QuartoService quartoService;

    public CheckInService() {
        this.reservaRepository = new ReservaRepository();
        this.hospedagemRepository = new HospedagemRepository();
        this.quartoService = new QuartoService();
    }

    public Hospedagem realizarCheckIn(int idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva);
        if (reserva == null) {
            throw new IllegalArgumentException("Reserva não encontrada.");
        }
        if (reserva.getEstadoReserva() != EstadoReserva.PENDENTE) {
            throw new IllegalStateException("A reserva não está pendente.");
        }

        QuartoService qs = quartoService;
        qs.alterarEstado(reserva.getQuarto().getNumeroQuarto(), EstadoQuarto.OCUPADO);

        reservaRepository.atualizarEstado(idReserva, EstadoReserva.ATIVA);

        Hospedagem h = new Hospedagem();
        h.setDataCheckIn(reserva.getCheckIn());
        h.setDataCheckOut(null);
        h.setReserva(reserva);
        h.setCliente(reserva.getCliente());

        int id = hospedagemRepository.inserir(h);
        if (id <= 0) {
            throw new RuntimeException("Falha ao criar o registo de hospedagem.");
        }
        return h;
    }
}