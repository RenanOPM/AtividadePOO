package javafxmvc.model.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProducaoResumo {
    private int idProducao;
    private LocalDate dataProducao;
    private String tipoCachaca;
    private String funcionario;
    private String gerente;
    private String localArmazenamento;
    private BigDecimal totalLitros;
    private int qtdEngradados;
    private BigDecimal litrosPorEngradado;
    private boolean selagemVerificada;

    public int getIdProducao() { return idProducao; }
    public void setIdProducao(int idProducao) { this.idProducao = idProducao; }
    public LocalDate getDataProducao() { return dataProducao; }
    public void setDataProducao(LocalDate dataProducao) { this.dataProducao = dataProducao; }
    public String getTipoCachaca() { return tipoCachaca; }
    public void setTipoCachaca(String tipoCachaca) { this.tipoCachaca = tipoCachaca; }
    public String getFuncionario() { return funcionario; }
    public void setFuncionario(String funcionario) { this.funcionario = funcionario; }
    public String getGerente() { return gerente; }
    public void setGerente(String gerente) { this.gerente = gerente; }
    public String getLocalArmazenamento() { return localArmazenamento; }
    public void setLocalArmazenamento(String localArmazenamento) { this.localArmazenamento = localArmazenamento; }
    public BigDecimal getTotalLitros() { return totalLitros; }
    public void setTotalLitros(BigDecimal totalLitros) { this.totalLitros = totalLitros; }
    public int getQtdEngradados() { return qtdEngradados; }
    public void setQtdEngradados(int qtdEngradados) { this.qtdEngradados = qtdEngradados; }
    public BigDecimal getLitrosPorEngradado() { return litrosPorEngradado; }
    public void setLitrosPorEngradado(BigDecimal litrosPorEngradado) { this.litrosPorEngradado = litrosPorEngradado; }
    public boolean isSelagemVerificada() { return selagemVerificada; }
    public void setSelagemVerificada(boolean selagemVerificada) { this.selagemVerificada = selagemVerificada; }
}
