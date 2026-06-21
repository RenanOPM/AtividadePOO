package javafxmvc.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ApoioDAO;
import javafxmvc.model.dao.IndicadoresDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.RelatorioProducao;
import javafxmvc.model.domain.TipoCachaca;

public class FXMLAnchorPaneRelatoriosProducaoController implements Initializable {

    @FXML private Label lblTotalProducoes, lblTotalLitros, lblTotalEngradados;
    @FXML private ComboBox<TipoCachaca> comboTipo;
    @FXML private DatePicker dpInicio, dpFim;
    @FXML private TableView<RelatorioProducao> tableView;
    @FXML private TableColumn<RelatorioProducao, Integer>    colId, colEngradados;
    @FXML private TableColumn<RelatorioProducao, String>     colData, colTipo, colFuncionario, colGerente, colLocal, colLote, colSelagem;
    @FXML private TableColumn<RelatorioProducao, BigDecimal> colLitros;

    private final Connection connection   = DatabaseFactory.getDatabase("postgresql").conectar();
    private final ApoioDAO apoioDAO       = new ApoioDAO();
    private final IndicadoresDAO indicDAO = new IndicadoresDAO();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apoioDAO.setConnection(connection);
        indicDAO.setConnection(connection);
        comboTipo.setItems(FXCollections.observableArrayList(apoioDAO.listarTiposAtivos()));

        colId.setCellValueFactory(new PropertyValueFactory<>("idProducao"));
        colData.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getDataProducao() != null ? cd.getValue().getDataProducao().format(FMT) : ""));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCachaca"));
        colFuncionario.setCellValueFactory(new PropertyValueFactory<>("funcionario"));
        colGerente.setCellValueFactory(new PropertyValueFactory<>("gerente"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("localArmazenamento"));
        colLitros.setCellValueFactory(new PropertyValueFactory<>("totalLitros"));
        colEngradados.setCellValueFactory(new PropertyValueFactory<>("qtdEngradados"));
        colLote.setCellValueFactory(new PropertyValueFactory<>("lote"));
        colSelagem.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().isSelagemVerificada() ? "Sim ✓" : "Não"));
        carregar();
    }

    @FXML
    public void carregar() {
        String tipo    = comboTipo.getValue() == null ? null : comboTipo.getValue().getNome();
        String inicio  = dpInicio.getValue() != null ? dpInicio.getValue().format(FMT) : null;
        String fim     = dpFim.getValue()    != null ? dpFim.getValue().format(FMT)    : null;

        List<RelatorioProducao> dados = indicDAO.listarRelatorioProducao(tipo, inicio, fim);
        tableView.setItems(FXCollections.observableArrayList(dados));

        BigDecimal totalLitros = dados.stream()
                .map(r -> r.getTotalLitros() != null ? r.getTotalLitros() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalEng = dados.stream().mapToInt(RelatorioProducao::getQtdEngradados).sum();

        lblTotalProducoes.setText(String.valueOf(dados.size()));
        lblTotalLitros.setText(totalLitros.setScale(0, RoundingMode.HALF_UP).toPlainString());
        lblTotalEngradados.setText(String.valueOf(totalEng));
    }

    @FXML
    public void limparFiltro() {
        comboTipo.setValue(null);
        dpInicio.setValue(null);
        dpFim.setValue(null);
        carregar();
    }
}
