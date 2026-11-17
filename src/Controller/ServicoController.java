package Controller;

import Model.Hospedagem;
import Model.Servico;
import Repository.HospedagemRepository;
import Service.ServicoService;

public class ServicoController {

    private final ServicoService servicoService;
    private final HospedagemRepository hospedagemRepository;

    public ServicoController() {
        this.servicoService = new ServicoService();
        this.hospedagemRepository = new HospedagemRepository();
    }

    public Hospedagem buscarHospedagemPorQuarto(int numeroQuarto) {
        Hospedagem h = hospedagemRepository.findByQuartoAtivo(numeroQuarto);
        if (h == null) {
            throw new IllegalArgumentException("Não existe hospedagem ativa no quarto " + numeroQuarto + ".");
        }
        return h;
    }

    public Servico adicionarRestaurante(int numeroQuarto, String tipoRefeicao, double precoBase) {
        Hospedagem h = buscarHospedagemPorQuarto(numeroQuarto);
        return servicoService.adicionarRestaurante(h, tipoRefeicao, precoBase);
    }

    public Servico adicionarLavandaria(int numeroQuarto, int quantPecas, double precoBase) {
        Hospedagem h = buscarHospedagemPorQuarto(numeroQuarto);
        return servicoService.adicionarLavandaria(h, quantPecas, precoBase);
    }

    public Servico adicionarTransporte(int numeroQuarto, double distanciaKm, double precoBase) {
        Hospedagem h = buscarHospedagemPorQuarto(numeroQuarto);
        return servicoService.adicionarTransporte(h, distanciaKm, precoBase);
    }

    public Servico adicionarEvento(int numeroQuarto, int numeroDias, double precoBase) {
        Hospedagem h = buscarHospedagemPorQuarto(numeroQuarto);
        return servicoService.adicionarEvento(h, numeroDias, precoBase);
    }
}