package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.GerenteDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Gerente;

public class FXMLAnchorPaneCadastrosGerentesController implements Initializable {
    @FXML private TableView<Gerente> tableView;
    @FXML private TableColumn<Gerente, Integer> colCodigo;
    @FXML private TableColumn<Gerente, String> colNome;
    @FXML private TableColumn<Gerente, String> colCpf;
    @FXML private TableColumn<Gerente, String> colEmail;
    @FXML private TextField txtNome, txtCpf, txtEmail, txtTelefone;
    @FXML private DatePicker dpAdmissao;
    @FXML private CheckBox chkAtivo;

    private final Connection connection = DatabaseFactory.getDatabase("postgresql").conectar();
    private final GerenteDAO dao = new GerenteDAO();

    @Override public void initialize(URL url, ResourceBundle rb) {
        dao.setConnection(connection);
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("idGerente"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableView.getSelectionModel().selectedItemProperty().addListener((o, a, g) -> preencher(g));
        dpAdmissao.setValue(LocalDate.now());
        chkAtivo.setSelected(true);
        carregar();
    }

    private void carregar() { List<Gerente> lista = dao.listar(); tableView.setItems(FXCollections.observableArrayList(lista)); }
    private void preencher(Gerente g) {
        if (g == null) return;
        txtNome.setText(g.getNome()); txtCpf.setText(g.getCpf()); txtEmail.setText(g.getEmail()); txtTelefone.setText(g.getTelefone());
        dpAdmissao.setValue(g.getDataAdmissao()); chkAtivo.setSelected(g.isAtivo());
    }
    private Gerente ler() {
        Gerente g = tableView.getSelectionModel().getSelectedItem(); if (g == null) g = new Gerente();
        g.setNome(txtNome.getText()); g.setCpf(txtCpf.getText()); g.setEmail(txtEmail.getText()); g.setTelefone(txtTelefone.getText());
        g.setDataAdmissao(dpAdmissao.getValue()); g.setAtivo(chkAtivo.isSelected()); return g;
    }
    private boolean validar() { return txtNome.getText()!=null && !txtNome.getText().trim().isEmpty() && txtCpf.getText()!=null && !txtCpf.getText().trim().isEmpty(); }
    @FXML public void handleNovo() { tableView.getSelectionModel().clearSelection(); txtNome.clear(); txtCpf.clear(); txtEmail.clear(); txtTelefone.clear(); dpAdmissao.setValue(LocalDate.now()); chkAtivo.setSelected(true); }
    @FXML public void handleSalvar() { if(!validar()){Util.erro("Informe nome e CPF.");return;} if(dao.inserir(ler())){carregar();handleNovo();Util.info("Gerente cadastrado.");} else Util.erro("Não foi possível cadastrar o gerente."); }
    @FXML public void handleAlterar() { Gerente g = tableView.getSelectionModel().getSelectedItem(); if(g==null){Util.erro("Selecione um gerente.");return;} if(dao.alterar(ler())){carregar();Util.info("Gerente alterado.");} else Util.erro("Não foi possível alterar."); }
    @FXML public void handleRemover() { Gerente g = tableView.getSelectionModel().getSelectedItem(); if(g==null){Util.erro("Selecione um gerente.");return;} if(dao.remover(g)){carregar();handleNovo();Util.info("Gerente desativado.");} else Util.erro("Não foi possível desativar."); }
}
