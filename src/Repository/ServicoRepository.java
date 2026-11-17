package Repository;

import Model.Servico;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServicoRepository {

    public int inserirBase(Servico servico) {
        String sql = "INSERT INTO Servico (PrecoBase) VALUES (?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, servico.getPrecoBase());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                servico.setIdServico(id);
                return id;
            }
        } catch (SQLException e) {
            System.err.println("ServicoRepository.inserirBase: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public double findPrecoBase(int idServico) {
        String sql = "SELECT PrecoBase FROM Servico WHERE IdServico = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idServico);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("PrecoBase");
            }
        } catch (SQLException e) {
            System.err.println("ServicoRepository.findPrecoBase: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return -1;
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
            System.err.println("ServicoRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }
}
