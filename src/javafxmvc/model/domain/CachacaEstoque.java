package javafxmvc.model.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CachacaEstoque {
    private int idCachaca;
    private String lote;
    private String tipoCachaca;
    private String localArmazenamento;
    private LocalDate dataProducao;
    private LocalDate dataLiberacao;
    private BigDecimal quantidadeLitros;
    private BigDecimal totalLitros;   // alias usado pelo relatório
    private String status;

    public int getIdCachaca() { return idCachaca; }
    public void setIdCachaca(int v) { this.idCachaca = v; }
    public String getLote() { return lote; }
    public void setLote(String v) { this.lote = v; }
    public String getTipoCachaca() { return tipoCachaca; }
    public void setTipoCachaca(String v) { this.tipoCachaca = v; }
    public String getLocalArmazenamento() { return localArmazenamento; }
    public void setLocalArmazenamento(String v) { this.localArmazenamento = v; }
    public LocalDate getDataProducao() { return dataProducao; }
    public void setDataProducao(LocalDate v) { this.dataProducao = v; }
    public LocalDate getDataLiberacao() { return dataLiberacao; }
    public void setDataLiberacao(LocalDate v) { this.dataLiberacao = v; }
    public BigDecimal getQuantidadeLitros() { return quantidadeLitros; }
    public void setQuantidadeLitros(BigDecimal v) { this.quantidadeLitros = v; }
    public BigDecimal getTotalLitros() { return totalLitros != null ? totalLitros : quantidadeLitros; }
    public void setTotalLitros(BigDecimal v) { this.totalLitros = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}
