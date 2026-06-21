package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class FXMLVBoxMainController implements Initializable {
    @FXML private AnchorPane anchorPane;

    @Override public void initialize(URL url, ResourceBundle rb) {}

    private void loadView(String fxml) {
        try {
            AnchorPane p = FXMLLoader.load(getClass().getResource("/javafxmvc/view/" + fxml));
            anchorPane.getChildren().setAll(p);
            AnchorPane.setTopAnchor(p, 0.0); AnchorPane.setBottomAnchor(p, 0.0);
            AnchorPane.setLeftAnchor(p, 0.0); AnchorPane.setRightAnchor(p, 0.0);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML public void handleMenuItemCadastrosGerentes()      { loadView("FXMLAnchorPaneCadastrosGerentes.fxml"); }
    @FXML public void handleMenuItemCadastrosFuncionarios()  { loadView("FXMLAnchorPaneCadastrosFuncionarios.fxml"); }
    @FXML public void handleMenuItemCadastrosTipos()         { loadView("FXMLAnchorPaneCadastrosTipos.fxml"); }
    @FXML public void handleMenuItemProcessosProducao()      { loadView("FXMLAnchorPaneProcessosProducao.fxml"); }
    @FXML public void handleMenuItemProcessosArmazenamento() { loadView("FXMLAnchorPaneProcessosArmazenamento.fxml"); }

    // Gráficos
    @FXML public void handleMenuItemGraficosPacotesMes()     { loadView("FXMLAnchorPaneGraficosPacotesMes.fxml"); }
    @FXML public void handleMenuItemGraficosLitrosTipo()     { loadView("FXMLAnchorPaneGraficosLitrosTipo.fxml"); }
    @FXML public void handleMenuItemGraficosEstoqueLocal()   { loadView("FXMLAnchorPaneGraficosEstoqueLocal.fxml"); }

    // Relatórios
    @FXML public void handleMenuItemRelatoriosPacotes()      { loadView("FXMLAnchorPaneRelatoriosPacotes.fxml"); }
    @FXML public void handleMenuItemRelatoriosProducao()     { loadView("FXMLAnchorPaneRelatoriosProducao.fxml"); }
    @FXML public void handleMenuItemRelatoriosEstoque()      { loadView("FXMLAnchorPaneRelatoriosEstoque.fxml"); }
}
