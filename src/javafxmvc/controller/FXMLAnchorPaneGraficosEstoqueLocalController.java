package javafxmvc.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.IndicadoresDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.EstoqueLocal;

public class FXMLAnchorPaneGraficosEstoqueLocalController implements Initializable {

    @FXML private Label lblTotalLocais, lblTotalEngradados, lblTotalLitros;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis eixoLocal;
    @FXML private NumberAxis   eixoEngradados;
    @FXML private TableView<EstoqueLocal> tableView;
    @FXML private TableColumn<EstoqueLocal, String>     colLocal, colStatus;
    @FXML private TableColumn<EstoqueLocal, Integer>    colEngradados;
    @FXML private TableColumn<EstoqueLocal, BigDecimal> colLitros;

    private final Connection connection   = DatabaseFactory.getDatabase("postgresql").conectar();
    private final IndicadoresDAO indicDAO = new IndicadoresDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        indicDAO.setConnection(connection);
        colLocal.setCellValueFactory(new PropertyValueFactory<>("localArmazenamento"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEngradados.setCellValueFactory(new PropertyValueFactory<>("totalEngradados"));
        colLitros.setCellValueFactory(new PropertyValueFactory<>("totalLitros"));
        carregar();
    }

    private void carregar() {
        List<EstoqueLocal> dados = indicDAO.listarEstoquePorLocal();
        tableView.setItems(FXCollections.observableArrayList(dados));

        // Agrupa por local para o gráfico
        Map<String, Integer> porLocal = new LinkedHashMap<>();
        int totalEng = 0; BigDecimal totalLit = BigDecimal.ZERO;
        for (EstoqueLocal e : dados) {
            String local = e.getLocalArmazenamento();
            porLocal.merge(local, e.getTotalEngradados(), Integer::sum);
            totalEng += e.getTotalEngradados();
            if (e.getTotalLitros() != null) totalLit = totalLit.add(e.getTotalLitros());
        }

        barChart.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Engradados");
        porLocal.forEach((local, eng) -> serie.getData().add(new XYChart.Data<>(local, eng)));
        barChart.getData().add(serie);

        // KPIs
        long numLocais = porLocal.values().stream().filter(v -> v > 0).count();
        lblTotalLocais.setText(String.valueOf(porLocal.size()));
        lblTotalEngradados.setText(String.valueOf(totalEng));
        lblTotalLitros.setText(totalLit.setScale(0, RoundingMode.HALF_UP).toPlainString());
    }
}
