package Repository;

import Model.Restaurante;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RestauranteRepository {

    public int inserir(Restaurante restaurante) {
        String sqlBase = "INSERT INTO Servico (PrecoBase) VALUES (?)";
        String sqlSub = "INSERT INTO Restaurante (IdServico, TipoRefeicao) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psBase = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            psBase.setDouble(1, restaurante.getPrecoBase());
            psBase.executeUpdate();

            ResultSet rs = psBase.getGeneratedKeys();
            if (rs.next()) {
                int idServico = rs.getInt(1);
                restaurante.setIdServico(idServico);

                PreparedStatement psSub = conn.prepareStatement(sqlSub);
                psSub.setInt(1, idServico);
                psSub.setString(2, restaurante.getTipoRefeicao());
                psSub.executeUpdate();

                conn.commit();
                return idServico;
            }
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("RestauranteRepository.inserir: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) { }
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Restaurante findById(int idServico) {
        String sql = "SELECT s.IdServico, s.PrecoBase, r.TipoRefeicao FROM Servico s " +
                "INNER JOIN Restaurante r ON s.IdServico = r.IdServico WHERE s.IdServico = ?";
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
            System.err.println("RestauranteRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Restaurante> findAll() {
        String sql = "SELECT s.IdServico, s.PrecoBase, r.TipoRefeicao FROM Servico s " +
                "INNER JOIN Restaurante r ON s.IdServico = r.IdServico ORDER BY s.IdServico";
        List<Restaurante> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("RestauranteRepository.findAll: " + e.getMessage());
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
            System.err.println("RestauranteRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Restaurante mapear(ResultSet rs) throws SQLException {
        Restaurante r = new Restaurante();
        r.setIdServico(rs.getInt("IdServico"));
        r.setPrecoBase(rs.getDouble("PrecoBase"));
        r.setTipoRefeicao(rs.getString("TipoRefeicao"));
        return r;
    }
}