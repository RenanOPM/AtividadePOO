package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ApoioDAO;
import javafxmvc.model.dao.IndicadoresDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.RelatorioPacotes;
import javafxmvc.model.domain.TipoCachaca;

public class FXMLAnchorPaneRelatoriosPacotesController implements Initializable {

    @FXML private Label lblTotalFeitos, lblTotalArm, lblTotalSaida, lblSaldoEstoque;
    @FXML private ComboBox<TipoCachaca> comboTipo;
    @FXML private TableView<RelatorioPacotes> tableView;
    @FXML private TableColumn<RelatorioPacotes, String>  colTipo;
    @FXML private TableColumn<RelatorioPacotes, Integer> colFeitos, colArmazenamento, colSaida, colSaldo;

    private final Connection connection   = DatabaseFactory.getDatabase("postgresql").conectar();
    private final ApoioDAO apoioDAO       = new ApoioDAO();
    private final IndicadoresDAO indicDAO = new IndicadoresDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        apoioDAO.setConnection(connection);
        indicDAO.setConnection(connection);
        comboTipo.setItems(FXCollections.observableArrayList(apoioDAO.listarTiposAtivos()));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCachaca"));
        colFeitos.setCellValueFactory(new PropertyValueFactory<>("pacotesFeitos"));
        colArmazenamento.setCellValueFactory(new PropertyValueFactory<>("pacotesArmazenamento"));
        colSaida.setCellValueFactory(new PropertyValueFactory<>("pacotesSaidaEstoque"));
        // Coluna Saldo = feitos - saída
        colSaldo.setCellValueFactory(cd ->
                new SimpleIntegerProperty(cd.getValue().getPacotesFeitos() - cd.getValue().getPacotesSaidaEstoque()).asObject());
        carregar();
    }

    @FXML
    public void carregar() {
        String tipo = comboTipo.getValue() == null ? null : comboTipo.getValue().getNome();
        List<RelatorioPacotes> dados = indicDAO.listarRelatorioPacotes(tipo);
        tableView.setItems(FXCollections.observableArrayList(dados));

        int totalFeitos = dados.stream().mapToInt(RelatorioPacotes::getPacotesFeitos).sum();
        int totalArm    = dados.stream().mapToInt(RelatorioPacotes::getPacotesArmazenamento).sum();
        int totalSaida  = dados.stream().mapToInt(RelatorioPacotes::getPacotesSaidaEstoque).sum();
        int saldo       = totalFeitos - totalSaida;

        lblTotalFeitos.setText(String.valueOf(totalFeitos));
        lblTotalArm.setText(String.valueOf(totalArm));
        lblTotalSaida.setText(String.valueOf(totalSaida));
        lblSaldoEstoque.setText(String.valueOf(saldo));
    }

    @FXML
    public void limparFiltro() { comboTipo.setValue(null); carregar(); }
}
