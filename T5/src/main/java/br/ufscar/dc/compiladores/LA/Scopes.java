package br.ufscar.dc.compiladores.LA;

import java.util.LinkedList;
import java.util.List;

public class Scopes {

    private LinkedList<SymbolTable> tablesStack;

    public Scopes() {
        tablesStack = new LinkedList<>();
        createNewScope();
    }

    public void createNewScope() {
        tablesStack.push(new SymbolTable());
    }

    public SymbolTable getCurrentScope() {
        return tablesStack.peek();
    }

    public List<SymbolTable> runNestedScopes() {
        return tablesStack;
    }

    public void giveupScope() {
        tablesStack.pop();
    }
}