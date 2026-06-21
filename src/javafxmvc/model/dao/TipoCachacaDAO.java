package javafxmvc.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.TipoCachaca;

public class TipoCachacaDAO extends BaseDAO {
    public boolean inserir(TipoCachaca t) {
        String sql = "INSERT INTO tipo_cachaca(nome, descricao, embalagem, volume_litros, tempo_envelhecimento_dias, ativo) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            preencher(stmt, t);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoCachacaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(TipoCachaca t) {
        String sql = "UPDATE tipo_cachaca SET nome=?, descricao=?, embalagem=?, volume_litros=?, tempo_envelhecimento_dias=?, ativo=? WHERE id_tipo=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            preencher(stmt, t);
            stmt.setInt(7, t.getIdTipo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoCachacaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private void preencher(PreparedStatement stmt, TipoCachaca t) throws SQLException {
        stmt.setString(1, t.getNome());
        stmt.setString(2, t.getDescricao());
        stmt.setString(3, t.getEmbalagem());
        stmt.setBigDecimal(4, t.getVolumeLitros());
        stmt.setInt(5, t.getTempoEnvelhecimentoDias());
        stmt.setBoolean(6, t.isAtivo());
    }

    public boolean remover(TipoCachaca t) {
        String sql = "UPDATE tipo_cachaca SET ativo=false WHERE id_tipo=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, t.getIdTipo());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TipoCachacaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<TipoCachaca> listar() {
        List<TipoCachaca> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_cachaca ORDER BY nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TipoCachaca t = new TipoCachaca();
                t.setIdTipo(rs.getInt("id_tipo"));
                t.setNome(rs.getString("nome"));
                t.setDescricao(rs.getString("descricao"));
                t.setEmbalagem(rs.getString("embalagem"));
                t.setVolumeLitros(rs.getBigDecimal("volume_litros"));
                t.setTempoEnvelhecimentoDias(rs.getInt("tempo_envelhecimento_dias"));
                t.setAtivo(rs.getBoolean("ativo"));
                lista.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoCachacaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
