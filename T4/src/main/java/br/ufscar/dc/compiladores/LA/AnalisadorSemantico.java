// Importação das bibliotecas e classes necessárias
package br.ufscar.dc.compiladores.LA;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import br.ufscar.dc.compiladores.LA.LAParser.CmdAtribuicaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdChamadaContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdLeiaContext;
import br.ufscar.dc.compiladores.LA.LAParser.Decl_local_globalContext;
import br.ufscar.dc.compiladores.LA.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.LA.LAParser.Exp_aritmeticaContext;
import br.ufscar.dc.compiladores.LA.LAParser.ExpressaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.IdentificadorContext;
import br.ufscar.dc.compiladores.LA.LAParser.VariavelContext;
import br.ufscar.dc.compiladores.LA.SymbolTable.TypeLAVariable;

    
public class AnalisadorSemantico extends LABaseVisitor<Void> { //verifica semantica em cada nivel da arvore sintatica
    Scopes nestedScopes = new Scopes();
    SymbolTable symbolTable;

    //define o tipo e add ao escopo
    Boolean defineTypeAndAddtoScope(String variableIdentifier, String variableType, SymbolTable symbolTable){
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
    //verifica se os identificadores foram declarados antes e add na tabela de simbolos com o tipo
    public Void visitDeclaracao_local(LAParser.Declaracao_localContext ctx) {
        // Lógica para a regra "declaracao_local"
        if(ctx.IDENT() != null){
            //Existe um IDENT (sequencia de caracteres que define um identificador (nome))
            String identifier = ctx.IDENT().getText();
            SymbolTable currentScope = nestedScopes.getCurrentScope();

            if (ctx.tipo_basico() != null) { 
                // constant declaration
                // 'constante' IDENT ':' tipo_basico '=' valor_constante
                if (currentScope.exists(identifier)) {
                    LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                            "identifier " + identifier + " ja declarado anteriormente\n");
                } else {
                    String constantType = ctx.tipo_basico().getText();
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
                            // nunca chega aqui
                            break;
                    }
                }
            } else {
                // type declaration
                // 'tipo' IDENT ':' tipo
                if (currentScope.exists(identifier)) { //ja tava declarado
                    LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                            "identifier " + identifier + " ja declarado anteriormente\n");
                } else { //add erro
                    SymbolTable fieldsTypes = new SymbolTable();
                    currentScope.put(identifier, SymbolTable.TypeLAIdentifier.TIPO, null, fieldsTypes);
                    for (VariavelContext variable : ctx.tipo().registro().variavel()) {
                        for (IdentificadorContext ctxIdentVariable : variable.identificador()) {
                            String variableIdentifier = ctxIdentVariable.getText();
                            if (fieldsTypes.exists(variableIdentifier)) {
                                LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                                        "identificador " + variableIdentifier + " ja declarado anteriormente\n");
                            } else {
                                String variableType = variable.tipo().getText();
                                if(!defineTypeAndAddtoScope(variableIdentifier, variableType, fieldsTypes)){
                                    //nada acontece
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
                for (IdentificadorContext ctxIdentVariable : ctx.variavel().identificador()) {
                    String variableIdentifier = "";
                    for (TerminalNode ident : ctxIdentVariable.IDENT())
                        variableIdentifier += ident.getText();
                    SymbolTable currentScope = nestedScopes.getCurrentScope();

                    if (ctxIdentVariable.dimensao() != null)
                            // dimension exists
                            for (Exp_aritmeticaContext expDim : ctxIdentVariable.dimensao().exp_aritmetica())
                                LASemanticUtils.verifyType(currentScope, expDim);

                    // Verifica se o identificador da variável já foi declarado anteriormente.
                    if (currentScope.exists(variableIdentifier)) {
                        LASemanticUtils.addSemanticError(ctxIdentVariable.IDENT(0).getSymbol(),
                                "identificador " + variableIdentifier + " ja declarado anteriormente\n");
                    } else {
                        String variableType = ctx.variavel().tipo().getText();
                        
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
                // declaracao do registro com tipo
                ArrayList<String> registerIdentifiers = new ArrayList<>();
                for (IdentificadorContext ctxIdentReg : ctx.variavel().identificador()) {
                    String identifierName = ctxIdentReg.getText();
                    SymbolTable currentScope = nestedScopes.getCurrentScope();

                    if (currentScope.exists(identifierName)) {
                        // ident tem que ser unico
                        LASemanticUtils.addSemanticError(ctxIdentReg.IDENT(0).getSymbol(),
                                "identificador " + identifierName + " ja declarado anteriormente\n");
                    } else {
                        SymbolTable fields = new SymbolTable();
                        currentScope.put(identifierName, SymbolTable.TypeLAIdentifier.REGISTRO, null,
                                fields);
                        registerIdentifiers.add(identifierName);
                    }
                }

                for (VariavelContext ctxVariableRegister : ctx.variavel().tipo().registro().variavel()) {
                    // popula o contexto do registro
                    for (IdentificadorContext ctxVariableRegisterIdent : ctxVariableRegister.identificador()) {
                        String registerFieldName = ctxVariableRegisterIdent.getText();
                        SymbolTable currentScope = nestedScopes.getCurrentScope();

                        for (String registerIdentifier : registerIdentifiers) {
                            SymbolTableEntry entry = currentScope.check(registerIdentifier);
                            SymbolTable registerFields = entry.argsRegFunc;

                            if (registerFields.exists(registerFieldName)) {
                                LASemanticUtils.addSemanticError(ctxVariableRegisterIdent.IDENT(0).getSymbol(),
                                        "identificador " + registerFieldName + " ja declarado anteriormente\n");
                            } else {
                                String variableType = ctxVariableRegister.tipo().getText();
                                if(!defineTypeAndAddtoScope(registerFieldName, variableType, registerFields)){
                                    // nao eh um tipo primitivo
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
    //verifica se os identificadores já foram declarados antes e add na tabela de simbolos junto com o tipo
    // lida com delcaração de parametros de funções
    public Void visitDeclaracao_global(Declaracao_globalContext ctx){ 
        String identifier = ctx.IDENT().getText();

        // recebe escopos
        List<SymbolTable> scopes = nestedScopes.runNestedScopes();
        if (scopes.size() > 1) {
            nestedScopes.giveupScope();
        }
        SymbolTable globalScope = nestedScopes.getCurrentScope();

        if(ctx.tipo_estendido() != null){
            //tem um tipo e retorna, eh função
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
                        //dps de declarar o tipo de um parametro, pode declarar varios parametros do msm tipo
                        String parameterIdentifier = ident.getText();

                        if(functionScope.exists(parameterIdentifier)){
                            //outro parametro ja identificado com msm nome
                            LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                                "identifier " + parameterIdentifier + " ja declarado anteriormente\n");
                        }
                        else{
                            if(defineTypeAndAddtoScope(parameterIdentifier, variableType, functionScope)){ 
                                //Caso consiga definir os tipos para o escopo da função, reproduz para os parametros
                                defineTypeAndAddtoScope(parameterIdentifier, variableType, funcParameters);
                            }else{
                                //Caso não seja um dos tipo_estendido 
                                if (globalScope.exists(variableType) && globalScope.check(
                                    variableType).identifierType == SymbolTable.TypeLAIdentifier.TIPO) {
                                    if (functionScope.exists(parameterIdentifier)) {
                                        LASemanticUtils.addSemanticError(ident.IDENT(0).getSymbol(),
                                                "identifier " + parameterIdentifier + " ja declarado anteriormente\n");
                                    } else {
                                        SymbolTableEntry fields = globalScope.check(variableType);
                                        SymbolTable nestedTableType = fields.argsRegFunc;

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
            //é um procedure
            nestedScopes.createNewScope();
            SymbolTable procScope = nestedScopes.getCurrentScope();
            procScope.setGlobal(globalScope); //Add referencia do escopo global na symbolTable

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
                        //dps de declarar o tipo de um parametro, pode declarar varios parametros do msm tipo
                        String parameterIdentifier = ident.getText();

                        if(procScope.exists(parameterIdentifier)){
                            //outro parametro ja identificado com msm nome
                            LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                                "identifier " + parameterIdentifier + " ja declarado anteriormente\n");
                        }
                        else{
                            if(defineTypeAndAddtoScope(parameterIdentifier, variableType, procScope)){
                                defineTypeAndAddtoScope(parameterIdentifier, variableType, procParameters);
                            }else{
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
    public Void visitCmdChamada(CmdChamadaContext ctx){ //verifica se os identificadores de funções foram delcarados antes

        SymbolTable currentScope = nestedScopes.getCurrentScope();
        String identifier  = ctx.IDENT().getText();

        if (!currentScope.exists(identifier)) {
            LASemanticUtils.addSemanticError(ctx.IDENT().getSymbol(),
                    "identificador " + identifier + " nao declarado\n");
        } else {
            ArrayList<SymbolTable.TypeLAVariable> parameterTypes = new ArrayList<>();
            for (ExpressaoContext exp : ctx.expressao()) {
                parameterTypes.add(LASemanticUtils.verifyType(currentScope, exp));
            }
        }

        return super.visitCmdChamada(ctx);
    }


    @Override
    //verifica se a atribuição é compativel (tipo, ponteiro, etc)
    public Void visitCmdAtribuicao(CmdAtribuicaoContext ctx){
        SymbolTable currentScope = nestedScopes.getCurrentScope();
        TypeLAVariable leftValue = LASemanticUtils.verifyType(currentScope,
                ctx.identificador());
        TypeLAVariable rightValue = LASemanticUtils.verifyType(currentScope,
                ctx.expressao());
        // Verifica atribuição para ponteiros
        String[] atribuition = ctx.getText().split("<-");
        if (!LASemanticUtils.verifyType(leftValue, rightValue) && !atribuition[0].contains("^")) {
            // Esse erro informa que a atribuição não é compatível para o identificador presente na atribuição.
            LASemanticUtils.addSemanticError(ctx.identificador().IDENT(0).getSymbol(),
                    "atribuicao nao compativel para " + ctx.identificador().getText() + "\n");
        }
        // checa tipo
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
    //verifica se os identificadores usados na chamada 'leia' foram declarados antes
    public Void visitCmdLeia(CmdLeiaContext ctx){
        // Obtemos o escopo atual através da variável currentScope 
        SymbolTable currentScope = nestedScopes.getCurrentScope();

        // Iteramos sobre os identificadores presentes no comando 
        for (IdentificadorContext ident : ctx.identificador()) {
            // Verificação semântica do tipo do identificador
            LASemanticUtils.verifyType(currentScope, ident);
        }
        return super.visitCmdLeia(ctx);
    }

    @Override
    //verifica se os tipos das chamadas aritmeticas sao compativeis
    public Void visitExp_aritmetica(LAParser.Exp_aritmeticaContext ctx){
        // Lógica para a regra "exp_aritmetica"
        SymbolTable currentScope = nestedScopes.getCurrentScope();
        LASemanticUtils.verifyType(currentScope, ctx);
        return super.visitExp_aritmetica(ctx);
    }

    // Program entrypoint method
    @Override
    //verifica se nao tem return fora de função
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        for (CmdContext ctxCmd : ctx.corpo().cmd()) {
            if (ctxCmd.cmdRetorne() != null) {
                LASemanticUtils.addSemanticError(ctxCmd.cmdRetorne().getStart(),
                        "comando retorne nao permitido nesse escopo\n");
            }
        }

        for (Decl_local_globalContext ctxDec : ctx.declaracoes().decl_local_global()) {
            if (ctxDec.declaracao_global() != null && ctxDec.declaracao_global().tipo_estendido() == null) {
                for (CmdContext ctxCmd : ctxDec.declaracao_global().cmd()) {
                    if (ctxCmd.cmdRetorne() != null)
                        LASemanticUtils.addSemanticError(ctxCmd.cmdRetorne().getStart(),
                                "comando retorne nao permitido nesse escopo\n");
                }
            }
        }

        return super.visitPrograma(ctx);
    }
    
    @Override
    //cria e gerencia escopos pra cada bloco de codigo
    public Void visitCorpo(LAParser.CorpoContext ctx) {
        List<SymbolTable> scopes = nestedScopes.runNestedScopes();
        if (scopes.size() > 1) {
            nestedScopes.giveupScope();
        }

        return super.visitCorpo(ctx);
    }
}
