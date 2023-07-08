package br.ufscar.dc.compiladores.LA;

import java.util.HashMap;

public class SymbolTable {

    public enum TypeLAVariable{
        LITERAL,
        INTEIRO,
        REAL,
        LOGICO,
        NAO_DECLARADO,
        INVALIDO
    }

    public enum TypeLAIdentifier{
        VARIAVEL,
        CONSTANTE,
        TIPO,
        PROCEDIMENTO,
        FUNCAO
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
    /* 
    public void put(String name, TypeLAIdentifier identifierType, TypeLAVariable variableType, SymbolTable argsRegFunc) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identifierType = identifierType;
        ste.variableType = variableType;
        ste.argsRegFunc = argsRegFunc;
        symbolTable.put(name, ste);
    }

    public void put(String name, TypeLAIdentifier identifierType, TypeLAVariable variableType, SymbolTable argsRegFunc, String specialType) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identifierType = identifierType;
        ste.variableType = variableType;
        ste.argsRegFunc = argsRegFunc;
        ste.specialType = specialType;
        symbolTable.put(name, ste);
    } */

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
    
    /* // type validation for registers and functions
    public boolean validType(ArrayList<SymbolTable.TypeLAVariable> types){
        int counter = 0;
        
        if(symbolTable.size() != types.size())
            return false;
        for(var entry: symbolTable.values()){
            if(types.get(counter) != entry.variableType){
                return false;
            }
            counter++;
        }
        
        return true;
    } */
}