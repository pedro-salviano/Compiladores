package br.ufscar.dc.compiladores.csvValidator;



import br.ufscar.dc.compiladores.csvValidator.SymbolTable.TypeCSVAtribute;
import br.ufscar.dc.compiladores.csvValidator.SymbolTable.TypeCSVIdent;


public class csvVisitor extends csvValidatorBaseVisitor<Void>{
    Scopes nestedScopes = new Scopes();

    //define o tipo e add ao escopo
    Boolean defineTypeAndAddtoScope(String variableIdentifier, String variableType, SymbolTable symbolTable, int size, boolean regra){
        switch (variableType) {
            case "inteiro":
                symbolTable.put(variableIdentifier, TypeCSVIdent.ATRIBUTO, TypeCSVAtribute.INTEIRO, regra);
                break;
            case "string":
                symbolTable.put(variableIdentifier, TypeCSVIdent.ATRIBUTO, TypeCSVAtribute.LITERAL, regra, size);
                break;
            case "real":
                symbolTable.put(variableIdentifier, TypeCSVIdent.ATRIBUTO, TypeCSVAtribute.REAL, regra);
                break;
            case "booleano":
                symbolTable.put(variableIdentifier, TypeCSVIdent.ATRIBUTO, TypeCSVAtribute.BOOLEANO, regra);
                break;
            default:
                return false;
        }
        return true;
    }

    //Visita a definição do formato de uma tabela
    @Override
    public Void visitDefinicao(csvValidatorParser.DefinicaoContext ctx){
        String identifier = ctx.IDENT().getText();

        if (nestedScopes.runNestedScopes().size() > 1) {
            nestedScopes.giveupScope();
        }
        SymbolTable globalScope = nestedScopes.getCurrentScope();

        // Checa se já existe um formato de tabela definido com o mesmo nome
        if(globalScope.exists(identifier)){
            csvValidatorSemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                "identifier " + identifier + " ja declarado anteriormente\n");
        }
        else{
            // Caso negativo, começa o processo de definir esse formato e adicionar à tabela. 

            // Cria um escopo (escopo da definicao)
            nestedScopes.createNewScope();
            SymbolTable definicaoScope = nestedScopes.getCurrentScope();
            definicaoScope.setGlobal(globalScope); // Define o escopo global, ao qual o escopo definicaoScope pertence

            globalScope.put(identifier, SymbolTable.TypeCSVIdent.DEFINICAO, null, definicaoScope); // O identifier (nome do formato de tabela) ao escopo global
            
            // Para cada atributo/coluna definido faz as verificações semânticas necessárias
            for(csvValidatorParser.AtributoContext atributo: ctx.corpo().atributo()){
                String identAtributo = atributo.IDENT().getText();
                  
                if(definicaoScope.exists(identAtributo)){
                        //outro parametro ja identificado com msm nome
                        csvValidatorSemanticUtils.addSemanticError(atributo.IDENT().getSymbol(),
                        "identifier " + identAtributo + " ja declarado anteriormente\n");
                }
                else{
                    // A coluna sendo analisada possui a regra PK?
                    boolean regra = atributo.REGRA() != null ? true: false;

                    // Apenas uma coluna na tabela pode ter PK
                    if(definicaoScope.existeRegra() && regra){
                        csvValidatorSemanticUtils.addSemanticError(atributo.IDENT().getSymbol(),
                        "O atributo " + atributo.IDENT().getText() + " nao pode ter regra, pois outro atributo já possui regra PK \n");
                    }

                    // Faz a inserção dependendo do tipo, uma vez que o tipo literal tem tamanho maximo
                    if(atributo.tipo().literal() != null){
                        String tipoAtributo = atributo.tipo().literal().DEFINE_STRING().getText();
                        int tamanho = Integer.parseInt(atributo.tipo().literal().tamanho().NUM_INT_POS().getText());
                        if(!defineTypeAndAddtoScope(identAtributo, tipoAtributo, definicaoScope, tamanho, regra)){
                            csvValidatorSemanticUtils.addSemanticError(atributo.IDENT().getSymbol(),
                            "tipo " + tipoAtributo + "nao reconhecido \n");
                        }
                    }
                    else{
                        String tipoAtributo = atributo.tipo().getText();
                        if(!defineTypeAndAddtoScope(identAtributo, tipoAtributo, definicaoScope, 0, regra)){
                            csvValidatorSemanticUtils.addSemanticError(atributo.IDENT().getSymbol(),
                            "tipo " + tipoAtributo + "nao reconhecido \n");
                        }
                    }
                }
            }

        }

        return super.visitDefinicao(ctx);
    }

    // Visita o trecho de execucao do analisador, para cada csv/linha checa se a definiçao de tabela referenciada existe na tabela de simbolos/escopo global
    @Override
    public Void visitExecucao(csvValidatorParser.ExecucaoContext ctx){
        if (nestedScopes.runNestedScopes().size() > 1) {
            nestedScopes.giveupScope();
        }
        SymbolTable globalScope = nestedScopes.getCurrentScope();

        for(csvValidatorParser.ChecagemContext checagem: ctx.checagem()){
            if(!globalScope.exists(checagem.IDENT().getText())){
                csvValidatorSemanticUtils.addSemanticError(checagem.IDENT().getSymbol(),
                        "definicao " + checagem.IDENT().getText() + " nao criada\n");
            }
        }

        return super.visitExecucao(ctx);
    }
    
}
