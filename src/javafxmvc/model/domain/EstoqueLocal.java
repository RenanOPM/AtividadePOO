package javafxmvc.model.domain;

import java.math.BigDecimal;

public class EstoqueLocal {
    private String localArmazenamento;
    private int totalEngradados;
    private BigDecimal totalLitros;
    private String status;

    public String getLocalArmazenamento() { return localArmazenamento; }
    public void setLocalArmazenamento(String v) { this.localArmazenamento = v; }
    public int getTotalEngradados() { return totalEngradados; }
    public void setTotalEngradados(int v) { this.totalEngradados = v; }
    public BigDecimal getTotalLitros() { return totalLitros; }
    public void setTotalLitros(BigDecimal v) { this.totalLitros = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}
