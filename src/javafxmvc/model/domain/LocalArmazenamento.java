package javafxmvc.model.domain;

import java.math.BigDecimal;

public class LocalArmazenamento {
    private int idLocal;
    private String nome;
    private BigDecimal capacidadeLitros = BigDecimal.ZERO;

    public int getIdLocal() { return idLocal; }
    public void setIdLocal(int idLocal) { this.idLocal = idLocal; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getCapacidadeLitros() { return capacidadeLitros; }
    public void setCapacidadeLitros(BigDecimal capacidadeLitros) { this.capacidadeLitros = capacidadeLitros; }
    @Override public String toString() { return nome; }
}
