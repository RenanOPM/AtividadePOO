package javafxmvc.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ApoioDAO;
import javafxmvc.model.dao.IndicadoresDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.CachacaEstoque;
import javafxmvc.model.domain.TipoCachaca;

public class FXMLAnchorPaneRelatoriosEstoqueController implements Initializable {

    @FXML private Label lblTotalItens, lblLiberados, lblEnvelhecendo, lblVendidos;
    @FXML private ComboBox<TipoCachaca> comboTipo;
    @FXML private TableView<CachacaEstoque> tableView;
    @FXML private TableColumn<CachacaEstoque, Integer>    colId;
    @FXML private TableColumn<CachacaEstoque, String>     colTipo, colStatus, colLocal, colData, colLote;
    @FXML private TableColumn<CachacaEstoque, BigDecimal> colLitros;

    private final Connection connection   = DatabaseFactory.getDatabase("postgresql").conectar();
    private final ApoioDAO apoioDAO       = new ApoioDAO();
    private final IndicadoresDAO indicDAO = new IndicadoresDAO();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apoioDAO.setConnection(connection);
        indicDAO.setConnection(connection);
        comboTipo.setItems(FXCollections.observableArrayList(apoioDAO.listarTiposAtivos()));

        colId.setCellValueFactory(new PropertyValueFactory<>("idCachaca"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCachaca"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("localArmazenamento"));
        colData.setCellValueFactory(cd -> new SimpleStringProperty(
                cd.getValue().getDataProducao() != null ? cd.getValue().getDataProducao().format(FMT) : ""));
        colLitros.setCellValueFactory(new PropertyValueFactory<>("totalLitros"));
        colLote.setCellValueFactory(new PropertyValueFactory<>("lote"));
        carregar();
    }

    @FXML
    public void carregar() {
        String tipo = comboTipo.getValue() == null ? null : comboTipo.getValue().getNome();
        List<CachacaEstoque> dados = indicDAO.listarEstoqueCachaca(tipo);
        tableView.setItems(FXCollections.observableArrayList(dados));

        long liberados   = dados.stream().filter(c -> "Liberado".equals(c.getStatus())).count();
        long envelhecendo = dados.stream().filter(c -> "Em Envelhecimento".equals(c.getStatus())).count();
        long vendidos    = dados.stream().filter(c -> "Vendido".equals(c.getStatus())).count();

        lblTotalItens.setText(String.valueOf(dados.size()));
        lblLiberados.setText(String.valueOf(liberados));
        lblEnvelhecendo.setText(String.valueOf(envelhecendo));
        lblVendidos.setText(String.valueOf(vendidos));
    }

    @FXML
    public void limparFiltro() { comboTipo.setValue(null); carregar(); }
}
