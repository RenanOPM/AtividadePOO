package javafxmvc.controller;

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
import javafxmvc.model.dao.FuncionarioDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Funcionario;
import javafxmvc.model.domain.Gerente;

public class FXMLAnchorPaneCadastrosFuncionariosController implements Initializable {
    @FXML private TableView<Funcionario> tableView;
    @FXML private TableColumn<Funcionario, Integer> colCodigo;
    @FXML private TableColumn<Funcionario, String> colNome, colCpf, colCargo, colGerente;
    @FXML private TextField txtNome, txtCpf, txtEmail, txtTelefone, txtCargo;
    @FXML private DatePicker dpAdmissao;
    @FXML private CheckBox chkAtivo;
    @FXML private ComboBox<Gerente> comboGerente;

    private final Connection connection = DatabaseFactory.getDatabase("postgresql").conectar();
    private final FuncionarioDAO dao = new FuncionarioDAO();
    private final ApoioDAO apoioDAO = new ApoioDAO();

    @Override public void initialize(URL url, ResourceBundle rb) {
        dao.setConnection(connection); apoioDAO.setConnection(connection);
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("idFuncionario")); colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf")); colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo")); colGerente.setCellValueFactory(new PropertyValueFactory<>("gerenteNome"));
        tableView.getSelectionModel().selectedItemProperty().addListener((o,a,f)->preencher(f));
        comboGerente.setItems(FXCollections.observableArrayList(apoioDAO.listarGerentesAtivos())); dpAdmissao.setValue(LocalDate.now()); chkAtivo.setSelected(true); carregar();
    }
    private void carregar(){ tableView.setItems(FXCollections.observableArrayList(dao.listar())); }
    private void preencher(Funcionario f){ if(f==null)return; txtNome.setText(f.getNome()); txtCpf.setText(f.getCpf()); txtEmail.setText(f.getEmail()); txtTelefone.setText(f.getTelefone()); txtCargo.setText(f.getCargo()); dpAdmissao.setValue(f.getDataAdmissao()); chkAtivo.setSelected(f.isAtivo()); for(Gerente g: comboGerente.getItems()) if(g.getIdGerente()==f.getIdGerente()) comboGerente.setValue(g); }
    private Funcionario ler(){ Funcionario f=tableView.getSelectionModel().getSelectedItem(); if(f==null)f=new Funcionario(); f.setNome(txtNome.getText()); f.setCpf(txtCpf.getText()); f.setEmail(txtEmail.getText()); f.setTelefone(txtTelefone.getText()); f.setCargo(txtCargo.getText()); f.setDataAdmissao(dpAdmissao.getValue()); f.setAtivo(chkAtivo.isSelected()); if(comboGerente.getValue()!=null) f.setIdGerente(comboGerente.getValue().getIdGerente()); return f; }
    private boolean validar(){ return !txtNome.getText().trim().isEmpty() && comboGerente.getValue()!=null; }
    @FXML public void handleNovo(){ tableView.getSelectionModel().clearSelection(); txtNome.clear(); txtCpf.clear(); txtEmail.clear(); txtTelefone.clear(); txtCargo.clear(); dpAdmissao.setValue(LocalDate.now()); chkAtivo.setSelected(true); comboGerente.setValue(null); }
    @FXML public void handleSalvar(){ if(!validar()){Util.erro("Informe nome e gerente.");return;} if(dao.inserir(ler())){carregar();handleNovo();Util.info("Funcionário cadastrado.");} else Util.erro("Não foi possível cadastrar."); }
    @FXML public void handleAlterar(){ if(tableView.getSelectionModel().getSelectedItem()==null){Util.erro("Selecione um funcionário.");return;} if(dao.alterar(ler())){carregar();Util.info("Funcionário alterado.");} else Util.erro("Não foi possível alterar."); }
    @FXML public void handleRemover(){ Funcionario f=tableView.getSelectionModel().getSelectedItem(); if(f==null){Util.erro("Selecione um funcionário.");return;} if(dao.remover(f)){carregar();handleNovo();Util.info("Funcionário desativado.");} else Util.erro("Não foi possível desativar."); }
}
