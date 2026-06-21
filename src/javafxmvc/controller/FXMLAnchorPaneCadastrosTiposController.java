package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.TipoCachacaDAO;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.TipoCachaca;

public class FXMLAnchorPaneCadastrosTiposController implements Initializable {
    @FXML private TableView<TipoCachaca> tableView;
    @FXML private TableColumn<TipoCachaca, Integer> colCodigo, colDias;
    @FXML private TableColumn<TipoCachaca, String> colNome, colEmbalagem;
    @FXML private TextField txtNome, txtEmbalagem, txtVolume, txtDias;
    @FXML private TextArea txtDescricao;
    @FXML private CheckBox chkAtivo;

    private final Connection connection = DatabaseFactory.getDatabase("postgresql").conectar();
    private final TipoCachacaDAO dao = new TipoCachacaDAO();

    @Override public void initialize(URL url, ResourceBundle rb){
        dao.setConnection(connection); colCodigo.setCellValueFactory(new PropertyValueFactory<>("idTipo")); colNome.setCellValueFactory(new PropertyValueFactory<>("nome")); colEmbalagem.setCellValueFactory(new PropertyValueFactory<>("embalagem")); colDias.setCellValueFactory(new PropertyValueFactory<>("tempoEnvelhecimentoDias"));
        tableView.getSelectionModel().selectedItemProperty().addListener((o,a,t)->preencher(t)); chkAtivo.setSelected(true); carregar();
    }
    private void carregar(){ tableView.setItems(FXCollections.observableArrayList(dao.listar())); }
    private void preencher(TipoCachaca t){ if(t==null)return; txtNome.setText(t.getNome()); txtDescricao.setText(t.getDescricao()); txtEmbalagem.setText(t.getEmbalagem()); txtVolume.setText(String.valueOf(t.getVolumeLitros())); txtDias.setText(String.valueOf(t.getTempoEnvelhecimentoDias())); chkAtivo.setSelected(t.isAtivo()); }
    private TipoCachaca ler(){ TipoCachaca t=tableView.getSelectionModel().getSelectedItem(); if(t==null)t=new TipoCachaca(); t.setNome(txtNome.getText()); t.setDescricao(txtDescricao.getText()); t.setEmbalagem(txtEmbalagem.getText()); t.setVolumeLitros(Util.decimal(txtVolume.getText())); t.setTempoEnvelhecimentoDias(Integer.parseInt(txtDias.getText().trim())); t.setAtivo(chkAtivo.isSelected()); return t; }
    private boolean validar(){ return !txtNome.getText().trim().isEmpty() && !txtEmbalagem.getText().trim().isEmpty() && !txtVolume.getText().trim().isEmpty() && !txtDias.getText().trim().isEmpty(); }
    @FXML public void handleNovo(){ tableView.getSelectionModel().clearSelection(); txtNome.clear(); txtDescricao.clear(); txtEmbalagem.clear(); txtVolume.clear(); txtDias.clear(); chkAtivo.setSelected(true); }
    @FXML public void handleSalvar(){ try{ if(!validar()){Util.erro("Preencha nome, embalagem, volume e dias de envelhecimento.");return;} if(dao.inserir(ler())){carregar();handleNovo();Util.info("Tipo cadastrado.");} else Util.erro("Não foi possível cadastrar."); }catch(Exception e){Util.erro("Confira os valores numéricos.");} }
    @FXML public void handleAlterar(){ try{ if(tableView.getSelectionModel().getSelectedItem()==null){Util.erro("Selecione um tipo.");return;} if(dao.alterar(ler())){carregar();Util.info("Tipo alterado.");} else Util.erro("Não foi possível alterar."); }catch(Exception e){Util.erro("Confira os valores numéricos.");} }
    @FXML public void handleRemover(){ TipoCachaca t=tableView.getSelectionModel().getSelectedItem(); if(t==null){Util.erro("Selecione um tipo.");return;} if(dao.remover(t)){carregar();handleNovo();Util.info("Tipo desativado.");} else Util.erro("Não foi possível desativar."); }
}
