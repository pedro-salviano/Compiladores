package br.ufscar.dc.compiladores.LA;

import java.util.LinkedList;
import java.util.List;

public class Scopes {

    private LinkedList<SymbolTable> tablesStack;

    public Scopes() {
        tablesStack = new LinkedList<>();
        createNewScope();
    }

    //cria novo escopo
    public void createNewScope() {
        tablesStack.push(new SymbolTable());
    }

    //obtem tabela de simbolos do escopo atual
    public SymbolTable getCurrentScope() {
        return tablesStack.peek();
    }

    //retorna a lista de tabelas de simbolo, representando escopos aninhados
    public List<SymbolTable> runNestedScopes() {
        return tablesStack;
    }

    //remove escopo atual
    public void giveupScope() {
        tablesStack.pop();
    }
}
