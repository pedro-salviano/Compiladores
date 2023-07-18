package br.ufscar.dc.compiladores.LA;

import java.util.HashMap;

public class SymbolTable {

    public enum TypeLAVariable{
        LITERAL,
        INTEIRO,
        REAL,
        LOGICO,
        NAO_DECLARADO,
        INVALIDO,
        PONT_INTE,
        PONT_REAL,
        PONT_LOGI,
        PONT_LITE,
        ENDERECO,
        REGISTRO
    }

    public enum TypeLAIdentifier{
        VARIAVEL,
        CONSTANTE,
        TIPO,
        PROCEDIMENTO,
        FUNCAO,
        REGISTRO
    }

    private HashMap<String, SymbolTableEntry> symbolTable;
    private SymbolTable global;

    public SymbolTable() {
        this.symbolTable = new HashMap<>();
        this.global = null;
    }

    void setGlobal(SymbolTable global){
        this.global = global;
    }

    public void put(String name, TypeLAIdentifier identifierType, TypeLAVariable variableType) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identifierType = identifierType;
        ste.variableType = variableType;
        symbolTable.put(name, ste);
    }

    public void put(String name, TypeLAIdentifier identifierType, TypeLAVariable variableType, SymbolTable argsRegFunc) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identifierType = identifierType;
        ste.variableType = variableType;
        ste.argsRegFunc = argsRegFunc;
        symbolTable.put(name, ste);
    }
   
    // returns true or false if an identifier exists in the table
    public boolean exists(String name) {
        if(global == null) {
            return symbolTable.containsKey(name);
        } else {
            return symbolTable.containsKey(name) || global.exists(name);
        }
    }

    // returns an entry of the symbol table given a name
    public SymbolTableEntry check(String name) {
        if(global == null)
            return symbolTable.get(name);
        else{
            if(symbolTable.containsKey(name))
                return symbolTable.get(name);
            else
                return global.check(name);
        }
    }
    
}