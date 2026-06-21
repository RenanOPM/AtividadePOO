package javafxmvc.model.domain;

import java.math.BigDecimal;

public class TipoCachaca {
    private int idTipo;
    private String nome;
    private String descricao;
    private String embalagem;
    private BigDecimal volumeLitros = BigDecimal.ZERO;
    private int tempoEnvelhecimentoDias;
    private boolean ativo = true;

    public int getIdTipo() { return idTipo; }
    public void setIdTipo(int idTipo) { this.idTipo = idTipo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getEmbalagem() { return embalagem; }
    public void setEmbalagem(String embalagem) { this.embalagem = embalagem; }
    public BigDecimal getVolumeLitros() { return volumeLitros; }
    public void setVolumeLitros(BigDecimal volumeLitros) { this.volumeLitros = volumeLitros; }
    public int getTempoEnvelhecimentoDias() { return tempoEnvelhecimentoDias; }
    public void setTempoEnvelhecimentoDias(int tempoEnvelhecimentoDias) { this.tempoEnvelhecimentoDias = tempoEnvelhecimentoDias; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    @Override public String toString() { return nome; }
}
