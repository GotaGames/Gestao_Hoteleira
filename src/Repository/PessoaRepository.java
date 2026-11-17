package Repository;

import Model.Pessoa;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PessoaRepository {

    public int inserir(Pessoa pessoa) {
        String sql = "INSERT INTO pessoa (nome, contacto, endereco, Sexo, documentoIdentificacao) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getContacto());
            ps.setString(3, pessoa.getEndereco());
            ps.setString(4, pessoa.getSexo());
            ps.setString(5, pessoa.getDocumentoIdentificacao());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                pessoa.setIdPessoa(id);
                return id;
            }
        } catch (SQLException e) {
            System.err.println("PessoaRepository.inserir: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Pessoa findById(int idPessoa) {
        String sql = "SELECT * FROM pessoa WHERE IdPessoa = ?";
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
            System.err.println("PessoaRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Pessoa> findAll() {
        String sql = "SELECT * FROM pessoa ORDER BY IdPessoa";
        List<Pessoa> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("PessoaRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public boolean atualizar(Pessoa pessoa) {
        String sql = "UPDATE pessoa SET nome = ?, contacto = ?, endereco = ?, Sexo = ?, documentoIdentificacao = ? WHERE IdPessoa = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pessoa.getNome());
            ps.setString(2, pessoa.getContacto());
            ps.setString(3, pessoa.getEndereco());
            ps.setString(4, pessoa.getSexo());
            ps.setString(5, pessoa.getDocumentoIdentificacao());
            ps.setInt(6, pessoa.getIdPessoa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PessoaRepository.atualizar: " + e.getMessage());
        } finally {
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
            System.err.println("PessoaRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Pessoa mapear(ResultSet rs) throws SQLException {
        Pessoa p = new Pessoa();
        p.setIdPessoa(rs.getInt("IdPessoa"));
        p.setNome(rs.getString("nome"));
        p.setContacto(rs.getString("contacto"));
        p.setEndereco(rs.getString("endereco"));
        p.setSexo(rs.getString("Sexo"));
        p.setDocumentoIdentificacao(rs.getString("documentoIdentificacao"));
        return p;
    }
}