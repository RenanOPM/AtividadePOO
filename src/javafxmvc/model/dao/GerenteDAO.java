package javafxmvc.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.Gerente;

public class GerenteDAO extends BaseDAO {
    public boolean inserir(Gerente g) {
        String sql = "INSERT INTO gerente(nome, cpf, email, telefone, data_admissao, ativo) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, g.getNome());
            stmt.setString(2, g.getCpf());
            stmt.setString(3, g.getEmail());
            stmt.setString(4, g.getTelefone());
            stmt.setDate(5, toSqlDate(g.getDataAdmissao()));
            stmt.setBoolean(6, g.isAtivo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(GerenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Gerente g) {
        String sql = "UPDATE gerente SET nome=?, cpf=?, email=?, telefone=?, data_admissao=?, ativo=? WHERE id_gerente=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, g.getNome());
            stmt.setString(2, g.getCpf());
            stmt.setString(3, g.getEmail());
            stmt.setString(4, g.getTelefone());
            stmt.setDate(5, toSqlDate(g.getDataAdmissao()));
            stmt.setBoolean(6, g.isAtivo());
            stmt.setInt(7, g.getIdGerente());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(GerenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Gerente g) {
        String sql = "UPDATE gerente SET ativo=false WHERE id_gerente=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, g.getIdGerente());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(GerenteDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Gerente> listar() {
        List<Gerente> lista = new ArrayList<>();
        String sql = "SELECT * FROM gerente ORDER BY nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Gerente g = new Gerente();
                g.setIdGerente(rs.getInt("id_gerente"));
                g.setNome(rs.getString("nome"));
                g.setCpf(rs.getString("cpf"));
                g.setEmail(rs.getString("email"));
                g.setTelefone(rs.getString("telefone"));
                g.setDataAdmissao(toLocalDate(rs.getDate("data_admissao")));
                g.setAtivo(rs.getBoolean("ativo"));
                lista.add(g);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GerenteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
