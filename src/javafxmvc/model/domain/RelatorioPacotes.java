package javafxmvc.model.domain;

public class RelatorioPacotes {
    private String tipoCachaca;
    private int pacotesFeitos;
    private int pacotesArmazenamento;
    private int pacotesSaidaEstoque;

    public String getTipoCachaca() { return tipoCachaca; }
    public void setTipoCachaca(String tipoCachaca) { this.tipoCachaca = tipoCachaca; }
    public int getPacotesFeitos() { return pacotesFeitos; }
    public void setPacotesFeitos(int pacotesFeitos) { this.pacotesFeitos = pacotesFeitos; }
    public int getPacotesArmazenamento() { return pacotesArmazenamento; }
    public void setPacotesArmazenamento(int pacotesArmazenamento) { this.pacotesArmazenamento = pacotesArmazenamento; }
    public int getPacotesSaidaEstoque() { return pacotesSaidaEstoque; }
    public void setPacotesSaidaEstoque(int pacotesSaidaEstoque) { this.pacotesSaidaEstoque = pacotesSaidaEstoque; }
}
