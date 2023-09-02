package br.ufscar.dc.compiladores.csvValidator;

import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class csvValidatorSemanticUtils {
    //  Criação de uma lista para armazenar os erros semânticos
    public static List<String> semanticErrors = new ArrayList<>();
    
    // Adiciona um erro semântico à lista de erros. Recebe um Token e uma 
    // mensagem como parâmetros, obtém o número da linha do token e adiciona 
    // o erro formatado à lista.
    public static void addSemanticError(Token t, String msg) {
        int line = t.getLine();
        semanticErrors.add(String.format("Linha %d: %s", line, msg));
    }
}
