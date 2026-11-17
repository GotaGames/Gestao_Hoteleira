package Repository;

import Model.Cliente;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    public int inserir(Cliente cliente) {
        String sqlPessoa = "INSERT INTO pessoa (nome, contacto, endereco, Sexo, documentoIdentificacao) VALUES (?, ?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO Cliente (IdPessoa, Nacionalidade) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            psPessoa.setString(1, cliente.getNome());
            psPessoa.setString(2, cliente.getContacto());
            psPessoa.setString(3, cliente.getEndereco());
            psPessoa.setString(4, cliente.getSexo());
            psPessoa.setString(5, cliente.getDocumentoIdentificacao());
            psPessoa.executeUpdate();

            ResultSet rs = psPessoa.getGeneratedKeys();
            if (rs.next()) {
                int idPessoa = rs.getInt(1);
                cliente.setIdPessoa(idPessoa);

                PreparedStatement psCliente = conn.prepareStatement(sqlCliente);
                psCliente.setInt(1, idPessoa);
                psCliente.setString(2, cliente.getNacionalidade());
                psCliente.executeUpdate();

                conn.commit();
                return idPessoa;
            }
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("ClienteRepository.inserir: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) { }
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Cliente findById(int idPessoa) {
        String sql = "SELECT p.*, c.Nacionalidade FROM pessoa p " +
                "INNER JOIN Cliente c ON p.IdPessoa = c.IdPessoa " +
                "WHERE p.IdPessoa = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPessoa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("ClienteRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public Cliente findByDocumento(String documento) {
        String sql = "SELECT p.*, c.Nacionalidade FROM pessoa p " +
                "INNER JOIN Cliente c ON p.IdPessoa = c.IdPessoa " +
                "WHERE p.documentoIdentificacao = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("ClienteRepository.findByDocumento: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Cliente> findAll() {
        String sql = "SELECT p.*, c.Nacionalidade FROM pessoa p " +
                "INNER JOIN Cliente c ON p.IdPessoa = c.IdPessoa " +
                "ORDER BY p.IdPessoa";
        List<Cliente> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("ClienteRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public boolean atualizar(Cliente cliente) {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, contacto = ?, endereco = ?, Sexo = ?, documentoIdentificacao = ? WHERE IdPessoa = ?";
        String sqlCliente = "UPDATE Cliente SET Nacionalidade = ? WHERE IdPessoa = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psPessoa = conn.prepareStatement(sqlPessoa);
            psPessoa.setString(1, cliente.getNome());
            psPessoa.setString(2, cliente.getContacto());
            psPessoa.setString(3, cliente.getEndereco());
            psPessoa.setString(4, cliente.getSexo());
            psPessoa.setString(5, cliente.getDocumentoIdentificacao());
            psPessoa.setInt(6, cliente.getIdPessoa());
            psPessoa.executeUpdate();

            PreparedStatement psCliente = conn.prepareStatement(sqlCliente);
            psCliente.setString(1, cliente.getNacionalidade());
            psCliente.setInt(2, cliente.getIdPessoa());
            psCliente.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("ClienteRepository.atualizar: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) { }
            DatabaseConnection.close(conn);
        }
        return false;
    }

    public boolean eliminar(int idPessoa) {
        String sql = "DELETE FROM pessoa WHERE IdPessoa = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idPessoa);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClienteRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdPessoa(rs.getInt("IdPessoa"));
        c.setNome(rs.getString("nome"));
        c.setContacto(rs.getString("contacto"));
        c.setEndereco(rs.getString("endereco"));
        c.setSexo(rs.getString("Sexo"));
        c.setDocumentoIdentificacao(rs.getString("documentoIdentificacao"));
        c.setNacionalidade(rs.getString("Nacionalidade"));
        return c;
    }
}
