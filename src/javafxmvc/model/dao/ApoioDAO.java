package javafxmvc.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.Gerente;
import javafxmvc.model.domain.Funcionario;
import javafxmvc.model.domain.LinhaProducao;
import javafxmvc.model.domain.LocalArmazenamento;
import javafxmvc.model.domain.TipoCachaca;

public class ApoioDAO extends BaseDAO {
    public List<Gerente> listarGerentesAtivos() {
        List<Gerente> lista = new ArrayList<>();
        String sql = "SELECT * FROM gerente WHERE ativo=true ORDER BY nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Gerente g = new Gerente();
                g.setIdGerente(rs.getInt("id_gerente"));
                g.setNome(rs.getString("nome"));
                lista.add(g);
            }
        } catch (SQLException ex) { Logger.getLogger(ApoioDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    public List<Funcionario> listarFuncionariosAtivos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario WHERE ativo=true ORDER BY nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setIdFuncionario(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                lista.add(f);
            }
        } catch (SQLException ex) { Logger.getLogger(ApoioDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    public List<TipoCachaca> listarTiposAtivos() {
        List<TipoCachaca> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_cachaca WHERE ativo=true ORDER BY nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                TipoCachaca t = new TipoCachaca();
                t.setIdTipo(rs.getInt("id_tipo"));
                t.setNome(rs.getString("nome"));
                t.setTempoEnvelhecimentoDias(rs.getInt("tempo_envelhecimento_dias"));
                lista.add(t);
            }
        } catch (SQLException ex) { Logger.getLogger(ApoioDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    public List<LinhaProducao> listarLinhasAtivas() {
        List<LinhaProducao> lista = new ArrayList<>();
        String sql = "SELECT lp.*, tc.nome AS tipo_nome FROM linha_producao lp JOIN tipo_cachaca tc ON tc.id_tipo=lp.id_tipo WHERE lp.ativa=true ORDER BY lp.descricao";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LinhaProducao l = new LinhaProducao();
                l.setIdLinha(rs.getInt("id_linha"));
                l.setDescricao(rs.getString("descricao"));
                l.setCapacidadeDia(rs.getBigDecimal("capacidade_dia"));
                l.setIdTipo(rs.getInt("id_tipo"));
                l.setTipoNome(rs.getString("tipo_nome"));
                lista.add(l);
            }
        } catch (SQLException ex) { Logger.getLogger(ApoioDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    public List<LocalArmazenamento> listarLocaisAtivos() {
        List<LocalArmazenamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM local_armazenamento WHERE ativo=true ORDER BY nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalArmazenamento l = new LocalArmazenamento();
                l.setIdLocal(rs.getInt("id_local"));
                l.setNome(rs.getString("nome"));
                l.setCapacidadeLitros(rs.getBigDecimal("capacidade_litros"));
                lista.add(l);
            }
        } catch (SQLException ex) { Logger.getLogger(ApoioDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }
}
