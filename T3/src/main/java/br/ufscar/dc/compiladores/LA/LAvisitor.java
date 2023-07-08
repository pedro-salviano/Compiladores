package br.ufscar.dc.compiladores.LA;

import br.ufscar.dc.compiladores.LA.LABaseVisitor;
import br.ufscar.dc.compiladores.LA.LAParser;
import br.ufscar.dc.compiladores.LA.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.LA.SymbolTable.TypeLAVariable;

import java.util.ArrayList;

public class LAvisitor extends LABaseVisitor<Void> {
    Scopes nestedScopes = new Scopes();
    SymbolTable symbolTable;

    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        symbolTable = new SymbolTable();
        return super.visitPrograma(ctx);
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
                        default:
                            // never reached
                            break;
                    }
                }
            } 
        } else {
            // variable declaration
            // 'declare' variavel
            if (ctx.variavel().tipo().registro() == null) {
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
                                                "identificador "
                                                        + variableIdentifier + " ja declarado anteriormente\n");
                                    }
                                }

                                break;
                        }
                    }
                }
                
            } 
        }

        return super.visitDeclaracao_local(ctx);
    }
}