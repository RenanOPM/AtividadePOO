package javafxmvc.model.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RelatorioProducao {
    private int idProducao;
    private LocalDate dataProducao;
    private String tipoCachaca;
    private String funcionario;
    private String gerente;
    private String localArmazenamento;
    private BigDecimal totalLitros;
    private int qtdEngradados;
    private String lote;
    private boolean selagemVerificada;

    public int getIdProducao() { return idProducao; }
    public void setIdProducao(int v) { this.idProducao = v; }
    public LocalDate getDataProducao() { return dataProducao; }
    public void setDataProducao(LocalDate v) { this.dataProducao = v; }
    public String getTipoCachaca() { return tipoCachaca; }
    public void setTipoCachaca(String v) { this.tipoCachaca = v; }
    public String getFuncionario() { return funcionario; }
    public void setFuncionario(String v) { this.funcionario = v; }
    public String getGerente() { return gerente; }
    public void setGerente(String v) { this.gerente = v; }
    public String getLocalArmazenamento() { return localArmazenamento; }
    public void setLocalArmazenamento(String v) { this.localArmazenamento = v; }
    public BigDecimal getTotalLitros() { return totalLitros; }
    public void setTotalLitros(BigDecimal v) { this.totalLitros = v; }
    public int getQtdEngradados() { return qtdEngradados; }
    public void setQtdEngradados(int v) { this.qtdEngradados = v; }
    public String getLote() { return lote; }
    public void setLote(String v) { this.lote = v; }
    public boolean isSelagemVerificada() { return selagemVerificada; }
    public void setSelagemVerificada(boolean v) { this.selagemVerificada = v; }
}
