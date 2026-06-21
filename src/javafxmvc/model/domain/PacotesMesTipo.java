package javafxmvc.model.domain;

import java.math.BigDecimal;

public class PacotesMesTipo {
    private String mes;
    private String tipoCachaca;
    private String embalagem;
    private int totalPacotes;
    private BigDecimal totalLitros;

    public String getMes() { return mes; }
    public void setMes(String mes) { this.mes = mes; }
    public String getTipoCachaca() { return tipoCachaca; }
    public void setTipoCachaca(String tipoCachaca) { this.tipoCachaca = tipoCachaca; }
    public String getEmbalagem() { return embalagem; }
    public void setEmbalagem(String embalagem) { this.embalagem = embalagem; }
    public int getTotalPacotes() { return totalPacotes; }
    public void setTotalPacotes(int totalPacotes) { this.totalPacotes = totalPacotes; }
    public BigDecimal getTotalLitros() { return totalLitros; }
    public void setTotalLitros(BigDecimal totalLitros) { this.totalLitros = totalLitros; }
}
