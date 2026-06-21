package javafxmvc.model.domain;

import java.math.BigDecimal;

public class LitrosPorTipo {
    private String tipoCachaca;
    private BigDecimal totalLitros;
    private double percentual;

    public String getTipoCachaca()  { return tipoCachaca; }
    public void setTipoCachaca(String v) { this.tipoCachaca = v; }
    public BigDecimal getTotalLitros() { return totalLitros; }
    public void setTotalLitros(BigDecimal v) { this.totalLitros = v; }
    public double getPercentual()   { return percentual; }
    public void setPercentual(double v) { this.percentual = v; }
}
