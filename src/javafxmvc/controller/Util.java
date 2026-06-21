package javafxmvc.controller;

import java.math.BigDecimal;
import javafx.scene.control.Alert;

public class Util {
    public static void info(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Informação");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void erro(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Erro");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static BigDecimal decimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(valor.trim().replace(",", "."));
    }
}
