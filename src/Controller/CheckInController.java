package Controller;


import Model.Hospedagem;
import Service.CheckInService;

public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController() {
        this.checkInService = new CheckInService();
    }

    public Hospedagem realizar(int idReserva) {
        return checkInService.realizarCheckIn(idReserva);
    }
}