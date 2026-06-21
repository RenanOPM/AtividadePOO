package javafxmvc.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.IndicadoresDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.LitrosPorTipo;

public class FXMLAnchorPaneGraficosLitrosTipoController implements Initializable {

    @FXML private Label lblTotalLitros, lblTipos, lblMediaLitros;
    @FXML private PieChart pieChart;
    @FXML private TableView<LitrosPorTipo> tableView;
    @FXML private TableColumn<LitrosPorTipo, String>     colTipo;
    @FXML private TableColumn<LitrosPorTipo, BigDecimal> colLitros;
    @FXML private TableColumn<LitrosPorTipo, Double>     colPerc;

    private final Connection connection   = DatabaseFactory.getDatabase("postgresql").conectar();
    private final IndicadoresDAO indicDAO = new IndicadoresDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        indicDAO.setConnection(connection);
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCachaca"));
        colLitros.setCellValueFactory(new PropertyValueFactory<>("totalLitros"));
        colPerc.setCellValueFactory(new PropertyValueFactory<>("percentual"));
        carregar();
    }

    private void carregar() {
        List<LitrosPorTipo> dados = indicDAO.listarLitrosPorTipo();
        tableView.setItems(FXCollections.observableArrayList(dados));

        pieChart.getData().clear();
        BigDecimal total = BigDecimal.ZERO;
        for (LitrosPorTipo l : dados) {
            if (l.getTotalLitros() != null && l.getTotalLitros().compareTo(BigDecimal.ZERO) > 0) {
                pieChart.getData().add(new PieChart.Data(
                        l.getTipoCachaca() + " (" + l.getPercentual() + "%)",
                        l.getTotalLitros().doubleValue()));
                total = total.add(l.getTotalLitros());
            }
        }

        lblTotalLitros.setText(total.setScale(0, RoundingMode.HALF_UP).toPlainString());
        lblTipos.setText(String.valueOf(dados.size()));
        BigDecimal media = dados.isEmpty() ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(dados.size()), 0, RoundingMode.HALF_UP);
        lblMediaLitros.setText(media.toPlainString());
    }
}
