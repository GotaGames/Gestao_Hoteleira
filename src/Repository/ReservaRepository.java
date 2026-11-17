package Repository;

import Model.Cliente;
import Model.Enums.EstadoQuarto;
import Model.Enums.EstadoReserva;
import Model.Quarto;
import Model.Reserva;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservaRepository {

    public int inserir(Reserva reserva) {
        String sql = "INSERT INTO reserva (dataReserva, CheckIn, CheckOut, EstadoReserva, numeroQuarto, idCliente) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(reserva.getDataReserva()));
            ps.setDate(2, Date.valueOf(reserva.getCheckIn()));
            ps.setDate(3, Date.valueOf(reserva.getCheckOut()));
            ps.setString(4, reserva.getEstadoReserva().name());
            ps.setInt(5, reserva.getQuarto().getNumeroQuarto());
            ps.setInt(6, reserva.getCliente().getIdPessoa());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                reserva.setIdReserva(id);
                return id;
            }
        } catch (SQLException e) {
            System.err.println("ReservaRepository.inserir: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Reserva findById(int idReserva) {
        String sql = "SELECT r.*, " +
                "q.tipoQuarto, q.precoDiario, q.EstadoQuarto, " +
                "p.nome, p.contacto, p.endereco, p.Sexo, p.documentoIdentificacao, " +
                "c.Nacionalidade " +
                "FROM reserva r " +
                "INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto " +
                "INNER JOIN pessoa p ON r.idCliente = p.IdPessoa " +
                "INNER JOIN Cliente c ON p.IdPessoa = c.IdPessoa " +
                "WHERE r.idReserva = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("ReservaRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Reserva> findAll() {
        String sql = "SELECT r.*, " +
                "q.tipoQuarto, q.precoDiario, q.EstadoQuarto, " +
                "p.nome, p.contacto, p.endereco, p.Sexo, p.documentoIdentificacao, " +
                "c.Nacionalidade " +
                "FROM reserva r " +
                "INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto " +
                "INNER JOIN pessoa p ON r.idCliente = p.IdPessoa " +
                "INNER JOIN Cliente c ON p.IdPessoa = c.IdPessoa " +
                "ORDER BY r.idReserva";
        List<Reserva> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("ReservaRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public List<Reserva> findByEstado(EstadoReserva estado) {
        String sql = "SELECT r.*, " +
                "q.tipoQuarto, q.precoDiario, q.EstadoQuarto, " +
                "p.nome, p.contacto, p.endereco, p.Sexo, p.documentoIdentificacao, " +
                "c.Nacionalidade " +
                "FROM reserva r " +
                "INNER JOIN quarto q ON r.numeroQuarto = q.numeroQuarto " +
                "INNER JOIN pessoa p ON r.idCliente = p.IdPessoa " +
                "INNER JOIN Cliente c ON p.IdPessoa = c.IdPessoa " +
                "WHERE r.EstadoReserva = ? " +
                "ORDER BY r.idReserva";
        List<Reserva> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("ReservaRepository.findByEstado: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public boolean atualizarEstado(int idReserva, EstadoReserva estado) {
        String sql = "UPDATE reserva SET EstadoReserva = ? WHERE idReserva = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, estado.name());
            ps.setInt(2, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReservaRepository.atualizarEstado: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public boolean atualizar(Reserva reserva) {
        String sql = "UPDATE reserva SET dataReserva = ?, CheckIn = ?, CheckOut = ?, EstadoReserva = ?, numeroQuarto = ?, idCliente = ? WHERE idReserva = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(reserva.getDataReserva()));
            ps.setDate(2, Date.valueOf(reserva.getCheckIn()));
            ps.setDate(3, Date.valueOf(reserva.getCheckOut()));
            ps.setString(4, reserva.getEstadoReserva().name());
            ps.setInt(5, reserva.getQuarto().getNumeroQuarto());
            ps.setInt(6, reserva.getCliente().getIdPessoa());
            ps.setInt(7, reserva.getIdReserva());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReservaRepository.atualizar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public boolean eliminar(int idReserva) {
        String sql = "DELETE FROM reserva WHERE idReserva = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ReservaRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Reserva mapear(ResultSet rs) throws SQLException {
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
        c.setNome(rs.getString("nome"));
        c.setContacto(rs.getString("contacto"));
        c.setEndereco(rs.getString("endereco"));
        c.setSexo(rs.getString("Sexo"));
        c.setDocumentoIdentificacao(rs.getString("documentoIdentificacao"));
        c.setNacionalidade(rs.getString("Nacionalidade"));
        r.setCliente(c);

        return r;
    }
}