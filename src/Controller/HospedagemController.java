package Controller;

import Model.Hospedagem;
import Repository.HospedagemRepository;

import java.util.List;

public class HospedagemController {

    private final HospedagemRepository hospedagemRepository;

    public HospedagemController() {
        this.hospedagemRepository = new HospedagemRepository();
    }

    public List<Hospedagem> listarAtivas() {
        return hospedagemRepository.findAtivas();
    }
}
