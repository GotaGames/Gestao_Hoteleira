package Repository;

import Model.Enums.EstadoPagamento;
import Model.Enums.MetodoPagamento;
import Model.Hospedagem;
import Model.Pagamento;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagamentoRepository {

    public int inserir(Pagamento pagamento) {
        String sql = "INSERT INTO Pagamento (idHospedagem, dataPagamento, valor, metodo, estado) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pagamento.getHospedagem().getIdHospedagem());
            ps.setDate(2, Date.valueOf(pagamento.getDataPagamento()));
            ps.setDouble(3, pagamento.getValor());
            ps.setString(4, pagamento.getMetodo().name());
            ps.setString(5, pagamento.getEstado().name());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                pagamento.setIdPagamento(id);
                return id;
            }
        } catch (SQLException e) {
            System.err.println("PagamentoRepository.inserir: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public List<Pagamento> findByHospedagem(int idHospedagem) {
        String sql = "SELECT * FROM Pagamento WHERE idHospedagem = ? ORDER BY dataPagamento";
        List<Pagamento> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHospedagem);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("PagamentoRepository.findByHospedagem: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public double totalPagoPorHospedagem(int idHospedagem) {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM Pagamento WHERE idHospedagem = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idHospedagem);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("PagamentoRepository.totalPagoPorHospedagem: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return 0.0;
    }

    public List<Pagamento> findAll() {
        String sql = "SELECT * FROM Pagamento ORDER BY dataPagamento DESC";
        List<Pagamento> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("PagamentoRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public List<Pagamento> findByPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT * FROM Pagamento WHERE dataPagamento BETWEEN ? AND ? ORDER BY dataPagamento";
        List<Pagamento> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("PagamentoRepository.findByPeriodo: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public double totalPorPeriodo(LocalDate inicio, LocalDate fim) {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM Pagamento WHERE dataPagamento BETWEEN ? AND ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.err.println("PagamentoRepository.totalPorPeriodo: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return 0.0;
    }

    private Pagamento mapear(ResultSet rs) throws SQLException {
        Pagamento p = new Pagamento();
        p.setIdPagamento(rs.getInt("idPagamento"));
        p.setDataPagamento(rs.getDate("dataPagamento").toLocalDate());
        p.setValor(rs.getDouble("valor"));
        p.setMetodo(MetodoPagamento.valueOf(rs.getString("metodo")));
        p.setEstado(EstadoPagamento.valueOf(rs.getString("estado")));

        Hospedagem h = new Hospedagem();
        h.setIdHospedagem(rs.getInt("idHospedagem"));
        p.setHospedagem(h);
        return p;
    }
}
