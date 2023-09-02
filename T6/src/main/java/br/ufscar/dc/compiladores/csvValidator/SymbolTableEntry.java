package br.ufscar.dc.compiladores.csvValidator;

public class SymbolTableEntry {
    public String name;
    public SymbolTable.TypeCSVIdent identType;
    public SymbolTable.TypeCSVAtribute variableType; //tipo de variavel
    public int size;

    public SymbolTable definicaoAtributos;

}
