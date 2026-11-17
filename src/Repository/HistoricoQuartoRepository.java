package Repository;

import Model.HistoricoQuarto;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HistoricoQuartoRepository {

    public List<HistoricoQuarto> findByQuarto(int numeroQuarto) {
        String sql = "SELECT * FROM HistoricoQuarto WHERE numeroQuarto = ? ORDER BY dataAlteracao DESC";
        List<HistoricoQuarto> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroQuarto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("HistoricoQuartoRepository.findByQuarto: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public List<HistoricoQuarto> findAll() {
        String sql = "SELECT * FROM HistoricoQuarto ORDER BY dataAlteracao DESC LIMIT 500";
        List<HistoricoQuarto> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("HistoricoQuartoRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    private HistoricoQuarto mapear(ResultSet rs) throws SQLException {
        HistoricoQuarto h = new HistoricoQuarto();
        h.setIdHistorico(rs.getInt("idHistorico"));
        h.setNumeroQuarto(rs.getInt("numeroQuarto"));
        h.setEstadoAnterior(rs.getString("estadoAnterior"));
        h.setEstadoNovo(rs.getString("estadoNovo"));

        Timestamp ts = rs.getTimestamp("dataAlteracao");
        if (ts != null) {
            h.setDataAlteracao(ts.toLocalDateTime());
        }

        int idRes = rs.getInt("idReserva");
        if (!rs.wasNull()) {
            h.setIdReserva(idRes);
        }
        h.setObservacao(rs.getString("observacao"));
        return h;
    }
}
