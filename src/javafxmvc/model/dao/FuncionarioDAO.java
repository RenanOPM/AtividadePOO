package javafxmvc.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.Funcionario;

public class FuncionarioDAO extends BaseDAO {
    public boolean inserir(Funcionario f) {
        String sql = "INSERT INTO funcionario(nome, cpf, email, telefone, cargo, data_admissao, ativo, id_gerente) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            preencher(stmt, f);
            stmt.setInt(8, f.getIdGerente());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Funcionario f) {
        String sql = "UPDATE funcionario SET nome=?, cpf=?, email=?, telefone=?, cargo=?, data_admissao=?, ativo=?, id_gerente=? WHERE id_funcionario=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            preencher(stmt, f);
            stmt.setInt(8, f.getIdGerente());
            stmt.setInt(9, f.getIdFuncionario());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void preencher(PreparedStatement stmt, Funcionario f) throws SQLException {
        stmt.setString(1, f.getNome());
        stmt.setString(2, f.getCpf());
        stmt.setString(3, f.getEmail());
        stmt.setString(4, f.getTelefone());
        stmt.setString(5, f.getCargo());
        stmt.setDate(6, toSqlDate(f.getDataAdmissao()));
        stmt.setBoolean(7, f.isAtivo());
    }

    public boolean remover(Funcionario f) {
        String sql = "UPDATE funcionario SET ativo=false WHERE id_funcionario=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, f.getIdFuncionario());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Funcionario> listar() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT f.*, g.nome AS gerente_nome FROM funcionario f JOIN gerente g ON g.id_gerente=f.id_gerente ORDER BY f.nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setIdFuncionario(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setEmail(rs.getString("email"));
                f.setTelefone(rs.getString("telefone"));
                f.setCargo(rs.getString("cargo"));
                f.setDataAdmissao(toLocalDate(rs.getDate("data_admissao")));
                f.setAtivo(rs.getBoolean("ativo"));
                f.setIdGerente(rs.getInt("id_gerente"));
                f.setGerenteNome(rs.getString("gerente_nome"));
                lista.add(f);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
