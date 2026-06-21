package javafxmvc.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.CachacaEstoque;

public class ArmazenamentoDAO extends BaseDAO {
    public List<CachacaEstoque> listar() {
        List<CachacaEstoque> lista = new ArrayList<>();
        String sql = "SELECT c.id_cachaca, c.lote, tc.nome AS tipo, la.nome AS local, c.data_producao, c.data_liberacao, c.quantidade_litros, c.status " +
                "FROM cachaca c JOIN tipo_cachaca tc ON tc.id_tipo=c.id_tipo JOIN local_armazenamento la ON la.id_local=c.id_local " +
                "ORDER BY c.data_liberacao, c.lote";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CachacaEstoque c = new CachacaEstoque();
                c.setIdCachaca(rs.getInt("id_cachaca"));
                c.setLote(rs.getString("lote"));
                c.setTipoCachaca(rs.getString("tipo"));
                c.setLocalArmazenamento(rs.getString("local"));
                c.setDataProducao(toLocalDate(rs.getDate("data_producao")));
                c.setDataLiberacao(toLocalDate(rs.getDate("data_liberacao")));
                c.setQuantidadeLitros(rs.getBigDecimal("quantidade_litros"));
                c.setStatus(rs.getString("status"));
                lista.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ArmazenamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public boolean atualizarStatus(int idCachaca, String status) {
        String sql = "UPDATE cachaca SET status=? WHERE id_cachaca=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idCachaca);
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ArmazenamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
