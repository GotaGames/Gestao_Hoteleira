package Repository;

import Model.Eventos;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventosRepository {

    public int inserir(Eventos eventos) {
        String sqlBase = "INSERT INTO Servico (PrecoBase) VALUES (?)";
        String sqlSub = "INSERT INTO Eventos (IdServico, numeroDias) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psBase = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            psBase.setDouble(1, eventos.getPrecoBase());
            psBase.executeUpdate();

            ResultSet rs = psBase.getGeneratedKeys();
            if (rs.next()) {
                int idServico = rs.getInt(1);
                eventos.setIdServico(idServico);

                PreparedStatement psSub = conn.prepareStatement(sqlSub);
                psSub.setInt(1, idServico);
                psSub.setInt(2, eventos.getNumeroDias());
                psSub.executeUpdate();

                conn.commit();
                return idServico;
            }
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("EventosRepository.inserir: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) { }
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Eventos findById(int idServico) {
        String sql = "SELECT s.IdServico, s.PrecoBase, e.numeroDias FROM Servico s " +
                "INNER JOIN Eventos e ON s.IdServico = e.IdServico WHERE s.IdServico = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idServico);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("EventosRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Eventos> findAll() {
        String sql = "SELECT s.IdServico, s.PrecoBase, e.numeroDias FROM Servico s " +
                "INNER JOIN Eventos e ON s.IdServico = e.IdServico ORDER BY s.IdServico";
        List<Eventos> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("EventosRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public boolean eliminar(int idServico) {
        String sql = "DELETE FROM Servico WHERE IdServico = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idServico);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("EventosRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Eventos mapear(ResultSet rs) throws SQLException {
        Eventos e = new Eventos();
        e.setIdServico(rs.getInt("IdServico"));
        e.setPrecoBase(rs.getDouble("PrecoBase"));
        e.setNumeroDias(rs.getInt("numeroDias"));
        return e;
    }
}
