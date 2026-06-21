package javafxmvc.model.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import javafxmvc.model.dao.ProducaoDAO;

public class ProducaoService {
    private final ProducaoDAO producaoDAO = new ProducaoDAO();

    public ProducaoService(Connection connection) {
        producaoDAO.setConnection(connection);
    }

    public void registrar(LocalDate data, int idGerente, int idFuncionario, int idLinha, int idLocal,
                          BigDecimal totalLitros, int qtdEngradados, BigDecimal litrosPorEngradado,
                          boolean selagemVerificada, String observacao, String lote) throws Exception {
        if (data == null) throw new Exception("Informe a data da produção.");
        if (idGerente <= 0) throw new Exception("Selecione um gerente.");
        if (idFuncionario <= 0) throw new Exception("Selecione um funcionário.");
        if (idLinha <= 0) throw new Exception("Selecione uma linha de produção.");
        if (idLocal <= 0) throw new Exception("Selecione um local de armazenamento.");
        if (totalLitros == null || totalLitros.compareTo(BigDecimal.ZERO) <= 0) throw new Exception("Informe uma quantidade de litros válida.");
        if (qtdEngradados <= 0) throw new Exception("Informe a quantidade de engradados.");
        if (litrosPorEngradado == null || litrosPorEngradado.compareTo(BigDecimal.ZERO) <= 0) throw new Exception("Informe os litros por engradado.");
        if (!selagemVerificada) throw new Exception("Todos os pacotes precisam ter checagem de selagem antes do registro.");
        if (lote == null || lote.trim().isEmpty()) throw new Exception("Informe o lote da cachaça.");

        BigDecimal calculado = litrosPorEngradado.multiply(new BigDecimal(qtdEngradados));
        if (calculado.compareTo(totalLitros) != 0) {
            throw new Exception("A quantidade total deve ser igual a: qtd. engradados x litros por engradado. Isso garante que todos os engradados tenham a mesma quantidade de litros.");
        }

        try {
            if (!producaoDAO.verificarLimiteFuncionario(idFuncionario, data, totalLitros)) {
                throw new Exception("Regra de negócio: um funcionário não pode participar de produção acima de 300 litros por dia.");
            }
            if (!producaoDAO.verificarLimiteDiario(data, totalLitros)) {
                throw new Exception("Regra de negócio: não é permitido produzir/armazenar mais de 1500 litros por dia.");
            }
            BigDecimal capacidade = producaoDAO.consultarCapacidadeLocal(idLocal);
            BigDecimal ocupado = producaoDAO.consultarEspacoOcupado(idLocal);
            if (ocupado.add(totalLitros).compareTo(capacidade) > 0) {
                throw new Exception("Regra de negócio: não há espaço disponível no local de armazenamento selecionado.");
            }
            producaoDAO.registrarProducao(data, idGerente, idFuncionario, idLinha, idLocal, totalLitros, qtdEngradados, litrosPorEngradado, selagemVerificada, observacao, lote);
        } catch (SQLException ex) {
            throw new Exception("Erro ao gravar a produção: " + ex.getMessage(), ex);
        }
    }
}
