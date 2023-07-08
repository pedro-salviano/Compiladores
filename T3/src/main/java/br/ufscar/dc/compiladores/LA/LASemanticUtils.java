package br.ufscar.dc.compiladores.LA;


import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

public class LASemanticUtils {
    public static List<String> semanticErrors = new ArrayList<>();
    
    public static void addSemanticError(Token t, String msg) {
        int line = t.getLine();
        semanticErrors.add(String.format("Linha %d: %s", line, msg));
    }

    // Gets Symbol type from symbolTable
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable symbolTable,
            LAParser.IdentificadorContext ctx) {
        var identifier = ctx.getText();

        if (!symbolTable.exists(identifier)) {
            addSemanticError(ctx.IDENT(0).getSymbol(), "identificador " + identifier + " nao declarado\n");
        }
        else{
            SymbolTableEntry ident = symbolTable.check(identifier);
            if (ident.variableType == SymbolTable.TypeLAVariable.INTEIRO)
                return SymbolTable.TypeLAVariable.INTEIRO;
            if (ident.variableType == SymbolTable.TypeLAVariable.LITERAL)
                return SymbolTable.TypeLAVariable.LITERAL;
            if (ident.variableType == SymbolTable.TypeLAVariable.REAL)
                return SymbolTable.TypeLAVariable.REAL;
            if (ident.variableType == SymbolTable.TypeLAVariable.LOGICO)
                return SymbolTable.TypeLAVariable.LOGICO;
        }

        return SymbolTable.TypeLAVariable.NAO_DECLARADO;
    }

    // verifyType in context of expression
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable symbolTable,
            LAParser.ExpressaoContext ctx) {
        SymbolTable.TypeLAVariable ret = null;
        for (var tl : ctx.termo_logico()) {
            SymbolTable.TypeLAVariable aux = verifyType(symbolTable, tl);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }

        return ret;
    }

    // verifyType in context of termoLogico
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable symbolTable, LAParser.Termo_logicoContext ctx){
        SymbolTable.TypeLAVariable ret = null;
        for (var fL : ctx.fator_logico()){
            SymbolTable.TypeLAVariable aux = verifyType(symbolTable, fL);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }

        return ret;
    }

    // verifyType in context of logic factor
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Fator_logicoContext ctx) {
        return verifyType(table, ctx.parcela_logica());
    }

    // verifyType in context of logic parcel
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Parcela_logicaContext ctx) {
        if (ctx.exp_relacional() != null) {
            return verifyType(table, ctx.exp_relacional());
        } else {
            return SymbolTable.TypeLAVariable.LOGICO;
        }
    }

    // verifyType in context of relational expression
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Exp_relacionalContext ctx) {
        SymbolTable.TypeLAVariable ret = null;
        if (ctx.exp_aritmetica().size() == 1)
            for (var ea : ctx.exp_aritmetica()) {
                var aux = verifyType(table, ea);
                if (ret == null) {
                    ret = aux;
                } else if (!verifyType(ret, aux)) {
                    ret = SymbolTable.TypeLAVariable.INVALIDO;
                }
            } else {
            for (var ea : ctx.exp_aritmetica()) {
                verifyType(table, ea);
            }

            return SymbolTable.TypeLAVariable.LOGICO;
        }

        return ret;
    }

    // verifyType in context of arithmetic expression
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Exp_aritmeticaContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        for (var te : ctx.termo()) {
            var aux = verifyType(table, te);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }
        return ret;
    }

    // verifyType in context of term
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.TermoContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        for (var fa : ctx.fator()) {
            var aux = verifyType(table, fa);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }
        return ret;
    }

    // verifyType in context of factor
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.FatorContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        for (var pa : ctx.parcela()) {
            var aux = verifyType(table, pa);
            if (ret == null) {
                ret = aux;
            } else if (!verifyType(ret, aux)) {
                ret = SymbolTable.TypeLAVariable.INVALIDO;
            }
        }

        return ret;
    }

    // verifyType in context of parcel
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.ParcelaContext ctx) {

        if (ctx.parcela_unario() != null) {
            return verifyType(table, ctx.parcela_unario());
        } else {
            return verifyType(table, ctx.parcela_nao_unario());
        }
    }

    // verifyType in context of unary parcel
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

            for (var exp : ctx.expressao()) {
                var aux = verifyType(symbolTable, exp);
                if (ret == null) {
                    ret = aux;
                } else if (!verifyType(ret, aux)) {
                    ret = SymbolTable.TypeLAVariable.INVALIDO;
                }
            }
        }

        if (ctx.identificador() != null) {
            return verifyType(symbolTable, ctx.identificador());
        }

        if (ctx.IDENT() == null && ctx.expressao() != null) {
            for (var exp : ctx.expressao()) {
                return verifyType(symbolTable, exp);
            }
        }

        return ret;
    }

    // Checks if atribution types are valid
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
        if (tipo1 != tipo2)
            return false;

        return true;
    }

    // verifyType in context of parcel non unary
    public static SymbolTable.TypeLAVariable verifyType(SymbolTable table,
            LAParser.Parcela_nao_unarioContext ctx) {
        SymbolTable.TypeLAVariable ret = null;

        if (ctx.CADEIA() != null) {
            ret = SymbolTable.TypeLAVariable.LITERAL;
        }
        return ret;
    }

}
