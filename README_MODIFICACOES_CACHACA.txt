SISTEMA DE PRODUCAO DE CACHACA - VERSAO ADAPTADA AO DOCUMENTO DE REQUISITOS

Resumo das alteracoes realizadas:

1. Tema do sistema
- O projeto original de vendas/produtos/clientes foi adaptado para o tema "Producao de Cachaca".
- A janela principal agora se chama "Sistema de Producao de Cachaca - JavaFX MVC".
- O menu principal foi reorganizado em: Cadastros, Processos, Graficos e Relatorios.

2. Cadastros implementados
- Cadastro de Gerentes.
- Cadastro de Funcionarios.
- Cadastro de Tipos de Cachaca.

3. Processos de negocio implementados
- Processo de Producao:
  - Escolha de data, gerente, funcionario, linha de producao, local de armazenamento, lote e quantidade.
  - Registro da producao.
  - Registro automatico do item de producao.
  - Registro automatico do lote na tabela cachaca, ja com status "Em Envelhecimento".
  - Calculo da data de liberacao usando a funcao fn_calcular_data_liberacao do banco.

- Processo de Armazenamento:
  - Listagem dos lotes armazenados.
  - Visualizacao da data de producao e da data de liberacao.
  - Alteracao de status entre Em Envelhecimento, Liberado, Embalo e Vendido.

4. Regras de negocio contempladas
- Um funcionario nao pode estar presente na producao de mais de 300 litros por dia.
- Cada tipo de cachaca deve possuir linha de producao exclusiva, garantida pelo banco e respeitada na tela de producao.
- Os engradados devem possuir a mesma quantidade de litros.
- Todos os pacotes devem ter checagem de selagem antes do registro.
- Antes da producao o sistema consulta o espaco disponivel no local de armazenamento.
- Nao permite produzir/armazenar mais de 1500 litros por dia.
- Ao registrar a cachaca, o sistema calcula quanto tempo ela deve ficar armazenada em envelhecimento antes de ser liberada.

5. Grafico implementado
- Grafico de barras com quantidade de pacotes feitos por mes.
- Filtro por tipo de cachaca.
- Tabela auxiliar com mes, tipo, embalagem, pacotes e litros.

6. Relatorio implementado
- Relatorio em tabela com:
  - Quantidade de pacotes feitos.
  - Quantidade de pacotes em armazenamento.
  - Quantidade de pacotes que sairam do estoque.
- Filtro por tipo de cachaca.

7. Banco de dados
- Foi mantido um script SQL chamado script_cachaca_db.sql na raiz do projeto.
- O arquivo DatabasePostgreSQL.java esta configurado para acessar:
  jdbc:postgresql://127.0.0.1/cachaca_db
  usuario: postgres
  senha: postgres
- Caso o banco tenha outro nome, usuario ou senha, alterar esse arquivo.

8. Observacoes importantes
- O ambiente deste chat nao possui bibliotecas JavaFX instaladas, entao a compilacao visual completa nao pode ser executada aqui.
- A camada model/dao/service foi validada sintaticamente com javac.
- Para executar no NetBeans, importe o projeto, crie o banco PostgreSQL usando script_cachaca_db.sql e confira se a biblioteca JavaFX esta configurada no JDK usado.
