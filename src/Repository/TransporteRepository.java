package Repository;

import Model.Transporte;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransporteRepository {

    public int inserir(Transporte transporte) {
        String sqlBase = "INSERT INTO Servico (PrecoBase) VALUES (?)";
        String sqlSub = "INSERT INTO Transporte (IdServico, distanciakm) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psBase = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            psBase.setDouble(1, transporte.getPrecoBase());
            psBase.executeUpdate();

            ResultSet rs = psBase.getGeneratedKeys();
            if (rs.next()) {
                int idServico = rs.getInt(1);
                transporte.setIdServico(idServico);

                PreparedStatement psSub = conn.prepareStatement(sqlSub);
                psSub.setInt(1, idServico);
                psSub.setDouble(2, transporte.getDistanciaKm());
                psSub.executeUpdate();

                conn.commit();
                return idServico;
            }
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("TransporteRepository.inserir: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) { }
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Transporte findById(int idServico) {
        String sql = "SELECT s.IdServico, s.PrecoBase, t.distanciakm FROM Servico s " +
                "INNER JOIN Transporte t ON s.IdServico = t.IdServico WHERE s.IdServico = ?";
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
            System.err.println("TransporteRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Transporte> findAll() {
        String sql = "SELECT s.IdServico, s.PrecoBase, t.distanciakm FROM Servico s " +
                "INNER JOIN Transporte t ON s.IdServico = t.IdServico ORDER BY s.IdServico";
        List<Transporte> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("TransporteRepository.findAll: " + e.getMessage());
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
            System.err.println("TransporteRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Transporte mapear(ResultSet rs) throws SQLException {
        Transporte t = new Transporte();
        t.setIdServico(rs.getInt("IdServico"));
        t.setPrecoBase(rs.getDouble("PrecoBase"));
        t.setDistanciaKm(rs.getDouble("distanciakm"));
        return t;
    }
}