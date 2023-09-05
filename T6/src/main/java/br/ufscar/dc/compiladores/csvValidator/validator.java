package br.ufscar.dc.compiladores.csvValidator;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufscar.dc.compiladores.csvValidator.csvValidatorParser.ScriptContext;


public class validator extends csvValidatorBaseVisitor<Void>{
    public static List<String> ExecutionErrors = new ArrayList<>();
    
    // Adiciona um erro à lista de erros. Recebe um Token e uma 
    // mensagem como parâmetros, obtém o número da linha do token e adiciona 
    // o erro formatado à lista.
    public static void addExecutionError(int line, String msg) {
        ExecutionErrors.add(String.format("Linha %d: %s", line, msg));
    }

    // Adicioa apena uma mensagem, sem linha, a lista de erros
    public static void addExecutionError(String msg) {
        ExecutionErrors.add(String.format("%s", msg));
    }

    // Cria a tabela de escopos
    Scopes nestedScopes = new Scopes();

    // Inicia a validacao, trazendo a tabela do semantico
    public void inicia(Scopes escopo, ScriptContext programa ){
        nestedScopes = escopo;

        visitScript(programa);
    }

    // Valida se a planilha tem um formato valido como definidio no arquivo validado até a análise semantica
    @Override
    public Void visitExecucao(csvValidatorParser.ExecucaoContext ctx){

        if (nestedScopes.runNestedScopes().size() > 1) {
            nestedScopes.giveupScope();
        }
        SymbolTable globalScope = nestedScopes.getCurrentScope();

        // Para cada checagem na lista de analise.
        for(csvValidatorParser.ChecagemContext checagem: ctx.checagem()){
            String caminho = checagem.CADEIA().getText().replaceAll("\"", "");
            SymbolTable tabelaDefinicao = globalScope.check(checagem.IDENT().getText()).definicaoAtributos;

            try {
                BufferedReader reader = new BufferedReader(new FileReader(caminho));
                String line = reader.readLine();
                String[] HeaderCells = line.split(",");

                // Checa se a tabela possui a mesma quantidade de colunas que os atributos definidos
                if(! (tabelaDefinicao.size() == HeaderCells.length)){
                    addExecutionError(checagem.IDENT().getSymbol().getLine(), 
                    "Cabecalho possui um numero diferente de colunas da quantidade de atributos definidos \n");
                }
                else{
                    // Checa se a coluna é um atributo definido
                    for(String atributo: HeaderCells){
                        if(!tabelaDefinicao.exists(atributo.replaceAll(" ", ""))){
                            addExecutionError(checagem.IDENT().getSymbol().getLine(), 
                        String.format("Cabecalho possui coluna nao definida: %s \n", atributo ));
                        }
                    }

                    // Para cada linha checa se os valores são valores válidos para a definicao do atributo
                    List<String> uniqueValues = new ArrayList<>();
                    line = reader.readLine();
                    while(line != null){
                        int columnIndex = 0;

                        String[] Cells = line.split(",");

                        for(String cell: Cells){
                            SymbolTableEntry atributo = tabelaDefinicao.check(HeaderCells[columnIndex].replaceAll(" ", ""));
                            
                            if(atributo != null){

                                // Se o atributo foi definido com regra PK, faz as checagens de unicidade e se o valor não é vazio 
                                if(atributo.regra){
                                    if(cell.replaceAll(" ", "").length() > 0){
                                        if(uniqueValues.contains(cell)){
                                            addExecutionError(String.format("Entrada duplicada, para atributo com regra PK: %s \n",cell));
                                        }
                                        else{
                                            uniqueValues.add(cell);
                                        }
                                        checkType(cell, atributo);
                                    }
                                    else{
                                        addExecutionError(String.format("Entrada inválida, atributo %s nao pode ser vazio\n", atributo.name));
                                    }
                                }else{
                                    checkType(cell, atributo);
                                }
                            }

                            columnIndex++;
                        }

                        line = reader.readLine();
                    }
                }


                reader.close();
            } catch (IOException e) {
                addExecutionError(checagem.IDENT().getSymbol().getLine(), e.toString()+"\n");
            }



        }


        return super.visitExecucao(ctx);
    }

    // Checa se o valor é válido para o tipo definido
    void checkType(String cell, SymbolTableEntry atributo){
        switch (atributo.variableType) {
            case INTEIRO:
                try {
                    Integer.parseInt(cell);
                } catch (Exception e) {
                    addExecutionError("Atributo definido como inteiro mas o valor preenchido foi '" +cell+ "'\n");
                }
                break;
            case REAL:
                try {
                    Float.parseFloat(cell);
                } catch (Exception e) {
                    addExecutionError("Atributo definido como real mas o valor preenchido foi '" +cell+"'\n");
                }
                break;
            case BOOLEANO:
                try {
                    Boolean.parseBoolean(cell);
                } catch (Exception e) {
                   
                   addExecutionError("Atributo definido como booleano mas o valor preenchido foi '" +cell+"'\n");
                }
                break;
            case LITERAL:
                if(cell.length() > atributo.size){
                    addExecutionError("Atributo Literal " + cell + " com tamanho maior que o definido para o atributo \n");
                }
                break;
            default:
                addExecutionError(String.format("foi impossivel determinar o tipo %s para a celula %s \n"  , atributo.name, cell));
                break;
        }

    }
    
}
