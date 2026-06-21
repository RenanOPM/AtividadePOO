CORREÇÃO DO ERRO DO JASPERREPORTS NO NETBEANS

O erro ocorria porque o NetBeans estava buscando as bibliotecas JasperReports em um caminho absoluto antigo:
C:\Projetos\POO2\Projeto_Cachaca_JavaFX_MVC\lib\jasper.reports\...

Como o projeto foi aberto em outra pasta, por exemplo:
C:\Users\renan\OneDrive\Documentos\NetBeansProjects\...

o build falhava tentando copiar um arquivo que existia dentro do projeto, mas estava referenciado pelo caminho errado.

Arquivos corrigidos:
- nbproject/project.properties
- nbproject/private/private.properties

Agora as referências foram ajustadas para caminhos relativos dentro da própria pasta do projeto:
lib/jasper.reports/jasperreports-6.3.1.jar
lib/jasper.reports/jasperreports-fonts-6.3.1.jar
lib/jasper.reports/jasperreports-javaflow-6.3.1.jar

Depois de abrir no NetBeans, recomenda-se executar:
1. Clean and Build
2. Run
