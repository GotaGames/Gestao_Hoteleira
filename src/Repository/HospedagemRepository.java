package Repository;

import Model.Cliente;
import Model.Enums.EstadoQuarto;
import Model.Enums.EstadoReserva;
import Model.Hospedagem;
import Model.Quarto;
import Model.Reserva;
import Model.Servico;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HospedagemRepository {

    private final RestauranteRepository restauranteRepo = new RestauranteRepository();
    private final LavandariaRepository lavandariaRepo = new LavandariaRepository();
    private final TransporteRepository transporteRepo = new TransporteRepository();
    private final EventosRepository eventosRepo = new EventosRepository();

    public int inserir(Hospedagem hospedagem) {
        String sql = "INSERT INTO Hospedagem (dataCheckIn, dataCheckOut, idReserva, idCliente) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(hospedagem.getDataCheckIn()));
            if (hospedagem.getDataCheckOut() != null) {
                ps.setDate(2, Date.valueOf(hospedagem.getDataCheckOut()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setInt(3, hospedagem.getReserva().getIdReserva());
            ps.setInt(4, hospedagem.getCliente().getIdPessoa());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                hospedagem.setIdHospedagem(id);
                return id;
            }
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.inserir: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public boolean adicionarServico(int idHospedagem, int idServico) {
        String sql = "INSERT INTO hospedagem_servico (idHospedagem, idServico) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHospedagem);
            ps.setInt(2, idServico);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.adicionarServico: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public boolean atualizarCheckOut(int idHospedagem, java.time.LocalDate dataCheckOut) {
        String sql = "UPDATE Hospedagem SET dataCheckOut = ? WHERE idHospedagem = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(dataCheckOut));
            ps.setInt(2, idHospedagem);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.atualizarCheckOut: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public Hospedagem findById(int idHospedagem) {
        String sql = "SELECT h.idHospedagem, h.dataCheckIn, h.dataCheckOut, h.idReserva, h.idCliente, " +
                "r.dataReserva, r.CheckIn, r.CheckOut, r.EstadoReserva, " +
                "q.numeroQuarto, q.tipoQuarto, q.precoDiario, q.EstadoQuarto, " +
                "p.nome, p.contacto, p.endereco, p.Sexo, p.documentoIdentificacao, " +
                "c.Nacionalidade " +
                "FROM Hospedagem h " +
                "INNER JOIN reserva r ON h.idReserva = r.idReserva " +
                "INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto " +
                "LEFT JOIN pessoa p ON h.idCliente = p.IdPessoa " +
                "LEFT JOIN Cliente c ON h.idCliente = c.IdPessoa " +
                "WHERE h.idHospedagem = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHospedagem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Hospedagem h = mapear(rs);
                h.setServicos(carregarServicos(idHospedagem));
                return h;
            }
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public Hospedagem findByQuartoAtivo(int numeroQuarto) {
        String sql = "SELECT h.idHospedagem, h.dataCheckIn, h.dataCheckOut, h.idReserva, h.idCliente, " +
                "r.dataReserva, r.CheckIn, r.CheckOut, r.EstadoReserva, " +
                "q.numeroQuarto, q.tipoQuarto, q.precoDiario, q.EstadoQuarto, " +
                "p.nome, p.contacto, p.endereco, p.Sexo, p.documentoIdentificacao, " +
                "c.Nacionalidade " +
                "FROM Hospedagem h " +
                "INNER JOIN reserva r ON h.idReserva = r.idReserva " +
                "INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto " +
                "LEFT JOIN pessoa p ON h.idCliente = p.IdPessoa " +
                "LEFT JOIN Cliente c ON h.idCliente = c.IdPessoa " +
                "WHERE q.numeroQuarto = ? AND h.dataCheckOut IS NULL " +
                "ORDER BY h.idHospedagem DESC LIMIT 1";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroQuarto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Hospedagem h = mapear(rs);
                h.setServicos(carregarServicos(h.getIdHospedagem()));
                return h;
            }
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.findByQuartoAtivo: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Hospedagem> findAll() {
        String sql = "SELECT h.idHospedagem, h.dataCheckIn, h.dataCheckOut, h.idReserva, h.idCliente, " +
                "r.dataReserva, r.CheckIn, r.CheckOut, r.EstadoReserva, " +
                "q.numeroQuarto, q.tipoQuarto, q.precoDiario, q.EstadoQuarto, " +
                "p.nome, p.contacto, p.endereco, p.Sexo, p.documentoIdentificacao, " +
                "c.Nacionalidade " +
                "FROM Hospedagem h " +
                "INNER JOIN reserva r ON h.idReserva = r.idReserva " +
                "INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto " +
                "LEFT JOIN pessoa p ON h.idCliente = p.IdPessoa " +
                "LEFT JOIN Cliente c ON h.idCliente = c.IdPessoa " +
                "ORDER BY h.idHospedagem";
        List<Hospedagem> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hospedagem h = mapear(rs);
                h.setServicos(carregarServicos(h.getIdHospedagem()));
                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public List<Hospedagem> findAtivas() {
        String sql = "SELECT h.idHospedagem, h.dataCheckIn, h.dataCheckOut, h.idReserva, h.idCliente, " +
                "r.dataReserva, r.CheckIn, r.CheckOut, r.EstadoReserva, " +
                "q.numeroQuarto, q.tipoQuarto, q.precoDiario, q.EstadoQuarto, " +
                "p.nome, p.contacto, p.endereco, p.Sexo, p.documentoIdentificacao, " +
                "c.Nacionalidade " +
                "FROM Hospedagem h " +
                "INNER JOIN reserva r ON h.idReserva = r.idReserva " +
                "INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto " +
                "LEFT JOIN pessoa p ON h.idCliente = p.IdPessoa " +
                "LEFT JOIN Cliente c ON h.idCliente = c.IdPessoa " +
                "WHERE h.dataCheckOut IS NULL " +
                "ORDER BY h.idHospedagem";
        List<Hospedagem> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hospedagem h = mapear(rs);
                h.setServicos(carregarServicos(h.getIdHospedagem()));
                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.findAtivas: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    private List<Servico> carregarServicos(int idHospedagem) {
        String sql = "SELECT idServico FROM hospedagem_servico WHERE idHospedagem = ?";
        List<Servico> servicos = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHospedagem);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idServico = rs.getInt("idServico");
                Servico s = resolverServico(idServico);
                if (s != null) {
                    servicos.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("HospedagemRepository.carregarServicos: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return servicos;
    }

    private Servico resolverServico(int idServico) {
        Servico s = restauranteRepo.findById(idServico);
        if (s != null) return s;
        s = lavandariaRepo.findById(idServico);
        if (s != null) return s;
        s = transporteRepo.findById(idServico);
        if (s != null) return s;
        s = eventosRepo.findById(idServico);
        return s;
    }

    private Hospedagem mapear(ResultSet rs) throws SQLException {
        Hospedagem h = new Hospedagem();
        h.setIdHospedagem(rs.getInt("idHospedagem"));
        h.setDataCheckIn(rs.getDate("dataCheckIn").toLocalDate());
        Date dco = rs.getDate("dataCheckOut");
        if (dco != null) {
            h.setDataCheckOut(dco.toLocalDate());
        }

        Reserva r = new Reserva();
        r.setIdReserva(rs.getInt("idReserva"));
        r.setDataReserva(rs.getDate("dataReserva").toLocalDate());
        r.setCheckIn(rs.getDate("CheckIn").toLocalDate());
        r.setCheckOut(rs.getDate("CheckOut").toLocalDate());
        r.setEstadoReserva(EstadoReserva.valueOf(rs.getString("EstadoReserva")));

        Quarto q = new Quarto();
        q.setNumeroQuarto(rs.getInt("numeroQuarto"));
        q.setTipoQuarto(rs.getString("tipoQuarto"));
        q.setPrecoDiario(rs.getDouble("precoDiario"));
        q.setEstadoQuarto(EstadoQuarto.valueOf(rs.getString("EstadoQuarto")));
        r.setQuarto(q);

        Cliente c = new Cliente();
        c.setIdPessoa(rs.getInt("idCliente"));
        String nome = rs.getString("nome");
        if (nome != null) {
            c.setNome(nome);
            c.setContacto(rs.getString("contacto"));
            c.setEndereco(rs.getString("endereco"));
            c.setSexo(rs.getString("Sexo"));
            c.setDocumentoIdentificacao(rs.getString("documentoIdentificacao"));
            c.setNacionalidade(rs.getString("Nacionalidade"));
        } else {
            c.setNome("(cliente não encontrado)");
        }

        r.setCliente(c);
        h.setReserva(r);
        h.setCliente(c);
        return h;
    }
}
