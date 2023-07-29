package br.ufscar.dc.compiladores.LA;


import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import br.ufscar.dc.compiladores.LA.LAParser.Exp_aritmeticaContext;
import br.ufscar.dc.compiladores.LA.LAParser.ExpressaoContext;
import br.ufscar.dc.compiladores.LA.LAParser.FatorContext;
import br.ufscar.dc.compiladores.LA.LAParser.Fator_logicoContext;
import br.ufscar.dc.compiladores.LA.LAParser.ParcelaContext;
import br.ufscar.dc.compiladores.LA.LAParser.TermoContext;
import br.ufscar.dc.compiladores.LA.LAParser.Termo_logicoContext;
import br.ufscar.dc.compiladores.LA.SymbolTable.TypeLAVariable;

public class LASemanticUtils {
    //  Criação de uma lista para armazenar os erros semânticos
    public static List<String> semanticErrors = new ArrayList<>();
    
    // Adiciona um erro semântico à lista de erros. Recebe um Token e uma 
    // mensagem como parâmetros, obtém o número da linha do token e adiciona 
    // o erro formatado à lista.
    public static void addSemanticError(Token t, String msg) {
        int line = t.getLine();
        semanticErrors.add(String.format("Linha %d: %s", line, msg));
    }

    // Obtém o tipo do símbolo a partir da tabela de símbolos
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable symbolTable,
            LAParser.IdentificadorContext ctx) {
        String identifier = ctx.getText();

        if (!identifier.contains("[") && !identifier.contains("]")){
            //No dimensions
            String[] part = identifier.split("\\.");

            if(!symbolTable.exists(part[0])){
                addSemanticError(ctx.IDENT(0).getSymbol(), "identificador " + identifier + " nao declarado\n");
            }
            else{
                SymbolTableEntry ident = symbolTable.check(part[0]);
                if(ident.identifierType == SymbolTable.TypeLAIdentifier.REGISTRO 
                && part.length > 1){
                    SymbolTable fields = ident.argsRegFunc;
                    if(!fields.exists(part[1])){
                        addSemanticError(ctx.IDENT(0).getSymbol(), "identificador " + identifier + " nao declarado\n");
                    }
                    else{
                        SymbolTableEntry ste = fields.check(part[1]);
                        if (ste.variableType == SymbolTable.TypeLAVariable.INTEIRO)
                            return SymbolTable.TypeLAVariable.INTEIRO;
                        if (ste.variableType == SymbolTable.TypeLAVariable.LITERAL)
                            return SymbolTable.TypeLAVariable.LITERAL;
                        if (ste.variableType == SymbolTable.TypeLAVariable.REAL)
                            return SymbolTable.TypeLAVariable.REAL;
                        if (ste.variableType == SymbolTable.TypeLAVariable.LOGICO)
                            return SymbolTable.TypeLAVariable.LOGICO;
                        if (ste.variableType == SymbolTable.TypeLAVariable.PONT_INTE)
                            return SymbolTable.TypeLAVariable.PONT_INTE;
                        if (ste.variableType == SymbolTable.TypeLAVariable.PONT_REAL)
                            return SymbolTable.TypeLAVariable.PONT_REAL;
                        if (ste.variableType == SymbolTable.TypeLAVariable.PONT_LOGI)
                            return SymbolTable.TypeLAVariable.PONT_LOGI;
                        if (ste.variableType == SymbolTable.TypeLAVariable.PONT_LITE)
                            return SymbolTable.TypeLAVariable.PONT_LITE;
                    }
                }
                if (ident.identifierType == SymbolTable.TypeLAIdentifier.REGISTRO
                        && part.length == 1) {
                    return SymbolTable.TypeLAVariable.REGISTRO;
                }
                if (ident.variableType == SymbolTable.TypeLAVariable.INTEIRO)
                    return SymbolTable.TypeLAVariable.INTEIRO;
                if (ident.variableType == SymbolTable.TypeLAVariable.LITERAL)
                    return SymbolTable.TypeLAVariable.LITERAL;
                if (ident.variableType == SymbolTable.TypeLAVariable.REAL)
                    return SymbolTable.TypeLAVariable.REAL;
                if (ident.variableType == SymbolTable.TypeLAVariable.LOGICO)
                    return SymbolTable.TypeLAVariable.LOGICO;
                if (ident.variableType == SymbolTable.TypeLAVariable.PONT_INTE)
                    return SymbolTable.TypeLAVariable.PONT_INTE;
                if (ident.variableType == SymbolTable.TypeLAVariable.PONT_REAL)
                    return SymbolTable.TypeLAVariable.PONT_REAL;
                if (ident.variableType == SymbolTable.TypeLAVariable.PONT_LOGI)
                    return SymbolTable.TypeLAVariable.PONT_LOGI;
                if (ident.variableType == SymbolTable.TypeLAVariable.PONT_LITE)
                    return SymbolTable.TypeLAVariable.PONT_LITE;
            }
        }
        else{
            // With dimension
            String identifierNoDim = "";
            // Ignores dimension and sees if variable already declared
            for (TerminalNode identCtx : ctx.IDENT())
                identifierNoDim += identCtx.getText();

            for (Exp_aritmeticaContext xp : ctx.dimensao().exp_aritmetica())
                verifyType(symbolTable, xp);

            if (!symbolTable.exists(identifierNoDim)) {
                addSemanticError(ctx.IDENT(0).getSymbol(), "identificador " + identifierNoDim + " nao declarado\n");
            }
            else{
                SymbolTableEntry ident = symbolTable.check(identifierNoDim);
                if (ident.variableType == SymbolTable.TypeLAVariable.INTEIRO)
                    return SymbolTable.TypeLAVariable.INTEIRO;
                if (ident.variableType == SymbolTable.TypeLAVariable.LITERAL)
                    return SymbolTable.TypeLAVariable.LITERAL;
                if (ident.variableType == SymbolTable.TypeLAVariable.REAL)
                    return SymbolTable.TypeLAVariable.REAL;
                if (ident.variableType == SymbolTable.TypeLAVariable.LOGICO)
                    return SymbolTable.TypeLAVariable.LOGICO;
                if (ident.identifierType == SymbolTable.TypeLAIdentifier.REGISTRO)
                    return SymbolTable.TypeLAVariable.REGISTRO;
            }
        }   
        return SymbolTable.TypeLAVariable.NAO_DECLARADO;
    }

    // Verifica o tipo em contexto de expressão
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable symbolTable,
            LAParser.ExpressaoContext ctx) {
        SymbolTable.TypeLAVariable ret = null;
        for (Termo_logicoContext tl : ctx.termo_logico()) {
            SymbolTable.TypeLAVariable aux = verifyType(symbolTable, tl);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo em contexto de termo lógico
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable symbolTable, LAParser.Termo_logicoContext ctx){
        SymbolTable.TypeLAVariable ret = null;
        for (Fator_logicoContext fL : ctx.fator_logico()){
            SymbolTable.TypeLAVariable aux = verifyType(symbolTable, fL);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo em contexto de fator lógico
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Fator_logicoContext ctx) {
        return verifyType(table, ctx.parcela_logica());
    }

    // Verifica o tipo em contexto de parcela lógica
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Parcela_logicaContext ctx) {
        if (ctx.exp_relacional() != null) {
            return verifyType(table, ctx.exp_relacional());
        } else {
            return SymbolTable.TypeLAVariable.LOGICO;
        }
    }

    // Verifica o tipo em contexto de expressão relacional
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Exp_relacionalContext ctx) {
        SymbolTable.TypeLAVariable ret = null;
        if (ctx.exp_aritmetica().size() == 1)
            for (Exp_aritmeticaContext ea : ctx.exp_aritmetica()) {
                TypeLAVariable aux = verifyType(table, ea);
                if (ret == null) {
                    ret = aux;
                } else if (!verifyType(ret, aux)) {
                    ret = SymbolTable.TypeLAVariable.INVALIDO;
                }
        } else {
            for (Exp_aritmeticaContext ea : ctx.exp_aritmetica()) {
                verifyType(table, ea);
            }

            return SymbolTable.TypeLAVariable.LOGICO;
        }

        return ret;
    }

    // Verifica o tipo em contexto de expressão aritmética
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Exp_aritmeticaContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        for (TermoContext te : ctx.termo()) {
            TypeLAVariable aux = verifyType(table, te);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }
        return ret;
    }
    // Verifica o tipo em contexto de termo
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.TermoContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        for (FatorContext fa : ctx.fator()) {
            TypeLAVariable aux = verifyType(table, fa);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }
        return ret;
    }

    // Verifica o tipo em contexto de fator
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.FatorContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        for (ParcelaContext pa : ctx.parcela()) {
            TypeLAVariable aux = verifyType(table, pa);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }

        return ret;
    }

    // Verifica o tipo em contexto de parcela
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.ParcelaContext ctx) {

        if (ctx.parcela_unario() != null) {
            return verifyType(table, ctx.parcela_unario());
        } else {
            return verifyType(table, ctx.parcela_nao_unario());
        }
    }

    // Verifica o tipo em contexto de parcela unária
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable symbolTable,
            LAParser.Parcela_unarioContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        if (ctx.NUM_INT() != null) {
            return SymbolTable.TypeLAVariable.INTEIRO;
        }
        if (ctx.NUM_REAL() != null) {
            return SymbolTable.TypeLAVariable.REAL;
        }
        if (ctx.IDENT() != null) {
            // function
            if (!symbolTable.exists(ctx.IDENT().getText())) {
                addSemanticError(ctx.identificador().IDENT(0).getSymbol(),
                        "identificador " + ctx.IDENT().getText() + " nao declarado\n");
            }

            for (ExpressaoContext exp : ctx.expressao()) {
                TypeLAVariable aux = verifyType(symbolTable, exp);
                if (ret == null) {
                    ret = aux;
                } else if (!verifyType(ret, aux)) {
                    ret = SymbolTable.TypeLAVariable.INVALIDO;
                }
            }

            if (symbolTable.exists(ctx.IDENT().getText())) {
                // return type
                SymbolTableEntry function = symbolTable.check(ctx.IDENT().getText()); 
                switch (function.functionType) {
                    case "inteiro":
                        ret = SymbolTable.TypeLAVariable.INTEIRO;
                        break;
                    case "literal":
                        ret = SymbolTable.TypeLAVariable.LITERAL;
                        break;
                    case "real":
                        ret = SymbolTable.TypeLAVariable.REAL;
                        break;
                    case "logico":
                        ret = SymbolTable.TypeLAVariable.LOGICO;
                        break;
                    case "^logico":
                        ret = SymbolTable.TypeLAVariable.PONT_LOGI;
                        break;
                    case "^real":
                        ret = SymbolTable.TypeLAVariable.PONT_REAL;
                        break;
                    case "^literal":
                        ret = SymbolTable.TypeLAVariable.PONT_LITE;
                        break;
                    case "^inteiro":
                        ret = SymbolTable.TypeLAVariable.PONT_INTE;
                        break;
                    default:
                        ret = SymbolTable.TypeLAVariable.REGISTRO;
                        break;
                }

                // Parameter type and number
                String nameFun = ctx.IDENT().getText();
                SymbolTableEntry funProc = symbolTable.check(nameFun);

                ArrayList<SymbolTable.TypeLAVariable> parameterTypes = new ArrayList<>();

                for (ExpressaoContext exp : ctx.expressao()) {
                    parameterTypes.add(verifyType(symbolTable, exp));
                }

                if (!funProc.argsRegFunc.validType(parameterTypes)) {
                    addSemanticError(ctx.IDENT().getSymbol(),
                            "incompatibilidade de parametros na chamada de " + nameFun + "\n");
                }
            }
        }

        if (ctx.identificador() != null) {
            return verifyType(symbolTable, ctx.identificador());
        }

        if (ctx.IDENT() == null && ctx.expressao() != null) {
            for (ExpressaoContext exp : ctx.expressao()) {
                return verifyType(symbolTable, exp);
            }
        }

        return ret;
    }

    // Verifica se os tipos de atribuição são válidos
    public static boolean verifyType(SymbolTable.TypeLAVariable tipo1, SymbolTable.TypeLAVariable tipo2) {
        if (tipo1 == tipo2)
            return true;
        if (tipo1 == SymbolTable.TypeLAVariable.NAO_DECLARADO
                || tipo2 == SymbolTable.TypeLAVariable.NAO_DECLARADO)
            return true;
        if (tipo1 == SymbolTable.TypeLAVariable.INVALIDO || tipo2 == SymbolTable.TypeLAVariable.INVALIDO)
            return false;
        if ((tipo1 == SymbolTable.TypeLAVariable.INTEIRO || tipo1 == SymbolTable.TypeLAVariable.REAL) &&
                (tipo2 == SymbolTable.TypeLAVariable.INTEIRO || tipo2 == SymbolTable.TypeLAVariable.REAL))
            return true;
        if ( 
                (
                    tipo1 == SymbolTable.TypeLAVariable.PONT_INTE 
                    || 
                    tipo1 == SymbolTable.TypeLAVariable.PONT_REAL 
                    || 
                    tipo1 == SymbolTable.TypeLAVariable.PONT_LOGI 
                    || 
                    tipo1 == SymbolTable.TypeLAVariable.PONT_LOGI 
                ) 
                && 
                tipo2 == SymbolTable.TypeLAVariable.ENDERECO
            )
            return true;
        if (tipo1 != tipo2)
            return false;

        return true;
    }

    // Verifica o tipo em contexto de parcela não unária
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Parcela_nao_unarioContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        if (ctx.CADEIA() != null) {
            ret = SymbolTable.TypeLAVariable.LITERAL;
        } 
        else {
            ret = verifyType(table, ctx.identificador());
            if (ctx.getText().contains("&")) {
                return SymbolTable.TypeLAVariable.ENDERECO;
            }
        }
        return ret;
    }

}
