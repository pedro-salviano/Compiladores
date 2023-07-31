# Trabalho 4 - Analisador Semântico

Grupo:

Melissa Campelo Amora Fontenelle - RA: 769824

Nayra Kaline Santos Vidal - RA: 769847

Pedro Augusto Benevides Salviano - RA: 790983

## Dependencias
- OpenJDK (>= 11)
- Antlr4 (>= 4.10.1)
- Maven (>= 3.6.3)

## Para compilar/executar
1. Usar mvn package para ler o código java e criar um pacote distribuível, no nosso caso em .jar.
2. Executar com 2 parâmetros, caminho dos arquivos de entrada e saída.

## Build
Para criar o *.jar* execute o comando a baixo na raiz do projeto.
```bash
mvn package
```
Então o *.jar* estará disponível na pasta target.
Nomeado *LA-1.2-SNAPSHOT-jar-with-dependencies.jar*. 

## Como executar
Após criar o *.jar* execute-o terminal com parâmetros input e output sendo respectivamente os arquivos de entrada para teste e saida 
obtida, e.g.:
```
java -jar  LA-1.2-SNAPSHOT-jar-with-dependencies.jar <input> <output>
```
O arquivo em input deve ser um arquivo válido.