package javafxmvc.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxmvc.model.domain.ProducaoResumo;

public class ProducaoDAO extends BaseDAO {
    public boolean verificarLimiteFuncionario(int idFuncionario, LocalDate data, BigDecimal litros) throws SQLException {
        String sql = "SELECT fn_verificar_limite_funcionario(?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFuncionario);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setBigDecimal(3, litros);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean(1);
        }
    }

    public boolean verificarLimiteDiario(LocalDate data, BigDecimal litros) throws SQLException {
        String sql = "SELECT fn_verificar_limite_diario(?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(data));
            stmt.setBigDecimal(2, litros);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getBoolean(1);
        }
    }

    public BigDecimal consultarEspacoOcupado(int idLocal) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantidade_litros),0) FROM cachaca WHERE id_local=? AND status IN ('Em Envelhecimento','Liberado','Embalo')";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idLocal);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    public BigDecimal consultarCapacidadeLocal(int idLocal) throws SQLException {
        String sql = "SELECT capacidade_litros FROM local_armazenamento WHERE id_local=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idLocal);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    public int buscarTipoDaLinha(int idLinha) throws SQLException {
        String sql = "SELECT id_tipo FROM linha_producao WHERE id_linha=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idLinha);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public void registrarProducao(LocalDate data, int idGerente, int idFuncionario, int idLinha, int idLocal,
                                  BigDecimal totalLitros, int qtdEngradados, BigDecimal litrosPorEngradado,
                                  boolean selagemVerificada, String observacao, String lote) throws SQLException {
        connection.setAutoCommit(false);
        try {
            int idProducao;
            String sqlProducao = "INSERT INTO producao(data_producao, observacao, total_litros, id_gerente, id_linha, id_local) VALUES(?,?,?,?,?,?) RETURNING id_producao";
            try (PreparedStatement stmt = connection.prepareStatement(sqlProducao)) {
                stmt.setDate(1, Date.valueOf(data));
                stmt.setString(2, observacao);
                stmt.setBigDecimal(3, totalLitros);
                stmt.setInt(4, idGerente);
                stmt.setInt(5, idLinha);
                stmt.setInt(6, idLocal);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                idProducao = rs.getInt("id_producao");
            }

            String sqlItem = "INSERT INTO item_producao(quantidade_litros, qtd_engradados, litros_por_engradado, selagem_verificada, id_producao, id_funcionario) VALUES(?,?,?,?,?,?)";
            try (PreparedStatement stmt = connection.prepareStatement(sqlItem)) {
                stmt.setBigDecimal(1, totalLitros);
                stmt.setInt(2, qtdEngradados);
                stmt.setBigDecimal(3, litrosPorEngradado);
                stmt.setBoolean(4, selagemVerificada);
                stmt.setInt(5, idProducao);
                stmt.setInt(6, idFuncionario);
                stmt.executeUpdate();
            }

            int idTipo = buscarTipoDaLinha(idLinha);
            String sqlCachaca = "INSERT INTO cachaca(lote, data_producao, data_liberacao, quantidade_litros, status, observacao, id_tipo, id_local, id_producao) " +
                    "VALUES(?, ?, fn_calcular_data_liberacao(?, ?), ?, 'Em Envelhecimento', ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sqlCachaca)) {
                stmt.setString(1, lote);
                stmt.setDate(2, Date.valueOf(data));
                stmt.setInt(3, idTipo);
                stmt.setDate(4, Date.valueOf(data));
                stmt.setBigDecimal(5, totalLitros);
                stmt.setString(6, observacao);
                stmt.setInt(7, idTipo);
                stmt.setInt(8, idLocal);
                stmt.setInt(9, idProducao);
                stmt.executeUpdate();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<ProducaoResumo> listarResumo() {
        List<ProducaoResumo> lista = new ArrayList<>();
        String sql = "SELECT p.id_producao, p.data_producao, tc.nome AS tipo, f.nome AS funcionario, g.nome AS gerente, la.nome AS local, " +
                "p.total_litros, ip.qtd_engradados, ip.litros_por_engradado, ip.selagem_verificada " +
                "FROM producao p JOIN gerente g ON g.id_gerente=p.id_gerente JOIN linha_producao lp ON lp.id_linha=p.id_linha " +
                "JOIN tipo_cachaca tc ON tc.id_tipo=lp.id_tipo JOIN local_armazenamento la ON la.id_local=p.id_local " +
                "JOIN item_producao ip ON ip.id_producao=p.id_producao JOIN funcionario f ON f.id_funcionario=ip.id_funcionario " +
                "ORDER BY p.data_producao DESC, p.id_producao DESC";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ProducaoResumo p = new ProducaoResumo();
                p.setIdProducao(rs.getInt("id_producao"));
                p.setDataProducao(toLocalDate(rs.getDate("data_producao")));
                p.setTipoCachaca(rs.getString("tipo"));
                p.setFuncionario(rs.getString("funcionario"));
                p.setGerente(rs.getString("gerente"));
                p.setLocalArmazenamento(rs.getString("local"));
                p.setTotalLitros(rs.getBigDecimal("total_litros"));
                p.setQtdEngradados(rs.getInt("qtd_engradados"));
                p.setLitrosPorEngradado(rs.getBigDecimal("litros_por_engradado"));
                p.setSelagemVerificada(rs.getBoolean("selagem_verificada"));
                lista.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProducaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}
