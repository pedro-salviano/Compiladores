// Importação das bibliotecas e classes necessárias
package br.ufscar.dc.compiladores.LA;

import java.util.ArrayList;

import br.ufscar.dc.compiladores.LA.SymbolTable.TypeLAVariable;

    
public class LAvisitor extends LABaseVisitor<Void> {
    Scopes nestedScopes = new Scopes();
    SymbolTable symbolTable;

    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        // Lógica para a regra "declaracao_local"
        if(ctx.variavel().tipo().registro() == null){
            for (var ctxIdentVariable : ctx.variavel().identificador()) {
                var variableIdentifier = "";
                for (var ident : ctxIdentVariable.IDENT())
                    variableIdentifier += ident.getText();
                var currentScope = nestedScopes.getCurrentScope();

                if (ctxIdentVariable.dimensao() != null)
                        // dimension exists
                        for (var expDim : ctxIdentVariable.dimensao().exp_aritmetica())
                            LASemanticUtils.verifyType(currentScope, expDim);

                // Verifica se o identificador da variável já foi declarado anteriormente.
                if (currentScope.exists(variableIdentifier)) {
                    LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                            "identificador " + variableIdentifier + " ja declarado anteriormente\n");
                } else {
                    var variableType = ctx.variavel().tipo().getText();
                    // Switch-case para tratar os diferentes tipos de variáveis.
                    switch (variableType) {
                        // Se o tipo for "inteiro", "literal", "real" ou "lógico", a variável é 
                        // adicionada ao escopo atual com o tipo correspondente utilizando a 
                        // função put da classe currentScope.
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
                        case "^inteiro":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_INTE);
                            break;
                        case "^literal":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LITE);
                            break;
                        case "^real":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_REAL);
                            break;
                        case "^logico":
                            currentScope.put(variableIdentifier,
                                    SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LOGI);
                            break;
                        default: 
                            // Caso o tipo não seja um tipo básico
                            if (currentScope.exists(variableType) && currentScope.check(
                                // Verificamos se o tipo já foi declarado anteriormente no escopo atual e se é um tipo válido.
                                variableType).identifierType == SymbolTable.TypeLAIdentifier.TIPO) {
                                if (currentScope.exists(variableIdentifier)) {
                                    LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                                            "identificador " + variableIdentifier + " ja declarado anteriormente\n");
                                }
                                else{
                                    SymbolTableEntry entry = currentScope.check(variableType);
                                    SymbolTable fieldsType = entry.argsRegFunc;
                                    currentScope.put(variableIdentifier,
                                            SymbolTable.TypeLAIdentifier.REGISTRO, null, fieldsType);
                                }
                            }

                            //Se o tipo não foi declarado, um erro semântico é adicionado informando que o tipo não foi declarado
                            if(!currentScope.exists(variableType)){
                                LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                                "tipo " + variableType + " nao declarado\n");
                                // A variável é adicionada ao escopo atual com um tipo inválido (INVALIDO).
                                currentScope.put(variableIdentifier,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL,
                                            SymbolTable.TypeLAVariable.INVALIDO);
                            }

                            break;
                    }
                }
            }
        }
        else{
            // Register with type declaration
            ArrayList<String> registerIdentifiers = new ArrayList<>();
            for (var ctxIdentReg : ctx.variavel().identificador()) {
                String identifierName = ctxIdentReg.getText();
                SymbolTable currentScope = nestedScopes.getCurrentScope();

                if (currentScope.exists(identifierName)) {
                    // identifier must be unique 
                    LASemanticUtils.addSemanticError(ctxIdentReg.IDENT(0).getSymbol(),
                            "identificador " + identifierName + " ja declarado anteriormente\n");
                } else {
                    SymbolTable fields = new SymbolTable();
                    currentScope.put(identifierName, SymbolTable.TypeLAIdentifier.REGISTRO, null,
                            fields);
                    registerIdentifiers.add(identifierName);
                }
            }

            for (var ctxVariableRegister : ctx.variavel().tipo().registro().variavel()) {
                // populate register context
                for (var ctxVariableRegisterIdent : ctxVariableRegister.identificador()) {
                    var registerFieldName = ctxVariableRegisterIdent.getText();
                    SymbolTable currentScope = nestedScopes.getCurrentScope();

                    for (var registerIdentifier : registerIdentifiers) {
                        SymbolTableEntry entry = currentScope.check(registerIdentifier);
                        SymbolTable registerFields = entry.argsRegFunc;

                        if (registerFields.exists(registerFieldName)) {
                            LASemanticUtils.addSemanticError(ctxVariableRegisterIdent.IDENT(0).getSymbol(),
                                    "identificador " + registerFieldName + " ja declarado anteriormente\n");
                        } else {
                            var variableType = ctxVariableRegister.tipo().getText();

                            switch (variableType) {
                                case "inteiro":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.INTEIRO);
                                    break;
                                case "literal":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LITERAL);
                                    break;
                                case "real":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.REAL);
                                    break;
                                case "logico":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LOGICO);
                                    break;
                                case "^logico":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LOGI);
                                    break;
                                case "^real":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_REAL);
                                    break;
                                case "^literal":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LITE);
                                    break;
                                case "^inteiro":
                                    registerFields.put(registerFieldName,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_INTE);
                                    break;
                                default:
                                    // not a basic/primitive type
                                    if (!currentScope.exists(variableType)) {
                                        LASemanticUtils.addSemanticError(
                                                ctxVariableRegisterIdent.IDENT(0).getSymbol(),
                                                "tipo " + variableType + " nao declarado\n");
                                        currentScope.put(registerFieldName,
                                                SymbolTable.TypeLAIdentifier.VARIAVEL,
                                                SymbolTable.TypeLAVariable.INVALIDO);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        }
        
        return super.visitDeclaracao_local(ctx);
    }


    @Override
    public Void visitCmd(LAParser.CmdContext ctx) {
        // Lógica para a regra "cmd", ou seja, o tratamento das ações a serem realizadas quando um comando é encontrado na análise do código.
        if (ctx.cmdLeia() != null) {
            // Obtemos o escopo atual através da variável currentScope 
            var currentScope = nestedScopes.getCurrentScope();

            // Iteramos sobre os identificadores presentes no comando 
            for (var ident : ctx.cmdLeia().identificador()) {
                // Verificação semântica do tipo do identificador
                LASemanticUtils.verifyType(currentScope, ident);
            }
        }

        if (ctx.cmdAtribuicao() != null) {
            var currentScope = nestedScopes.getCurrentScope();
            var leftValue = LASemanticUtils.verifyType(currentScope,
                    ctx.cmdAtribuicao().identificador());
            var rightValue = LASemanticUtils.verifyType(currentScope,
                    ctx.cmdAtribuicao().expressao());
            // Verifica atribuição para ponteiros
            var atribuition = ctx.cmdAtribuicao().getText().split("<-");
            if (!LASemanticUtils.verifyType(leftValue, rightValue) && !atribuition[0].contains("^")) {
                // Esse erro informa que a atribuição não é compatível para o identificador presente na atribuição.
                LASemanticUtils.addSemanticError(ctx.cmdAtribuicao().identificador().IDENT(0).getSymbol(),
                        "atribuicao nao compativel para " + ctx.cmdAtribuicao().identificador().getText() + "\n");
            }
            // Type Checking
            if (atribuition[0].contains("^")){
                if (
                    leftValue == SymbolTable.TypeLAVariable.PONT_INTE
                    && 
                    rightValue != SymbolTable.TypeLAVariable.INTEIRO
                    )
                    LASemanticUtils.addSemanticError(ctx.cmdAtribuicao().identificador().IDENT(0).getSymbol(),
                            "atribuicao nao compativel para " + atribuition[0] + "\n");
                if (
                    leftValue == SymbolTable.TypeLAVariable.PONT_LOGI
                    && 
                    rightValue != SymbolTable.TypeLAVariable.LOGICO
                    )
                    LASemanticUtils.addSemanticError(ctx.cmdAtribuicao().identificador().IDENT(0).getSymbol(),
                            "atribuicao nao compativel para " + atribuition[0] + "\n");
                if (
                    leftValue == SymbolTable.TypeLAVariable.PONT_REAL
                    && 
                    rightValue != SymbolTable.TypeLAVariable.REAL
                    )
                    LASemanticUtils.addSemanticError(ctx.cmdAtribuicao().identificador().IDENT(0).getSymbol(),
                            "atribuicao nao compativel para " + atribuition[0] + "\n");
                if (
                    leftValue == SymbolTable.TypeLAVariable.PONT_LITE
                    && 
                    rightValue != SymbolTable.TypeLAVariable.LITERAL
                    )
                    LASemanticUtils.addSemanticError(ctx.cmdAtribuicao().identificador().IDENT(0).getSymbol(),
                            "atribuicao nao compativel para " + atribuition[0] + "\n");
            }
        }

        // Permite que a visita aos nós filhos da regra "cmd" seja continuada.
        return super.visitCmd(ctx);
    } 

    @Override
    public Void visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx){
        // Lógica para a regra "exp_aritmetica"
        var currentScope = nestedScopes.getCurrentScope();
        LASemanticUtils.verifyType(currentScope, ctx);
        return super.visitExp_aritmetica(ctx);
    }

}
