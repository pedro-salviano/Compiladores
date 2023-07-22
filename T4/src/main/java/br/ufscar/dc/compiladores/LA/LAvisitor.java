// Importação das bibliotecas e classes necessárias
package br.ufscar.dc.compiladores.LA;

import java.util.ArrayList;

import br.ufscar.dc.compiladores.LA.LAParser.CmdAtribuicaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdChamadaContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdLeiaContext;
import br.ufscar.dc.compiladores.LA.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.LA.SymbolTable.TypeLAVariable;

    
public class LAvisitor extends LABaseVisitor<Void> {
    Scopes nestedScopes = new Scopes();
    SymbolTable symbolTable;

    public Boolean defineTypeAndAddtoScope(String variableIdentifier, String variableType, SymbolTable symbolTable){
        switch (variableType) {
            case "inteiro":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.INTEIRO);
                break;
            case "literal":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LITERAL);
                break;
            case "real":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.REAL);
                break;
            case "logico":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LOGICO);
                break;
            case "^logico":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LOGI);
                break;
            case "^real":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_REAL);
                break;
            case "^literal":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LITE);
                break;
            case "^inteiro":
                symbolTable.put(variableIdentifier,
                        SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_INTE);
                break;
            default:
                return false;
        }
        return true;
    }


    @Override
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        // Lógica para a regra "declaracao_local"
        if(ctx.IDENT() != null){
            //Existe um IDENT (sequencia de caracteres que define um identificador (nome))
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
            } else {
                // type declaration
                // 'tipo' IDENT ':' tipo
                if (currentScope.exists(identifier)) {
                    LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                            "identifier " + identifier + " ja declarado anteriormente\n");
                } else {
                    var fieldsTypes = new SymbolTable();
                    currentScope.put(identifier, SymbolTable.TypeLAIdentifier.TIPO, null, fieldsTypes);
                    for (var variable : ctx.tipo().registro().variavel()) {
                        for (var ctxIdentVariable : variable.identificador()) {
                            var variableIdentifier = ctxIdentVariable.getText();
                            if (fieldsTypes.exists(variableIdentifier)) {
                                LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                                        "identificador " + variableIdentifier + " ja declarado anteriormente\n");
                            } else {
                                var variableType = variable.tipo().getText();
                                if(!defineTypeAndAddtoScope(variableIdentifier, variableType, fieldsTypes)){
                                    //nothing happens
                                }
                            }
                        }
                    }
                }
            }
        }else{
            //'declare' variavel
            if(ctx.variavel().tipo().registro() == null){
                //Não é registro
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
                        
                        if(!defineTypeAndAddtoScope(variableIdentifier, variableType, currentScope)){
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
                                if(!defineTypeAndAddtoScope(registerFieldName, variableType, registerFields)){
                                    // not a basic/primitive type
                                    if (!currentScope.exists(variableType)) {
                                        LASemanticUtils.addSemanticError(
                                                ctxVariableRegisterIdent.IDENT(0).getSymbol(),
                                                "tipo " + variableType + " nao declarado\n");
                                        currentScope.put(registerFieldName,
                                                SymbolTable.TypeLAIdentifier.VARIAVEL,
                                                SymbolTable.TypeLAVariable.INVALIDO);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Void visitDeclaracao_global(Declaracao_globalContext ctx){
        var identifier = ctx.IDENT().getText();

        // Geting scopes
        var scopes = nestedScopes.runNestedScopes();
        if (scopes.size() > 1) {
            nestedScopes.giveupScope();
        }
        var globalScope = nestedScopes.getCurrentScope();

        if(ctx.tipo_estendido() != null){
            //has a type and returns, is a function
            nestedScopes.createNewScope();
            SymbolTable functionScope = nestedScopes.getCurrentScope();
            functionScope.setGlobal(globalScope); //Add global scope reference to symbolTable

            if(globalScope.exists(identifier)){
                LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                        "identifier " + identifier + " ja declarado anteriormente\n");
            }
            else{
                SymbolTable funcParameters = new SymbolTable();
                globalScope.put(identifier, SymbolTable.TypeLAIdentifier.FUNCAO, null, funcParameters,
                        ctx.tipo_estendido().getText());

                for(LAParser.ParametroContext declaredParameter: ctx.parametros().parametro()){
                    String variableType =  declaredParameter.tipo_estendido().getText();

                    for(LAParser.IdentificadorContext ident: declaredParameter.identificador()){
                        //After declaring a type of a parameter, is possible to declare multiple parameters of same type
                        String parameterIdentifier = ident.getText();

                        if(functionScope.exists(parameterIdentifier)){
                            //Another parameter with same name, already defined
                            LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                                "identifier " + parameterIdentifier + " ja declarado anteriormente\n");
                        }
                        else{
                            if(!defineTypeAndAddtoScope(parameterIdentifier, variableType, functionScope)){
                                if (globalScope.exists(variableType) && globalScope.check(
                                    variableType).identifierType == SymbolTable.TypeLAIdentifier.TIPO) {
                                    if (functionScope.exists(parameterIdentifier)) {
                                        LASemanticUtils.addSemanticError(ident.IDENT(0).getSymbol(),
                                                "identifier " + parameterIdentifier + " ja declarado anteriormente\n");
                                    } else {
                                        var fields = globalScope.check(variableType);
                                        var nestedTableType = fields.argsRegFunc;

                                        functionScope.put(parameterIdentifier,
                                                SymbolTable.TypeLAIdentifier.REGISTRO,
                                                SymbolTable.TypeLAVariable.REGISTRO, nestedTableType,
                                                variableType);
                                        funcParameters.put(parameterIdentifier,
                                                SymbolTable.TypeLAIdentifier.REGISTRO,
                                                SymbolTable.TypeLAVariable.REGISTRO, nestedTableType,
                                                variableType);
                                    }
                                }
                                if (!globalScope.exists(variableType)) {
                                    LASemanticUtils.addSemanticError(ident.IDENT(0).getSymbol(),
                                            "tipo " + variableType + " nao declarado\n");
                                    functionScope.put(parameterIdentifier,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL,
                                            SymbolTable.TypeLAVariable.INVALIDO);
                                    funcParameters.put(parameterIdentifier,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL,
                                            SymbolTable.TypeLAVariable.INVALIDO);
                                }
                            }
                        }

                    }
                }
            }

        }else{
            //is a procedure
            nestedScopes.createNewScope();
            SymbolTable procScope = nestedScopes.getCurrentScope();
            procScope.setGlobal(globalScope); //Add global scope reference to symbolTable

            if(globalScope.exists(identifier)){
                LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                        "identifier " + identifier + " ja declarado anteriormente\n");
            }
            else{
                SymbolTable procParameters = new SymbolTable();
                globalScope.put(identifier, SymbolTable.TypeLAIdentifier.PROCEDIMENTO, null, procParameters);

                for(LAParser.ParametroContext declaredParameter: ctx.parametros().parametro()){
                    String variableType =  declaredParameter.tipo_estendido().getText();

                    for(LAParser.IdentificadorContext ident: declaredParameter.identificador()){
                        //After declaring a type of a parameter, is possible to declare multiple parameters of same type
                        String parameterIdentifier = ident.getText();

                        if(procScope.exists(parameterIdentifier)){
                            //Another parameter with same name, already defined
                            LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                                "identifier " + parameterIdentifier + " ja declarado anteriormente\n");
                        }
                        else{
                            if(!defineTypeAndAddtoScope(parameterIdentifier, variableType, procScope)){
                                
                                if (globalScope.exists(variableType) && globalScope.check(
                                        variableType).identifierType == SymbolTable.TypeLAIdentifier.TIPO) {
                                    if (procScope.exists(parameterIdentifier)) {
                                        LASemanticUtils.addSemanticError(ident.IDENT(0).getSymbol(),
                                                "identifier " + parameterIdentifier + " ja declarado anteriormente\n");
                                    } else {
                                        SymbolTableEntry fields = globalScope.check(variableType);
                                        SymbolTable nestedTableType = fields.argsRegFunc;

                                        procScope.put(parameterIdentifier,
                                                SymbolTable.TypeLAIdentifier.REGISTRO,
                                                SymbolTable.TypeLAVariable.REGISTRO, nestedTableType,
                                                variableType);
                                        procParameters.put(parameterIdentifier,
                                                SymbolTable.TypeLAIdentifier.REGISTRO,
                                                SymbolTable.TypeLAVariable.REGISTRO, nestedTableType,
                                                variableType);
                                    }
                                }

                                if (!globalScope.exists(variableType)) {
                                    LASemanticUtils.addSemanticError(ident.IDENT(0).getSymbol(),
                                            "tipo " + variableType + " nao declarado\n");
                                    procScope.put(parameterIdentifier,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL,
                                            SymbolTable.TypeLAVariable.INVALIDO);
                                    procParameters.put(parameterIdentifier,
                                            SymbolTable.TypeLAIdentifier.VARIAVEL,
                                            SymbolTable.TypeLAVariable.INVALIDO);
                                }
                            }
                        }
                    }
                }
            }


        }
        

        return super.visitDeclaracao_global(ctx);
    }

    @Override
    public Void visitCmdChamada(CmdChamadaContext ctx){

        SymbolTable currentScope = nestedScopes.getCurrentScope();
        String identifier  = ctx.IDENT().getText();

        if (!currentScope.exists(identifier)) {
            LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                    "identificador " + identifier + " nao declarado\n");
        } else {
            var funProc = currentScope.check(identifier);
            ArrayList<SymbolTable.TypeLAVariable> parameterTypes = new ArrayList<>();
            for (var exp : ctx.expressao()) {
                parameterTypes.add(LASemanticUtils.verifyType(currentScope, exp));
            }
            if (!funProc.argsRegFunc.validType(parameterTypes)) {
                LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                        "incompatibilidade de parametros na chamada de " + identifier + "\n");
            }
        }

        return super.visitCmdChamada(ctx);
    }


    @Override
    public Void visitCmdAtribuicao(CmdAtribuicaoContext ctx){
        var currentScope = nestedScopes.getCurrentScope();
        var leftValue = LASemanticUtils.verifyType(currentScope,
                ctx.identificador());
        var rightValue = LASemanticUtils.verifyType(currentScope,
                ctx.expressao());
        // Verifica atribuição para ponteiros
        var atribuition = ctx.getText().split("<-");
        if (!LASemanticUtils.verifyType(leftValue, rightValue) && !atribuition[0].contains("^")) {
            // Esse erro informa que a atribuição não é compatível para o identificador presente na atribuição.
            LASemanticUtils.addSemanticError(ctx.identificador().IDENT(0).getSymbol(),
                    "atribuicao nao compativel para " + ctx.identificador().getText() + "\n");
        }
        // Type Checking
        if (atribuition[0].contains("^")){
            if (
                leftValue == SymbolTable.TypeLAVariable.PONT_INTE
                && 
                rightValue != SymbolTable.TypeLAVariable.INTEIRO
                )
                LASemanticUtils.addSemanticError(ctx.identificador().IDENT(0).getSymbol(),
                        "atribuicao nao compativel para " + atribuition[0] + "\n");
            if (
                leftValue == SymbolTable.TypeLAVariable.PONT_LOGI
                && 
                rightValue != SymbolTable.TypeLAVariable.LOGICO
                )
                LASemanticUtils.addSemanticError(ctx.identificador().IDENT(0).getSymbol(),
                        "atribuicao nao compativel para " + atribuition[0] + "\n");
            if (
                leftValue == SymbolTable.TypeLAVariable.PONT_REAL
                && 
                rightValue != SymbolTable.TypeLAVariable.REAL
                )
                LASemanticUtils.addSemanticError(ctx.identificador().IDENT(0).getSymbol(),
                        "atribuicao nao compativel para " + atribuition[0] + "\n");
            if (
                leftValue == SymbolTable.TypeLAVariable.PONT_LITE
                && 
                rightValue != SymbolTable.TypeLAVariable.LITERAL
                )
                LASemanticUtils.addSemanticError(ctx.identificador().IDENT(0).getSymbol(),
                        "atribuicao nao compativel para " + atribuition[0] + "\n");
        }
        return super.visitCmdAtribuicao(ctx);
    }

    @Override
    public Void visitCmdLeia(CmdLeiaContext ctx){
        // Obtemos o escopo atual através da variável currentScope 
        var currentScope = nestedScopes.getCurrentScope();

        // Iteramos sobre os identificadores presentes no comando 
        for (var ident : ctx.identificador()) {
            // Verificação semântica do tipo do identificador
            LASemanticUtils.verifyType(currentScope, ident);
        }
        return super.visitCmdLeia(ctx);
    }

    @Override
    public Void visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx){
        // Lógica para a regra "exp_aritmetica"
        var currentScope = nestedScopes.getCurrentScope();
        LASemanticUtils.verifyType(currentScope, ctx);
        return super.visitExp_aritmetica(ctx);
    }

    // Program entrypoint method
    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        for (var ctxCmd : ctx.corpo().cmd()) {
            if (ctxCmd.cmdRetorne() != null) {
                LASemanticUtils.addSemanticError(ctxCmd.cmdRetorne().getStart(),
                        "comando retorne nao permitido nesse escopo\n");
            }
        }

        for (var ctxDec : ctx.declaracoes().decl_local_global()) {
            if (ctxDec.declaracao_global() != null && ctxDec.declaracao_global().tipo_estendido() == null) {
                for (var ctxCmd : ctxDec.declaracao_global().cmd()) {
                    if (ctxCmd.cmdRetorne() != null)
                        LASemanticUtils.addSemanticError(ctxCmd.cmdRetorne().getStart(),
                                "comando retorne nao permitido nesse escopo\n");
                }
            }
        }

        return super.visitPrograma(ctx);
    }

}
