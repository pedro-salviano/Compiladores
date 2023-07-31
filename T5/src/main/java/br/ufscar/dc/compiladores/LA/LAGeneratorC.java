// Importação das bibliotecas e classes necessárias
package br.ufscar.dc.compiladores.LA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;


import br.ufscar.dc.compiladores.LA.LAParser.CmdAtribuicaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdCasoContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdEnquantoContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdEscrevaContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdFacaContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdLeiaContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdParaContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdRetorneContext;
import br.ufscar.dc.compiladores.LA.LAParser.CmdSeContext;
import br.ufscar.dc.compiladores.LA.LAParser.Declaracao_globalContext;
import br.ufscar.dc.compiladores.LA.LAParser.Exp_aritmeticaContext;
import br.ufscar.dc.compiladores.LA.LAParser.Exp_relacionalContext;
import br.ufscar.dc.compiladores.LA.LAParser.ExpressaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.FatorContext;
import br.ufscar.dc.compiladores.LA.LAParser.Fator_logicoContext;
import br.ufscar.dc.compiladores.LA.LAParser.IdentificadorContext;
import br.ufscar.dc.compiladores.LA.LAParser.Item_selecaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.ParcelaContext;
import br.ufscar.dc.compiladores.LA.LAParser.Parcela_logicaContext;
import br.ufscar.dc.compiladores.LA.LAParser.Parcela_nao_unarioContext;
import br.ufscar.dc.compiladores.LA.LAParser.Parcela_unarioContext;
import br.ufscar.dc.compiladores.LA.LAParser.SelecaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.TermoContext;
import br.ufscar.dc.compiladores.LA.LAParser.Termo_logicoContext;
import br.ufscar.dc.compiladores.LA.LAParser.VariavelContext;
import br.ufscar.dc.compiladores.LA.SymbolTable.TypeLAVariable;

public class LAGeneratorC extends LABaseVisitor<Void> {

    Scopes nestedScopes = new Scopes();
    SymbolTable symbolTable;

    //são iniciadas a saída e as tabelas necessárias
    public StringBuilder output;
    
    public LAGeneratorC() {
        output = new StringBuilder();
        this.symbolTable = new SymbolTable();
    }

    //início da execução, visita inclui todas as bibliotecas necessárias, visita todas as declarações globais,
    //cria-se o main, visita as declarações locais e os comandos e, por fim, termina o código com return 0
    @Override
    public Void visitPrograma(LAParser.ProgramaContext ctx) {
        output.append("#include <stdio.h>\n");
        output.append("#include <stdlib.h>\n");
        ctx.declaracoes().decl_local_global().forEach(dec -> visitDecl_local_global(dec));
        output.append("\n");
        output.append("int main() {\n");
        ctx.corpo().declaracao_local().forEach(decl -> visitDeclaracao_local(decl));
        ctx.corpo().cmd().forEach(cmd -> visitCmd(cmd));
        output.append("return 0;\n");
        output.append("}\n");
        return null;
    }

    public static String getCType(SymbolTable.TypeLAVariable val){
        String type = null;
                switch(val) {
                    case LITERAL:
                        type = "char";
                        break;
                    case INTEIRO: 
                        type = "int";
                        break;
                    case REAL: 
                        type = "float";
                        break;
                    default:
                        break;
                }
        return type;
    }

    public static String getCTypeSymbol(SymbolTable.TypeLAVariable val){
        String type = null;
                switch(val) {
                    case LITERAL:
                        type = "s";
                        break;
                    case INTEIRO: 
                        type = "d";
                        break;
                    case REAL: 
                        type = "f";
                        break;
                    default:
                        break;
                }
        return type;
    }

