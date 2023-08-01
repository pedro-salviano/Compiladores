package br.ufscar.dc.compiladores.LA;

public class SymbolTableEntry {
    public String name;
    public SymbolTable.TypeLAIdentifier identifierType; //tipo de identif
    public SymbolTable.TypeLAVariable variableType; //tipo de variavel

    public SymbolTable argsRegFunc; //tabela de simbolos que armazena os argumentos de uma func ou registro
    public String functionType; // retorno da func representada pela tabela de simbolos

}
