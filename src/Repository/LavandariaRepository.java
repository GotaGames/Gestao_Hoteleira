package Repository;

import Model.Lavandaria;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LavandariaRepository {

    public int inserir(Lavandaria lavandaria) {
        String sqlBase = "INSERT INTO Servico (PrecoBase) VALUES (?)";
        String sqlSub = "INSERT INTO Lavandaria (IdServico, quantPecas) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psBase = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);
            psBase.setDouble(1, lavandaria.getPrecoBase());
            psBase.executeUpdate();

            ResultSet rs = psBase.getGeneratedKeys();
            if (rs.next()) {
                int idServico = rs.getInt(1);
                lavandaria.setIdServico(idServico);

                PreparedStatement psSub = conn.prepareStatement(sqlSub);
                psSub.setInt(1, idServico);
                psSub.setInt(2, lavandaria.getQuantPecas());
                psSub.executeUpdate();

                conn.commit();
                return idServico;
            }
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("LavandariaRepository.inserir: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) { }
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Lavandaria findById(int idServico) {
        String sql = "SELECT s.IdServico, s.PrecoBase, l.quantPecas FROM Servico s " +
                "INNER JOIN Lavandaria l ON s.IdServico = l.IdServico WHERE s.IdServico = ?";
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
            System.err.println("LavandariaRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Lavandaria> findAll() {
        String sql = "SELECT s.IdServico, s.PrecoBase, l.quantPecas FROM Servico s " +
                "INNER JOIN Lavandaria l ON s.IdServico = l.IdServico ORDER BY s.IdServico";
        List<Lavandaria> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("LavandariaRepository.findAll: " + e.getMessage());
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
            System.err.println("LavandariaRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Lavandaria mapear(ResultSet rs) throws SQLException {
        Lavandaria l = new Lavandaria();
        l.setIdServico(rs.getInt("IdServico"));
        l.setPrecoBase(rs.getDouble("PrecoBase"));
        l.setQuantPecas(rs.getInt("quantPecas"));
        return l;
    }
}