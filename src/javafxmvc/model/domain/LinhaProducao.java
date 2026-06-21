package javafxmvc.model.domain;

import java.math.BigDecimal;

public class LinhaProducao {
    private int idLinha;
    private String descricao;
    private BigDecimal capacidadeDia = BigDecimal.ZERO;
    private int idTipo;
    private String tipoNome;

    public int getIdLinha() { return idLinha; }
    public void setIdLinha(int idLinha) { this.idLinha = idLinha; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getCapacidadeDia() { return capacidadeDia; }
    public void setCapacidadeDia(BigDecimal capacidadeDia) { this.capacidadeDia = capacidadeDia; }
    public int getIdTipo() { return idTipo; }
    public void setIdTipo(int idTipo) { this.idTipo = idTipo; }
    public String getTipoNome() { return tipoNome; }
    public void setTipoNome(String tipoNome) { this.tipoNome = tipoNome; }
    @Override public String toString() { return descricao + " - " + tipoNome; }
}
