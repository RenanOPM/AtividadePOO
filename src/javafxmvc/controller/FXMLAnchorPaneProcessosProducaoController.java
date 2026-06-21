package javafxmvc.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ApoioDAO;
import javafxmvc.model.dao.ProducaoDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.*;
import javafxmvc.model.service.ProducaoService;

public class FXMLAnchorPaneProcessosProducaoController implements Initializable {
    @FXML private DatePicker dpData;
    @FXML private ComboBox<Gerente> comboGerente;
    @FXML private ComboBox<Funcionario> comboFuncionario;
    @FXML private ComboBox<LinhaProducao> comboLinha;
    @FXML private ComboBox<LocalArmazenamento> comboLocal;
    @FXML private TextField txtLitros, txtQtdEngradados, txtLitrosEngradado, txtLote;
    @FXML private TextArea txtObservacao;
    @FXML private CheckBox chkSelagem;
    @FXML private TableView<ProducaoResumo> tableView;
    @FXML private TableColumn<ProducaoResumo, Integer> colCodigo, colEngradados;
    @FXML private TableColumn<ProducaoResumo, String> colData, colTipo, colFuncionario, colLocal;
    @FXML private TableColumn<ProducaoResumo, BigDecimal> colLitros;
    @FXML private Label labelRegras;

    private final Connection connection = DatabaseFactory.getDatabase("postgresql").conectar();
    private final ApoioDAO apoioDAO = new ApoioDAO();
    private final ProducaoDAO producaoDAO = new ProducaoDAO();
    private ProducaoService service;

    @Override public void initialize(URL url, ResourceBundle rb){
        apoioDAO.setConnection(connection); producaoDAO.setConnection(connection); service = new ProducaoService(connection);
        comboGerente.setItems(FXCollections.observableArrayList(apoioDAO.listarGerentesAtivos()));
        comboFuncionario.setItems(FXCollections.observableArrayList(apoioDAO.listarFuncionariosAtivos()));
        comboLinha.setItems(FXCollections.observableArrayList(apoioDAO.listarLinhasAtivas()));
        comboLocal.setItems(FXCollections.observableArrayList(apoioDAO.listarLocaisAtivos()));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("idProducao")); colData.setCellValueFactory(new PropertyValueFactory<>("dataProducao")); colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCachaca")); colFuncionario.setCellValueFactory(new PropertyValueFactory<>("funcionario")); colLocal.setCellValueFactory(new PropertyValueFactory<>("localArmazenamento")); colLitros.setCellValueFactory(new PropertyValueFactory<>("totalLitros")); colEngradados.setCellValueFactory(new PropertyValueFactory<>("qtdEngradados"));
        dpData.setValue(LocalDate.now()); labelRegras.setText("Regras aplicadas: limite de 300 L por funcionário/dia, 1500 L por dia, selagem obrigatória, linha exclusiva por tipo, engradados iguais e checagem de espaço antes do armazenamento."); carregar();
    }
    private void carregar(){ tableView.setItems(FXCollections.observableArrayList(producaoDAO.listarResumo())); }
    @FXML public void handleRegistrar(){ try{
        Gerente g=comboGerente.getValue(); Funcionario f=comboFuncionario.getValue(); LinhaProducao l=comboLinha.getValue(); LocalArmazenamento local=comboLocal.getValue();
        service.registrar(dpData.getValue(), g==null?0:g.getIdGerente(), f==null?0:f.getIdFuncionario(), l==null?0:l.getIdLinha(), local==null?0:local.getIdLocal(), Util.decimal(txtLitros.getText()), Integer.parseInt(txtQtdEngradados.getText().trim()), Util.decimal(txtLitrosEngradado.getText()), chkSelagem.isSelected(), txtObservacao.getText(), txtLote.getText());
        carregar(); handleLimpar(); Util.info("Produção registrada e lote enviado ao armazenamento em envelhecimento.");
    }catch(Exception e){ Util.erro(e.getMessage()); } }
    @FXML public void handleLimpar(){ dpData.setValue(LocalDate.now()); comboGerente.setValue(null); comboFuncionario.setValue(null); comboLinha.setValue(null); comboLocal.setValue(null); txtLitros.clear(); txtQtdEngradados.clear(); txtLitrosEngradado.clear(); txtLote.clear(); txtObservacao.clear(); chkSelagem.setSelected(false); }
}