    Boolean defineTypeAndAddtoScope(String variableIdentifier, String variableType, SymbolTable symbolTable){
        switch (variableType) {
            case "inteiro":
                output.append("        int " + variableIdentifier + ";\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.INTEIRO);
                break;
            case "literal":
                output.append("        char " + variableIdentifier + "[80];\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LITERAL);
                break;
            case "real":
                output.append("        float " + variableIdentifier + ";\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.REAL);
                break;
            case "logico":
                output.append("        boolean " + variableIdentifier + ";\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.LOGICO);
                break;
            case "^logico":
                output.append("        boolean* " + variableIdentifier + ";\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LOGI);
                break;
            case "^real":
                output.append("        float* " + variableIdentifier + ";\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_REAL);
                break;
            case "^literal":
                output.append("        char* " + variableIdentifier + "[80];\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_LITE);
                break;
            case "^inteiro":
                output.append("        int* " + variableIdentifier + ";\n");
                symbolTable.put( variableIdentifier, SymbolTable.TypeLAIdentifier.VARIAVEL, TypeLAVariable.PONT_INTE);
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
            String identifier = ctx.IDENT().getText();
            SymbolTable currentScope = nestedScopes.getCurrentScope();

            if (ctx.tipo_basico() != null) { 
                // constant declaration
                // 'constante' IDENT ':' tipo_basico '=' valor_constante
                output.append("#define " + identifier + " " + ctx.valor_constante().getText());
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
                        // never reached
                        break;
                }
                
            } else {
                // type declaration
                // 'tipo' IDENT ':' tipo
                var fieldsType = new SymbolTable();
                currentScope.put(identifier, SymbolTable.TypeLAIdentifier.TIPO, null, fieldsType);

                output.append("    typedef struct {\n");
                for (VariavelContext variable : ctx.tipo().registro().variavel()) {
                    for (IdentificadorContext ctxIdentVariable : variable.identificador()) {
                        String variableIdentifier = ctxIdentVariable.getText();
                        String variableType = variable.tipo().getText();
                        defineTypeAndAddtoScope(variableIdentifier, variableType, fieldsType);
                        
                        
                    }
                }
                output.append("    } " + identifier + ";\n");
                
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
                    String variableType = ctx.variavel().tipo().getText();
                    
                    if(!defineTypeAndAddtoScope(variableIdentifier, variableType, currentScope)){
                        // Caso o tipo não seja um tipo básico
                        SymbolTableEntry entry = currentScope.check(variableType);
                        SymbolTable fieldsType = entry.argsRegFunc;
                        currentScope.put(variableIdentifier,
                                            SymbolTable.TypeLAIdentifier.REGISTRO, null, fieldsType);
                        output.append("    " + variableType + " " + ctxIdentVariable.getText() + ";\n");
                    }
                    
                }
            }
            else{
                // Register with type declaration
                output.append("    struct {\n");

                ArrayList<String> registerIdentifiers = new ArrayList<>();
                for (var ctxIdentReg : ctx.variavel().identificador()) {
                    String identifierName = ctxIdentReg.getText();
                    SymbolTable currentScope = nestedScopes.getCurrentScope();
                    SymbolTable fields = new SymbolTable();
                    currentScope.put(identifierName, SymbolTable.TypeLAIdentifier.REGISTRO, null,
                            fields);
                    registerIdentifiers.add(ctxIdentReg.getText());
                }

                boolean lock = false;
                for (VariavelContext ctxVariableRegister : ctx.variavel().tipo().registro().variavel()) {
                    // populate register context
                    for (IdentificadorContext ctxVariableRegisterIdent : ctxVariableRegister.identificador()) {
                        lock = false;
                        String registerFieldName = ctxVariableRegisterIdent.getText();
                        SymbolTable currentScope = nestedScopes.getCurrentScope();

                        for (String registerIdentifier : registerIdentifiers) {
                            SymbolTableEntry entry = currentScope.check(registerIdentifier);
                            SymbolTable registerFields = entry.argsRegFunc;

                            String variableType = ctxVariableRegister.tipo().getText();
                            if(!lock){
                                defineTypeAndAddtoScope(registerFieldName, variableType, registerFields);
                            }
                            
                        }
                        lock = true;
                    }
                }
                output.append("    }"); 
                for(String registerIdentifier : registerIdentifiers){
                    output.append(registerIdentifier);
                }
                output.append(";\n");
            }
        }
        return null;
    }

    @Override
    public Void visitDeclaracao_global(Declaracao_globalContext ctx){
        String identifier = ctx.IDENT().getText();

        // Geting scopes
        List<SymbolTable> scopes = nestedScopes.runNestedScopes();
        if (scopes.size() > 1) {
            nestedScopes.giveupScope();
        }
        SymbolTable globalScope = nestedScopes.getCurrentScope();
       

        if(ctx.tipo_estendido() != null){
            //Funcao com retorno

            nestedScopes.createNewScope();
            SymbolTable functionScope = nestedScopes.getCurrentScope();
            functionScope.setGlobal(globalScope);

            var returnType = ctx.tipo_estendido().getText();

            defineTypeAndAddtoScope(identifier, returnType, functionScope);
            output.append("(");

            boolean firstParameter = true;
            for(LAParser.ParametroContext declaredParameter: ctx.parametros().parametro()){
                String variableType =  declaredParameter.tipo_estendido().getText();

                for(LAParser.IdentificadorContext ident: declaredParameter.identificador()){
                    String parameterIdentifier = ident.getText();

                    if(!firstParameter){
                        output.append(",");
                    }
                    if(!defineTypeAndAddtoScope(parameterIdentifier, variableType, functionScope)){
                        
                        if (globalScope.exists(variableType) && globalScope.check(
                                    variableType).identifierType == SymbolTable.TypeLAIdentifier.TIPO){
                            SymbolTableEntry fields = globalScope.check(variableType);
                            SymbolTable nestedTableType = fields.argsRegFunc;

                            functionScope.put(parameterIdentifier,
                                    SymbolTable.TypeLAIdentifier.REGISTRO,
                                    SymbolTable.TypeLAVariable.REGISTRO, nestedTableType,
                                    variableType);
                        }

                    }
                    firstParameter = false;

                }
            }
            output.append(") {\n");
            

        }else{
            //Procedure (função sem retorno)
            nestedScopes.createNewScope();
            SymbolTable procScope = nestedScopes.getCurrentScope();
            procScope.setGlobal(globalScope);

            output.append("void "+ identifier + "(");
            boolean firstParameter = true;

            for(LAParser.ParametroContext declaredParameter: ctx.parametros().parametro()){
                String variableType =  declaredParameter.tipo_estendido().getText();

                for(LAParser.IdentificadorContext ident: declaredParameter.identificador()){
                    //After declaring a type of a parameter, is possible to declare multiple parameters of same type
                    String parameterIdentifier = ident.getText();

                    if(!firstParameter){
                        output.append(",");
                    }
                    if(!defineTypeAndAddtoScope(parameterIdentifier, variableType, procScope)){
                        if (globalScope.exists(variableType) && globalScope.check(
                                    variableType).identifierType == SymbolTable.TypeLAIdentifier.TIPO){
                            SymbolTableEntry fields = globalScope.check(variableType);
                            SymbolTable nestedTableType = fields.argsRegFunc;

                            procScope.put(parameterIdentifier,
                                    SymbolTable.TypeLAIdentifier.REGISTRO,
                                    SymbolTable.TypeLAVariable.REGISTRO, nestedTableType,
                                    variableType);
                        }
                        
                    }
                    firstParameter = false;
                }
            }
            


        }
        

        return null;
    }

    // vistCmdChamada treats function calls
    @Override
    public Void visitCmdChamada(LAParser.CmdChamadaContext ctx) {
        output.append("    " + ctx.getText() + ";\n");

        return null;
    }


    @Override
    public Void visitCmdAtribuicao(CmdAtribuicaoContext ctx){
        var currentScope = nestedScopes.getCurrentScope();

        if(ctx.getText().contains("^"))
            output.append("*");
        try{
            SymbolTable.TypeLAVariable variableType = currentScope.check(ctx.identificador().getText()).variableType;

            if(variableType != null && variableType == SymbolTable.TypeLAVariable.LITERAL){
                // output.append("strcpy(" + ctx.identificador().getText()+","+ctx.expressao().getText()+");\n");
                output.append("strcpy(");
                visitIdentificador(ctx.identificador());
                output.append(","+ctx.expressao().getText()+");\n");
            }
            else{
                // output.append(ctx.identificador().getText());
                visitIdentificador(ctx.identificador());
                output.append(" = ");
                output.append(ctx.expressao().getText());
                output.append(";\n");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage() +  " q ocorreu");
        }
        return null;
    }

    @Override
    public Void visitCmdLeia(CmdLeiaContext ctx){
        var currentScope = nestedScopes.getCurrentScope();
        for(LAParser.IdentificadorContext id: ctx.identificador()) {
            SymbolTable.TypeLAVariable variableType = currentScope.check(id.getText()).variableType;
            if(variableType != SymbolTable.TypeLAVariable.LITERAL){
                output.append("scanf(\"%");
                output.append(getCTypeSymbol(variableType));
                output.append("\", &");
                output.append(id.getText());
                output.append(");\n");
            } else {
                output.append("gets(");
                visitIdentificador(id);
                output.append(");\n");
            }
        }
        
        return null;
    }

    @Override
    public Void visitCmdEscreva(CmdEscrevaContext ctx) { // comando para escrever a variavel, verifica o tipo ou na tabela, ou no utils
        for(LAParser.ExpressaoContext exp: ctx.expressao()) {
            SymbolTable currentScope = nestedScopes.getCurrentScope();
            String cType = getCTypeSymbol(LASemanticUtils.verifyType(currentScope, exp));
            if(currentScope.exists(exp.getText())){
                SymbolTable.TypeLAVariable variableType = currentScope.check(exp.getText()).variableType;
                cType = getCTypeSymbol(variableType);
            }
            output.append("printf(\"%");
            output.append(cType);
            output.append("\", ");
            output.append(exp.getText());
            output.append(");\n");
        }
        return null;
    }
    
    @Override
    public Void visitCorpo(LAParser.CorpoContext ctx) {
        List<SymbolTable> scopes = nestedScopes.runNestedScopes();
        if (scopes.size() > 1) {
            nestedScopes.giveupScope();
        }

        return super.visitCorpo(ctx);
    }

    @Override
    public Void visitCmdRetorne(CmdRetorneContext ctx) {//adiciona return, e pega a expressao que vai retornar
        output.append("return ");
        visitExpressao(ctx.expressao());
        output.append(";\n");
        return null;
    }

    @Override
    public Void visitCmdSe(CmdSeContext ctx) {//transcrição do comando if else
        output.append("if(");
        visitExpressao(ctx.expressao());
        output.append(") {\n");
        for(CmdContext cmd : ctx.cmd()) {
            visitCmd(cmd);
        }
        output.append("}\n");
        if(ctx.getText().contains("senao")){
            output.append("else {\n");
            for(CmdContext cmd : ctx.cmdElse) {
                visitCmd(cmd);
            }
            output.append("}\n");
        }
        
        return null;
    }

    @Override
    public Void visitExpressao(ExpressaoContext ctx) {//usado para visitar uma expressao, que e constituida de termos e operadores
        if(ctx.termo_logico() != null){
            visitTermo_logico(ctx.termo_logico(0));

            for(int i = 1; i < ctx.termo_logico().size(); i++){
                LAParser.Termo_logicoContext termo = ctx.termo_logico(i);
                output.append(" || ");
                visitTermo_logico(termo);
            }
        }

        return null;
    }

    @Override
    public Void visitTermo_logico(Termo_logicoContext ctx) {//usado para visitar termos logicos
        visitFator_logico(ctx.fator_logico(0));

        for(int i = 1; i < ctx.fator_logico().size(); i++){
            LAParser.Fator_logicoContext fator = ctx.fator_logico(i);
            output.append(" && ");
            visitFator_logico(fator);
        }
        
        return null;
    }

    @Override
    public Void visitFator_logico(Fator_logicoContext ctx) {// usado para visitar fatores logicos
        if(ctx.getText().startsWith("nao")){
            output.append("!");
        }
        visitParcela_logica(ctx.parcela_logica());
        
        return null;
    }

    @Override
    public Void visitParcela_logica(Parcela_logicaContext ctx) {//usado para visitar parcelas logicas
        if(ctx.exp_relacional() != null){
            visitExp_relacional(ctx.exp_relacional());
        } else{
            if(ctx.getText() == "verdadeiro"){
                output.append("true");
            } else {
                output.append("false");
            }
        }
        
        return null;
    }

    // usado para expressoes relacionais, convertendo o simbolo de igualdade para o equivalente em c
    @Override
    public Void visitExp_relacional(Exp_relacionalContext ctx) {
         visitExp_aritmetica(ctx.exp_aritmetica(0));
        for(int i = 1; i < ctx.exp_aritmetica().size(); i++){
            LAParser.Exp_aritmeticaContext termo = ctx.exp_aritmetica(i);
            if(ctx.op_relacional().getText().equals("=")){
                output.append(" == ");
            } else{
                output.append(ctx.op_relacional().getText());
            }
            visitExp_aritmetica(termo);
        }
        
        return null;
    }

    @Override
    public Void visitExp_aritmetica(Exp_aritmeticaContext ctx) {//visitar expressoes aritmeticas
        visitTermo(ctx.termo(0));

        for(int i = 1; i < ctx.termo().size(); i++){
            LAParser.TermoContext termo = ctx.termo(i);
            output.append(ctx.op1(i-1).getText());
            visitTermo(termo);
        }
        return null;
    }

    @Override
    public Void visitTermo(TermoContext ctx) {//visita o termo para verificar se tem fatores
       visitFator(ctx.fator(0));

        for(int i = 1; i < ctx.fator().size(); i++){
            LAParser.FatorContext fator = ctx.fator(i);
            output.append(ctx.op2(i-1).getText());
            visitFator(fator);
        }
        return null;
    }

    @Override
    public Void visitFator(FatorContext ctx) {//visita o fator para verificar se tem parcelas
        visitParcela(ctx.parcela(0));

        for(int i = 1; i < ctx.parcela().size(); i++){
            LAParser.ParcelaContext parcela = ctx.parcela(i);
            output.append(ctx.op3(i-1).getText());
            visitParcela(parcela);
        }
        return null;
    }

    @Override
    public Void visitParcela(ParcelaContext ctx) {//redireciona parcela para unaria ou nao unaria
        if(ctx.parcela_unario() != null){
            if(ctx.op_unario() != null){
                output.append(ctx.op_unario().getText());
            }
            visitParcela_unario(ctx.parcela_unario());
        } else{
            visitParcela_nao_unario(ctx.parcela_nao_unario());
        }
        
        return null;
    }

    @Override
    public Void visitParcela_unario(Parcela_unarioContext ctx) {
        //visitar parcela unario imprimindo todos os identificadores, ou redireciona caso chegou aqui com uma expressao ent
        if(ctx.IDENT() != null){
            output.append(ctx.IDENT().getText());
            output.append("(");
            for(int i = 0; i < ctx.expressao().size(); i++){
                visitExpressao(ctx.expressao(i));
                if(i < ctx.expressao().size()-1){
                    output.append(", ");
                }
            }
        } else if(ctx.AP() != null){
            output.append("(");
            ctx.expressao().forEach( exp -> visitExpressao(exp));
            output.append(")");
        }
        else {
            output.append(ctx.getText());
        }
        
        return null;
    }

    @Override
    public Void visitParcela_nao_unario(Parcela_nao_unarioContext ctx) {//parcela nao unaria é só o valor do campo
        output.append(ctx.getText());
        return null;
    }

    @Override
    public Void visitCmdCaso(CmdCasoContext ctx) {//switch case, tratando intervalos, com visita a expressao aritmetica
        output.append("switch(");
        visit(ctx.exp_aritmetica());
        output.append("){\n");
        visit(ctx.selecao());

        if (ctx.getText().contains("senao")) {
            output.append("    default:\n");
            ctx.cmd().forEach(cmd -> visitCmd(cmd));
            output.append("    }\n");
        }

        return null;
    }
    @Override
    public Void visitSelecao(SelecaoContext ctx) {//visita todas os itens da selecao
        ctx.item_selecao().forEach(var -> visitItem_selecao(var));
        return null;
    }
    @Override
    public Void visitItem_selecao(Item_selecaoContext ctx) {// cadda item deve ser tratado para caso seja um intervalo imprima todos os cases do mesmo
        ArrayList<String> intervalo = new ArrayList<>(Arrays.asList(ctx.constantes().getText().split("\\.\\.")));
        String first = intervalo.size() > 0 ? intervalo.get(0) : ctx.constantes().getText();
        String last = intervalo.size() > 1 ? intervalo.get(1) : intervalo.get(0);
        for(int i = Integer.parseInt(first); i <= Integer.parseInt(last); i++){
            output.append("case " + i + ":\n");
            ctx.cmd().forEach(var -> visitCmd(var));
            output.append("break;\n");
        }
        return null;
    }

    @Override
    public Void visitCmdPara(CmdParaContext ctx) {//criando loop for, ate o valor passado depois do literall ate
        String id = ctx.IDENT().getText();
        output.append("for(" + id + " = ");
        visitExp_aritmetica(ctx.exp_aritmetica(0));
        output.append("; " + id + " <= ");
        visitExp_aritmetica(ctx.exp_aritmetica(1));
        output.append("; " + id + "++){\n");
        ctx.cmd().forEach(var -> visitCmd(var));
        output.append("}\n");
        return null;
    }

    @Override
    public Void visitCmdEnquanto(CmdEnquantoContext ctx) {//cmd enquando loop while em c
        // TODO Auto-generated method stub
        output.append("while(");
        visitExpressao(ctx.expressao());
        output.append("){\n");
        ctx.cmd().forEach(var -> visitCmd(var));
        output.append("}\n");
        return null;
    }

    @Override
    public Void visitCmdFaca(CmdFacaContext ctx) {//comando faca loop do while em c
        // TODO Auto-generated method stub
        output.append("do{\n");
        ctx.cmd().forEach(var -> visitCmd(var));
        output.append("} while(");
        visitExpressao(ctx.expressao());
        output.append(");\n");
        return null;
    }

    @Override
    public Void visitIdentificador(IdentificadorContext ctx) {//criado para imprimir identificadores com dimensoes
        // TODO Auto-generated method stub
        output.append(" ");
        int i = 0;
        for(TerminalNode id : ctx.IDENT()){
            if(i++ > 0)
                output.append(".");
            output.append(id.getText());
        }
        visitDimensao(ctx.dimensao());
        return null;
    }
}
