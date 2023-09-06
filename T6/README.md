# ANTLR4 implemented CSV validator

O intuito do CSV Validator é que, a partir de uma entrada textual no formato definido, conseguir avaliar se o csv possui em sua totalidade uma estrutura válida, se há duplicação de chaves únicas e/ou há entradas com chaves vazias.

# Trabalho 6 - csvValidator 

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
Nomeado *csvValidator-0.1-jar-with-dependencies.jar*. 

## Como executar
Após criar o *.jar* execute-o terminal com parâmetros input e output, sendo respectivamente os arquivos de entrada para teste e saida 
obtida, e.g.:
```
java -jar  csvValidator-0.1-jar-with-dependencies.jar <input> <output>
```
Ex - chamando a partir de Compiladores/T6:
```
java -jar  ./target/csvValidator-0.1-jar-with-dependencies.jar ./testCases/Input/esboco1.txt ./testCases/Output/saida1.txt
```

O arquivo em input precisa ser um arquivo válido e deve conter a seguinte estrutura: 

IDENT {
    (IDENT: tipo regra?)+
}+

verificar {
    (CADEIA -> IDENT)+
}

Sem os símbolos + (1 ou mais entradas do tipo), parênteses, e ? (opcional)

O arquivo de input podem criar várias definiçoes e validar multiplos csvs contra as múltiplas definições.
Os tipos válidos são inteiro, real, booelano e string (tamanho em chars)
1 único atributo (IDENT: tipo regra?) para cada definição pode ter a regra PK.
CADEIA é o caminho relativo (a partir da pasta onde o programa foi executado) para o csv que será validado contra a definição dada a IDENT

Para mais esclarecimentos cheque os exemplos de teste.
