package Repository;

import Model.Funcionario;
import Util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {

    public int inserir(Funcionario funcionario) {
        String sqlPessoa = "INSERT INTO pessoa (nome, contacto, endereco, Sexo, documentoIdentificacao) VALUES (?, ?, ?, ?, ?)";
        String sqlFuncionario = "INSERT INTO Funcionario (IdPessoa, cargo, horarioTrabalho, nivelPermissao, salario) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            psPessoa.setString(1, funcionario.getNome());
            psPessoa.setString(2, funcionario.getContacto());
            psPessoa.setString(3, funcionario.getEndereco());
            psPessoa.setString(4, funcionario.getSexo());
            psPessoa.setString(5, funcionario.getDocumentoIdentificacao());
            psPessoa.executeUpdate();

            ResultSet rs = psPessoa.getGeneratedKeys();
            if (rs.next()) {
                int idPessoa = rs.getInt(1);
                funcionario.setIdPessoa(idPessoa);

                PreparedStatement psFunc = conn.prepareStatement(sqlFuncionario);
                psFunc.setInt(1, idPessoa);
                psFunc.setString(2, funcionario.getCargo());
                psFunc.setInt(3, funcionario.getHorarioTrabalho());
                psFunc.setString(4, funcionario.getNivelPermissao());
                psFunc.setDouble(5, funcionario.getSalario());
                psFunc.executeUpdate();

                conn.commit();
                return idPessoa;
            }
            conn.rollback();
        } catch (SQLException e) {
            System.err.println("FuncionarioRepository.inserir: " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) { }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ignored) { }
            DatabaseConnection.close(conn);
        }
        return -1;
    }

    public Funcionario findById(int idPessoa) {
        String sql = "SELECT p.*, f.cargo, f.horarioTrabalho, f.nivelPermissao, f.salario " +
                "FROM pessoa p INNER JOIN Funcionario f ON p.IdPessoa = f.IdPessoa " +
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
            System.err.println("FuncionarioRepository.findById: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return null;
    }

    public List<Funcionario> findAll() {
        String sql = "SELECT p.*, f.cargo, f.horarioTrabalho, f.nivelPermissao, f.salario " +
                "FROM pessoa p INNER JOIN Funcionario f ON p.IdPessoa = f.IdPessoa " +
                "ORDER BY p.IdPessoa";
        List<Funcionario> lista = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("FuncionarioRepository.findAll: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return lista;
    }

    public boolean atualizar(Funcionario funcionario) {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, contacto = ?, endereco = ?, Sexo = ?, documentoIdentificacao = ? WHERE IdPessoa = ?";
        String sqlFuncionario = "UPDATE Funcionario SET cargo = ?, horarioTrabalho = ?, nivelPermissao = ?, salario = ? WHERE IdPessoa = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement psPessoa = conn.prepareStatement(sqlPessoa);
            psPessoa.setString(1, funcionario.getNome());
            psPessoa.setString(2, funcionario.getContacto());
            psPessoa.setString(3, funcionario.getEndereco());
            psPessoa.setString(4, funcionario.getSexo());
            psPessoa.setString(5, funcionario.getDocumentoIdentificacao());
            psPessoa.setInt(6, funcionario.getIdPessoa());
            psPessoa.executeUpdate();

            PreparedStatement psFunc = conn.prepareStatement(sqlFuncionario);
            psFunc.setString(1, funcionario.getCargo());
            psFunc.setInt(2, funcionario.getHorarioTrabalho());
            psFunc.setString(3, funcionario.getNivelPermissao());
            psFunc.setDouble(4, funcionario.getSalario());
            psFunc.setInt(5, funcionario.getIdPessoa());
            psFunc.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("FuncionarioRepository.atualizar: " + e.getMessage());
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
            System.err.println("FuncionarioRepository.eliminar: " + e.getMessage());
        } finally {
            DatabaseConnection.close(conn);
        }
        return false;
    }

    private Funcionario mapear(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setIdPessoa(rs.getInt("IdPessoa"));
        f.setNome(rs.getString("nome"));
        f.setContacto(rs.getString("contacto"));
        f.setEndereco(rs.getString("endereco"));
        f.setSexo(rs.getString("Sexo"));
        f.setDocumentoIdentificacao(rs.getString("documentoIdentificacao"));
        f.setCargo(rs.getString("cargo"));
        f.setHorarioTrabalho(rs.getInt("horarioTrabalho"));
        f.setNivelPermissao(rs.getString("nivelPermissao"));
        f.setSalario(rs.getDouble("salario"));
        return f;
    }
}