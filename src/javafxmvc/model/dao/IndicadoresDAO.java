package javafxmvc.model.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.EstoqueLocal;
import javafxmvc.model.domain.LitrosPorTipo;
import javafxmvc.model.domain.PacotesMesTipo;
import javafxmvc.model.domain.RelatorioPacotes;
import javafxmvc.model.domain.RelatorioProducao;

public class IndicadoresDAO extends BaseDAO {

    // ── Pacotes por mês e tipo (gráfico de barras / linha) ────────────────
    public List<PacotesMesTipo> listarPacotesPorMes(String tipo) {
        List<PacotesMesTipo> lista = new ArrayList<>();
        String sql = "SELECT TO_CHAR(mes, 'MM/YYYY') AS mes_formatado, tipo_cachaca, embalagem, total_pacotes, total_litros " +
                "FROM vw_pacotes_mes_tipo WHERE (? IS NULL OR tipo_cachaca = ?) ORDER BY mes_formatado, tipo_cachaca";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (tipo == null || tipo.trim().isEmpty()) {
                stmt.setNull(1, java.sql.Types.VARCHAR); stmt.setNull(2, java.sql.Types.VARCHAR);
            } else { stmt.setString(1, tipo); stmt.setString(2, tipo); }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PacotesMesTipo p = new PacotesMesTipo();
                p.setMes(rs.getString("mes_formatado"));
                p.setTipoCachaca(rs.getString("tipo_cachaca"));
                p.setEmbalagem(rs.getString("embalagem"));
                p.setTotalPacotes(rs.getInt("total_pacotes"));
                p.setTotalLitros(rs.getBigDecimal("total_litros"));
                lista.add(p);
            }
        } catch (SQLException ex) { Logger.getLogger(IndicadoresDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    // ── Relatório de movimentação de pacotes ──────────────────────────────
    public List<RelatorioPacotes> listarRelatorioPacotes(String tipo) {
        List<RelatorioPacotes> lista = new ArrayList<>();
        String sql = "SELECT tc.nome AS tipo_cachaca, " +
                "COALESCE(SUM(ip.qtd_engradados),0) AS pacotes_feitos, " +
                "COALESCE(SUM(CASE WHEN c.status IN ('Em Envelhecimento','Liberado','Embalo') THEN ip.qtd_engradados ELSE 0 END),0) AS pacotes_armazenamento, " +
                "COALESCE(SUM(CASE WHEN c.status = 'Vendido' THEN ip.qtd_engradados ELSE 0 END),0) AS pacotes_saida_estoque " +
                "FROM tipo_cachaca tc " +
                "LEFT JOIN linha_producao lp ON lp.id_tipo=tc.id_tipo " +
                "LEFT JOIN producao p ON p.id_linha=lp.id_linha " +
                "LEFT JOIN item_producao ip ON ip.id_producao=p.id_producao " +
                "LEFT JOIN cachaca c ON c.id_producao=p.id_producao " +
                "WHERE (? IS NULL OR tc.nome = ?) GROUP BY tc.nome ORDER BY tc.nome";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (tipo == null || tipo.trim().isEmpty()) {
                stmt.setNull(1, java.sql.Types.VARCHAR); stmt.setNull(2, java.sql.Types.VARCHAR);
            } else { stmt.setString(1, tipo); stmt.setString(2, tipo); }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RelatorioPacotes r = new RelatorioPacotes();
                r.setTipoCachaca(rs.getString("tipo_cachaca"));
                r.setPacotesFeitos(rs.getInt("pacotes_feitos"));
                r.setPacotesArmazenamento(rs.getInt("pacotes_armazenamento"));
                r.setPacotesSaidaEstoque(rs.getInt("pacotes_saida_estoque"));
                lista.add(r);
            }
        } catch (SQLException ex) { Logger.getLogger(IndicadoresDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    // ── Litros por tipo (gráfico pizza) ──────────────────────────────────
    public List<LitrosPorTipo> listarLitrosPorTipo() {
        List<LitrosPorTipo> lista = new ArrayList<>();
        String sql = "SELECT tc.nome AS tipo_cachaca, " +
                "COALESCE(SUM(p.total_litros), 0) AS total_litros " +
                "FROM tipo_cachaca tc " +
                "LEFT JOIN linha_producao lp ON lp.id_tipo = tc.id_tipo " +
                "LEFT JOIN producao p ON p.id_linha = lp.id_linha " +
                "WHERE tc.ativo = true " +
                "GROUP BY tc.nome ORDER BY total_litros DESC";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            BigDecimal totalGeral = BigDecimal.ZERO;
            List<Object[]> temp = new ArrayList<>();
            while (rs.next()) {
                BigDecimal litros = rs.getBigDecimal("total_litros");
                if (litros == null) litros = BigDecimal.ZERO;
                totalGeral = totalGeral.add(litros);
                temp.add(new Object[]{rs.getString("tipo_cachaca"), litros});
            }
            for (Object[] row : temp) {
                LitrosPorTipo l = new LitrosPorTipo();
                l.setTipoCachaca((String) row[0]);
                l.setTotalLitros((BigDecimal) row[1]);
                double perc = totalGeral.compareTo(BigDecimal.ZERO) == 0 ? 0
                        : ((BigDecimal) row[1]).doubleValue() / totalGeral.doubleValue() * 100;
                l.setPercentual(Math.round(perc * 10.0) / 10.0);
                lista.add(l);
            }
        } catch (SQLException ex) { Logger.getLogger(IndicadoresDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    // ── Estoque por local de armazenamento (gráfico de barras) ───────────
    public List<EstoqueLocal> listarEstoquePorLocal() {
        List<EstoqueLocal> lista = new ArrayList<>();
        String sql = "SELECT la.nome AS local_armazenamento, " +
                "COALESCE(SUM(ip.qtd_engradados), 0) AS total_engradados, " +
                "COALESCE(SUM(p.total_litros), 0) AS total_litros, " +
                "c.status " +
                "FROM local_armazenamento la " +
                "LEFT JOIN producao p ON p.id_local = la.id_local " +
                "LEFT JOIN item_producao ip ON ip.id_producao = p.id_producao " +
                "LEFT JOIN cachaca c ON c.id_producao = p.id_producao " +
                "WHERE la.ativo = true " +
                "GROUP BY la.nome, c.status ORDER BY la.nome";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                EstoqueLocal e = new EstoqueLocal();
                e.setLocalArmazenamento(rs.getString("local_armazenamento"));
                e.setTotalEngradados(rs.getInt("total_engradados"));
                e.setTotalLitros(rs.getBigDecimal("total_litros"));
                e.setStatus(rs.getString("status"));
                lista.add(e);
            }
        } catch (SQLException ex) { Logger.getLogger(IndicadoresDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    // ── Relatório de produção detalhada ───────────────────────────────────
    public List<RelatorioProducao> listarRelatorioProducao(String tipo, String dataInicio, String dataFim) {
        List<RelatorioProducao> lista = new ArrayList<>();
        String sql = "SELECT p.id_producao, p.data_producao, tc.nome AS tipo_cachaca, " +
                "f.nome AS funcionario, g.nome AS gerente, la.nome AS local_armazenamento, " +
                "p.total_litros, COALESCE(SUM(ip.qtd_engradados),0) AS qtd_engradados, " +
                "p.lote, p.selagem_verificada " +
                "FROM producao p " +
                "JOIN linha_producao lp ON lp.id_linha = p.id_linha " +
                "JOIN tipo_cachaca tc ON tc.id_tipo = lp.id_tipo " +
                "JOIN funcionario f ON f.id_funcionario = p.id_funcionario " +
                "JOIN gerente g ON g.id_gerente = p.id_gerente " +
                "JOIN local_armazenamento la ON la.id_local = p.id_local " +
                "LEFT JOIN item_producao ip ON ip.id_producao = p.id_producao " +
                "WHERE (? IS NULL OR tc.nome = ?) " +
                "  AND (? IS NULL OR p.data_producao >= TO_DATE(?, 'DD/MM/YYYY')) " +
                "  AND (? IS NULL OR p.data_producao <= TO_DATE(?, 'DD/MM/YYYY')) " +
                "GROUP BY p.id_producao, p.data_producao, tc.nome, f.nome, g.nome, la.nome, p.total_litros, p.lote, p.selagem_verificada " +
                "ORDER BY p.data_producao DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (tipo == null || tipo.trim().isEmpty()) { stmt.setNull(1, java.sql.Types.VARCHAR); stmt.setNull(2, java.sql.Types.VARCHAR); }
            else { stmt.setString(1, tipo); stmt.setString(2, tipo); }
            if (dataInicio == null || dataInicio.trim().isEmpty()) { stmt.setNull(3, java.sql.Types.VARCHAR); stmt.setNull(4, java.sql.Types.VARCHAR); }
            else { stmt.setString(3, dataInicio); stmt.setString(4, dataInicio); }
            if (dataFim == null || dataFim.trim().isEmpty()) { stmt.setNull(5, java.sql.Types.VARCHAR); stmt.setNull(6, java.sql.Types.VARCHAR); }
            else { stmt.setString(5, dataFim); stmt.setString(6, dataFim); }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RelatorioProducao r = new RelatorioProducao();
                r.setIdProducao(rs.getInt("id_producao"));
                r.setDataProducao(rs.getDate("data_producao").toLocalDate());
                r.setTipoCachaca(rs.getString("tipo_cachaca"));
                r.setFuncionario(rs.getString("funcionario"));
                r.setGerente(rs.getString("gerente"));
                r.setLocalArmazenamento(rs.getString("local_armazenamento"));
                r.setTotalLitros(rs.getBigDecimal("total_litros"));
                r.setQtdEngradados(rs.getInt("qtd_engradados"));
                r.setLote(rs.getString("lote"));
                r.setSelagemVerificada(rs.getBoolean("selagem_verificada"));
                lista.add(r);
            }
        } catch (SQLException ex) { Logger.getLogger(IndicadoresDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }

    // ── Estoque de cachaça (relatório) ────────────────────────────────────
    public List<javafxmvc.model.domain.CachacaEstoque> listarEstoqueCachaca(String tipo) {
        List<javafxmvc.model.domain.CachacaEstoque> lista = new ArrayList<>();
        String sql = "SELECT c.id_cachaca, tc.nome AS tipo_cachaca, c.status, la.nome AS local_armazenamento, " +
                "p.data_producao, p.total_litros, p.lote " +
                "FROM cachaca c " +
                "JOIN producao p ON p.id_producao = c.id_producao " +
                "JOIN linha_producao lp ON lp.id_linha = p.id_linha " +
                "JOIN tipo_cachaca tc ON tc.id_tipo = lp.id_tipo " +
                "JOIN local_armazenamento la ON la.id_local = p.id_local " +
                "WHERE (? IS NULL OR tc.nome = ?) " +
                "ORDER BY p.data_producao DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (tipo == null || tipo.trim().isEmpty()) { stmt.setNull(1, java.sql.Types.VARCHAR); stmt.setNull(2, java.sql.Types.VARCHAR); }
            else { stmt.setString(1, tipo); stmt.setString(2, tipo); }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                javafxmvc.model.domain.CachacaEstoque ce = new javafxmvc.model.domain.CachacaEstoque();
                ce.setIdCachaca(rs.getInt("id_cachaca"));
                ce.setTipoCachaca(rs.getString("tipo_cachaca"));
                ce.setStatus(rs.getString("status"));
                ce.setLocalArmazenamento(rs.getString("local_armazenamento"));
                ce.setDataProducao(rs.getDate("data_producao").toLocalDate());
                ce.setTotalLitros(rs.getBigDecimal("total_litros"));
                ce.setLote(rs.getString("lote"));
                lista.add(ce);
            }
        } catch (SQLException ex) { Logger.getLogger(IndicadoresDAO.class.getName()).log(Level.SEVERE, null, ex); }
        return lista;
    }
}
