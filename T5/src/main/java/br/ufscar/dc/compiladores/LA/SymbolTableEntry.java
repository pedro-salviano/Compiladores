package br.ufscar.dc.compiladores.LA;

public class SymbolTableEntry {
    public String name;
    public SymbolTable.TypeLAIdentifier identifierType;
    public SymbolTable.TypeLAVariable variableType;

    public SymbolTable argsRegFunc;
    public String functionType;

}
