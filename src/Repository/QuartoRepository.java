package Repository;

import Model.Enums.EstadoQuarto;
import Model.Quarto;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuartoRepository {

    public boolean inserir(Quarto quarto) {
        String sql = "INSERT INTO quarto (numeroQuarto, tipoQuarto, precoDiario, EstadoQuarto) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, quarto.getNumeroQuarto());
            ps.setString(2, quarto.getTipoQuarto());
            ps.setDouble(3, quarto.getPrecoDiario());
            ps.setString(4, quarto.getEstadoQuarto().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("QuartoRepository.inserir: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public Quarto findById(int numeroQuarto) {
        String sql = "SELECT * FROM quarto WHERE numeroQuarto = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroQuarto);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("QuartoRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Quarto> findAll() {
        String sql = "SELECT * FROM quarto ORDER BY numeroQuarto";
        List<Quarto> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("QuartoRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public List<Quarto> findByEstado(EstadoQuarto estado) {
        String sql = "SELECT * FROM quarto WHERE EstadoQuarto = ? ORDER BY numeroQuarto";
        List<Quarto> lista = new ArrayList<>();
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
            System.err.println("QuartoRepository.findByEstado: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public boolean atualizar(Quarto quarto) {
        String sql = "UPDATE quarto SET tipoQuarto = ?, precoDiario = ?, EstadoQuarto = ? WHERE numeroQuarto = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, quarto.getTipoQuarto());
            ps.setDouble(2, quarto.getPrecoDiario());
            ps.setString(3, quarto.getEstadoQuarto().name());
            ps.setInt(4, quarto.getNumeroQuarto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("QuartoRepository.atualizar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public boolean atualizarEstado(int numeroQuarto, EstadoQuarto estado) {
        String sql = "UPDATE quarto SET EstadoQuarto = ? WHERE numeroQuarto = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, estado.name());
            ps.setInt(2, numeroQuarto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("QuartoRepository.atualizarEstado: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public boolean eliminar(int numeroQuarto) {
        String sql = "DELETE FROM quarto WHERE numeroQuarto = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroQuarto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("QuartoRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Quarto mapear(ResultSet rs) throws SQLException {
        Quarto q = new Quarto();
        q.setNumeroQuarto(rs.getInt("numeroQuarto"));
        q.setTipoQuarto(rs.getString("tipoQuarto"));
        q.setPrecoDiario(rs.getDouble("precoDiario"));
        q.setEstadoQuarto(EstadoQuarto.valueOf(rs.getString("EstadoQuarto")));
        return q;
    }
}
