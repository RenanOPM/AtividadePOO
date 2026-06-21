package javafxmvc.model.domain;

import java.time.LocalDate;

public class Gerente {
    private int idGerente;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataAdmissao;
    private boolean ativo = true;

    public int getIdGerente() { return idGerente; }
    public void setIdGerente(int idGerente) { this.idGerente = idGerente; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    @Override public String toString() { return nome; }
}
