package br.ufscar.dc.compiladores.csvValidator;

public class SymbolTableEntry {
    public String name;
    public SymbolTable.TypeCSVIdent identType; //Tipo de identificador (definicao de tabela ou atributo da tabela)
    public SymbolTable.TypeCSVAtribute variableType; //tipo de variavel
    public int size;
    public boolean regra;

    public SymbolTable definicaoAtributos;

}
