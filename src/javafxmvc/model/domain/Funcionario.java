package javafxmvc.model.domain;

import java.time.LocalDate;

public class Funcionario {
    private int idFuncionario;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String cargo;
    private LocalDate dataAdmissao;
    private boolean ativo = true;
    private int idGerente;
    private String gerenteNome;

    public int getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public int getIdGerente() { return idGerente; }
    public void setIdGerente(int idGerente) { this.idGerente = idGerente; }
    public String getGerenteNome() { return gerenteNome; }
    public void setGerenteNome(String gerenteNome) { this.gerenteNome = gerenteNome; }
    @Override public String toString() { return nome; }
}
