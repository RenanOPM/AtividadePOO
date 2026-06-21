package javafxmvc.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ArmazenamentoDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.CachacaEstoque;

public class FXMLAnchorPaneProcessosArmazenamentoController implements Initializable {
    @FXML private TableView<CachacaEstoque> tableView;
    @FXML private TableColumn<CachacaEstoque, String> colLote, colTipo, colLocal, colStatus;
    @FXML private TableColumn<CachacaEstoque, BigDecimal> colLitros;
    @FXML private TableColumn<CachacaEstoque, String> colProducao, colLiberacao;
    @FXML private ComboBox<String> comboStatus;
    @FXML private Label labelDetalhe;

    private final Connection connection = DatabaseFactory.getDatabase("postgresql").conectar();
    private final ArmazenamentoDAO dao = new ArmazenamentoDAO();

    @Override public void initialize(URL url, ResourceBundle rb){
        dao.setConnection(connection);
        colLote.setCellValueFactory(new PropertyValueFactory<>("lote")); colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCachaca")); colLocal.setCellValueFactory(new PropertyValueFactory<>("localArmazenamento")); colLitros.setCellValueFactory(new PropertyValueFactory<>("quantidadeLitros")); colProducao.setCellValueFactory(new PropertyValueFactory<>("dataProducao")); colLiberacao.setCellValueFactory(new PropertyValueFactory<>("dataLiberacao")); colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        comboStatus.setItems(FXCollections.observableArrayList("Em Envelhecimento", "Liberado", "Embalo", "Vendido"));
        tableView.getSelectionModel().selectedItemProperty().addListener((o,a,c)->selecionar(c)); carregar();
    }
    private void carregar(){ tableView.setItems(FXCollections.observableArrayList(dao.listar())); }
    private void selecionar(CachacaEstoque c){ if(c==null){labelDetalhe.setText(""); return;} comboStatus.setValue(c.getStatus()); labelDetalhe.setText("Lote " + c.getLote() + " - retirar para embalo/venda somente após " + c.getDataLiberacao()); }
    @FXML public void handleAtualizarStatus(){ CachacaEstoque c=tableView.getSelectionModel().getSelectedItem(); if(c==null){Util.erro("Selecione um lote.");return;} if(comboStatus.getValue()==null){Util.erro("Selecione um status.");return;} if(dao.atualizarStatus(c.getIdCachaca(), comboStatus.getValue())){carregar();Util.info("Status atualizado.");} else Util.erro("Não foi possível atualizar o status."); }
}
