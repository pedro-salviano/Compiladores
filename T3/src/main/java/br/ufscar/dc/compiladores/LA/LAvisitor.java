package br.ufscar.dc.compiladores.LA;


import br.ufscar.dc.compiladores.LA.SymbolTable.TypeLAVariable;


public class LAvisitor extends LABaseVisitor<Void> {
    Scopes nestedScopes = new Scopes();
    SymbolTable symbolTable;

    // visitCorpo tidies the scopes to make only
    // the globalScope visible
    @Override
    public Void visitCorpo(LAParser.CorpoContext ctx) {
        var scopes = nestedScopes.runNestedScopes();
        if (scopes.size() > 1) {
            nestedScopes.giveupScope();
        }

        return super.visitCorpo(ctx);
    }
     
    // visitDeclaracao_local treats the declaration of variables, constants 
    // and types
    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        if (ctx.IDENT() != null) {
            var identifier = ctx.IDENT().getText();
            var currentScope = nestedScopes.getCurrentScope();

            if (ctx.tipo_basico() != null) { 
                // constant declaration
                // 'constante' IDENT ':' tipo_basico '=' valor_constante
                if (currentScope.exists(identifier)) {
                    LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                            "identifier " + identifier + " ja declarado anteriormente\n");
                } else {
                    var constantType = ctx.tipo_basico().getText();
                    switch (constantType) {
                        case "inteiro":
                            currentScope.put(identifier, SymbolTable.TypeLAIdentifier.CONSTANTE,
                                    TypeLAVariable.INTEIRO);
                            break;
                        case "literal":
                            currentScope.put(identifier, SymbolTable.TypeLAIdentifier.CONSTANTE,
                                    TypeLAVariable.LITERAL);
                            break;
                        case "real":
                            currentScope.put(identifier, SymbolTable.TypeLAIdentifier.CONSTANTE,
                                    TypeLAVariable.REAL);
                            break;
                        case "logico":
                            currentScope.put(identifier, SymbolTable.TypeLAIdentifier.CONSTANTE,
                                    TypeLAVariable.LOGICO);
                            break;
                    }
                }
            } else {
                switch(ctx.variavel().tipo().getText()){
                    case "inteiro":
                        symbolTable.put(ctx.IDENT().getText(), SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.INTEIRO);
                        break;
                    case "literal":
                        symbolTable.put(ctx.IDENT().getText(),
                                SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LITERAL);
                        break;
                    case "real":
                        symbolTable.put(ctx.IDENT().getText(),
                                SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.REAL);
                        break;
                    case "logico":
                        symbolTable.put(ctx.IDENT().getText(),
                                SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LOGICO);
                        break;
                }
            }
        } else {
            // variable declaration
            // 'declare' variavel
            for (var ctxIdentVariable : ctx.variavel().identificador()) {
                var variableIdentifier = "";
                for (var ident : ctxIdentVariable.IDENT())
                    variableIdentifier += ident.getText();
                var currentScope = nestedScopes.getCurrentScope();

                // Caso o identificador da variavel ja esteja sendo usada.
                if (currentScope.exists(variableIdentifier)) {
                    LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                            "identificador " + variableIdentifier + " ja declarado anteriormente\n");
                } else {
                    var variableType = ctx.variavel().tipo().getText();
                    switch (variableType) {
                        case "inteiro":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.INTEIRO);
                            break;
                        case "literal":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LITERAL);
                            break;
                        case "real":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.REAL);
                            break;
                        case "logico":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LOGICO);
                            break;
                        default: 
                            // not a basic/primitive type
                            if (currentScope.exists(variableType) && currentScope.check(
                                    variableType).identifierType == SymbolTable.TypeLAIdentifier.TIPO) {
                                if (currentScope.exists(variableIdentifier)) {
                                    LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                                            "identificador " + variableIdentifier + " ja declarado anteriormente\n");
                                }
                            }

                            if(!currentScope.exists(variableType)){
                                LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                                "tipo " + variableType + " nao declarado\n");
                                currentScope.put(variableIdentifier,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL,
                                            SymbolTable.TypeLAVariable.INVALIDO);
                            }

                            break;
                    }
                }
            }
                
        }

        return super.visitDeclaracao_local(ctx);
    }


    @Override
    public Void visitCmd(LAParser.CmdContext ctx) {
        if (ctx.cmdLeia() != null) {
            var currentScope = nestedScopes.getCurrentScope();
            for (var ident : ctx.cmdLeia().identificador()) {
                LASemanticUtils.verifyType(currentScope, ident);
            }
        }

        if (ctx.cmdAtribuicao() != null) {
            var currentScope = nestedScopes.getCurrentScope();
            var leftValue = LASemanticUtils.verifyType(currentScope,
                    ctx.cmdAtribuicao().identificador());
            var rightValue = LASemanticUtils.verifyType(currentScope,
                    ctx.cmdAtribuicao().expressao());
            // for pointers
            var atribuition = ctx.cmdAtribuicao().getText().split("<-");
            if (!LASemanticUtils.verifyType(leftValue, rightValue) && !atribuition[0].contains("^")) {
                LASemanticUtils.addSemanticError(ctx.cmdAtribuicao().identificador().IDENT(0).getSymbol(),
                        "atribuicao nao compativel para " + ctx.cmdAtribuicao().identificador().getText() + "\n");
            }
            
        }

        return super.visitCmd(ctx);
    } 

    @Override
    public Void visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx){
        var currentScope = nestedScopes.getCurrentScope();
        LASemanticUtils.verifyType(currentScope, ctx);
        return super.visitExp_aritmetica(ctx);
    }

}