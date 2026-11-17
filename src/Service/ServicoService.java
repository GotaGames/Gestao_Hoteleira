package Service;

import Model.Hospedagem;
import Model.Lavandaria;
import Model.Restaurante;
import Model.Servico;
import Model.Transporte;
import Model.Eventos;
import Repository.EventosRepository;
import Repository.HospedagemRepository;
import Repository.LavandariaRepository;
import Repository.RestauranteRepository;
import Repository.TransporteRepository;

public class ServicoService {

    private final RestauranteRepository restauranteRepository;
    private final LavandariaRepository lavandariaRepository;
    private final TransporteRepository transporteRepository;
    private final EventosRepository eventosRepository;
    private final HospedagemRepository hospedagemRepository;

    public ServicoService() {
        this.restauranteRepository = new RestauranteRepository();
        this.lavandariaRepository = new LavandariaRepository();
        this.transporteRepository = new TransporteRepository();
        this.eventosRepository = new EventosRepository();
        this.hospedagemRepository = new HospedagemRepository();
    }

    public Servico adicionarRestaurante(Hospedagem hospedagem, String tipoRefeicao, double precoBase) {
        if (hospedagem == null) {
            throw new IllegalArgumentException("A hospedagem é obrigatória.");
        }
        if (tipoRefeicao == null || tipoRefeicao.isBlank()) {
            throw new IllegalArgumentException("O tipo de refeição é obrigatório.");
        }
        if (precoBase <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        Restaurante r = new Restaurante();
        r.setTipoRefeicao(tipoRefeicao);
        r.setPrecoBase(precoBase);

        int id = restauranteRepository.inserir(r);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível registar o serviço de restaurante.");
        }
        ligarHospedagem(hospedagem.getIdHospedagem(), id);
        hospedagem.adicionarServico(r);
        return r;
    }

    public Servico adicionarLavandaria(Hospedagem hospedagem, int quantPecas, double precoBase) {
        if (hospedagem == null) {
            throw new IllegalArgumentException("A hospedagem é obrigatória.");
        }
        if (quantPecas <= 0) {
            throw new IllegalArgumentException("A quantidade de peças deve ser maior que zero.");
        }
        if (precoBase <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        Lavandaria l = new Lavandaria();
        l.setQuantPecas(quantPecas);
        l.setPrecoBase(precoBase);

        int id = lavandariaRepository.inserir(l);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível registar o serviço de lavandaria.");
        }
        ligarHospedagem(hospedagem.getIdHospedagem(), id);
        hospedagem.adicionarServico(l);
        return l;
    }

    public Servico adicionarTransporte(Hospedagem hospedagem, double distanciaKm, double precoBase) {
        if (hospedagem == null) {
            throw new IllegalArgumentException("A hospedagem é obrigatória.");
        }
        if (distanciaKm <= 0) {
            throw new IllegalArgumentException("A distância deve ser maior que zero.");
        }
        if (precoBase <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        Transporte t = new Transporte();
        t.setDistanciaKm(distanciaKm);
        t.setPrecoBase(precoBase);

        int id = transporteRepository.inserir(t);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível registar o serviço de transporte.");
        }
        ligarHospedagem(hospedagem.getIdHospedagem(), id);
        hospedagem.adicionarServico(t);
        return t;
    }

    public Servico adicionarEvento(Hospedagem hospedagem, int numeroDias, double precoBase) {
        if (hospedagem == null) {
            throw new IllegalArgumentException("A hospedagem é obrigatória.");
        }
        if (numeroDias <= 0) {
            throw new IllegalArgumentException("O número de dias deve ser maior que zero.");
        }
        if (precoBase <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        Eventos e = new Eventos();
        e.setNumeroDias(numeroDias);
        e.setPrecoBase(precoBase);

        int id = eventosRepository.inserir(e);
        if (id <= 0) {
            throw new RuntimeException("Não foi possível registar o serviço de eventos.");
        }
        ligarHospedagem(hospedagem.getIdHospedagem(), id);
        hospedagem.adicionarServico(e);
        return e;
    }

    private void ligarHospedagem(int idHospedagem, int idServico) {
        boolean ok = hospedagemRepository.adicionarServico(idHospedagem, idServico);
        if (!ok) {
            throw new RuntimeException("Não foi possível associar o serviço à hospedagem.");
        }
    }
}