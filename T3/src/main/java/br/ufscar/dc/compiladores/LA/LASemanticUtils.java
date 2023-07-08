package br.ufscar.dc.compiladores.LA;

import br.ufscar.dc.compiladores.LA.LAParser;

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

        return SymbolTable.TypeLAVariable.NAO_DECLARADO;
    }

}
