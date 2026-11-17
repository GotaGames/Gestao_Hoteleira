package Controller;

import Service.CheckOutService;
import Service.ResultadoCheckOut;

import java.time.LocalDate;

public class CheckOutController {

    private final CheckOutService checkOutService;

    public CheckOutController() {
        this.checkOutService = new CheckOutService();
    }

    public ResultadoCheckOut realizar(int numeroQuarto, LocalDate dataCheckOut) {
        return checkOutService.realizarCheckOut(numeroQuarto, dataCheckOut);
    }
}
