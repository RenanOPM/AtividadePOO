package javafxmvc.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ApoioDAO;
import javafxmvc.model.dao.IndicadoresDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.PacotesMesTipo;
import javafxmvc.model.domain.TipoCachaca;

public class FXMLAnchorPaneGraficosPacotesMesController implements Initializable {

    @FXML private ComboBox<TipoCachaca> comboTipo;
    @FXML private Label lblTotal;
    @FXML private TabPane tabPane;

    // BarChart
    @FXML private BarChart<String, Number>  barChart;
    @FXML private CategoryAxis eixoMes;
    @FXML private NumberAxis   eixoPacotes;
    @FXML private TableView<PacotesMesTipo> tableView;
    @FXML private TableColumn<PacotesMesTipo, String>     colMes, colTipo, colEmbalagem;
    @FXML private TableColumn<PacotesMesTipo, Integer>    colPacotes;
    @FXML private TableColumn<PacotesMesTipo, BigDecimal> colLitros;

    // LineChart
    @FXML private LineChart<String, Number> lineChart;
    @FXML private CategoryAxis eixoMesLinha;
    @FXML private NumberAxis   eixoLitros;

    private final Connection connection   = DatabaseFactory.getDatabase("postgresql").conectar();
    private final ApoioDAO apoioDAO       = new ApoioDAO();
    private final IndicadoresDAO indicDAO = new IndicadoresDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apoioDAO.setConnection(connection);
        indicDAO.setConnection(connection);
        comboTipo.setItems(FXCollections.observableArrayList(apoioDAO.listarTiposAtivos()));
        colMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCachaca"));
        colEmbalagem.setCellValueFactory(new PropertyValueFactory<>("embalagem"));
        colPacotes.setCellValueFactory(new PropertyValueFactory<>("totalPacotes"));
        colLitros.setCellValueFactory(new PropertyValueFactory<>("totalLitros"));
        carregar();
    }

    @FXML
    public void carregar() {
        String tipo = comboTipo.getValue() == null ? null : comboTipo.getValue().getNome();
        List<PacotesMesTipo> dados = indicDAO.listarPacotesPorMes(tipo);
        tableView.setItems(FXCollections.observableArrayList(dados));

        // Totalizador
        int total = dados.stream().mapToInt(PacotesMesTipo::getTotalPacotes).sum();
        lblTotal.setText("Total: " + total + " pacotes");

        // ── BarChart por tipo/série ───────────────────────────────────────
        barChart.getData().clear();
        Map<String, XYChart.Series<String, Number>> seriesMap = new LinkedHashMap<>();
        for (PacotesMesTipo p : dados) {
            String serieNome = p.getTipoCachaca() != null ? p.getTipoCachaca() : "Sem tipo";
            seriesMap.computeIfAbsent(serieNome, k -> {
                XYChart.Series<String, Number> s = new XYChart.Series<>();
                s.setName(k);
                return s;
            });
            seriesMap.get(serieNome).getData().add(new XYChart.Data<>(p.getMes(), p.getTotalPacotes()));
        }
        barChart.getData().addAll(seriesMap.values());

        // ── LineChart — litros por mês (soma de todos os tipos filtrados) ─
        lineChart.getData().clear();
        Map<String, Double> litrosPorMes = new LinkedHashMap<>();
        for (PacotesMesTipo p : dados) {
            litrosPorMes.merge(p.getMes(),
                    p.getTotalLitros() != null ? p.getTotalLitros().doubleValue() : 0.0, Double::sum);
        }
        XYChart.Series<String, Number> serieLinha = new XYChart.Series<>();
        serieLinha.setName(tipo == null ? "Todos os tipos" : tipo);
        litrosPorMes.forEach((mes, litros) ->
                serieLinha.getData().add(new XYChart.Data<>(mes, litros)));
        lineChart.getData().add(serieLinha);
    }

    @FXML
    public void limparFiltro() { comboTipo.setValue(null); carregar(); }
}
