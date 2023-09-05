package br.ufscar.dc.compiladores.csvValidator;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    public enum TypeCSVAtribute{ //tipos de variavel em LA
        LITERAL,
        INTEIRO,
        REAL,
        BOOLEANO
    }

    public enum TypeCSVIdent{
        DEFINICAO,
        ATRIBUTO
    }

    private HashMap<String, SymbolTableEntry> symbolTable; //tabela de simbolos em hashmap
    private SymbolTable global;
    private boolean existeRegra;

    public SymbolTable() {
        this.symbolTable = new HashMap<>();
        this.global = null;
        this.existeRegra = false;
    }

    //define tabela de simbolos global
    void setGlobal(SymbolTable global){
        this.global = global;
    }


    //add uma entrada na tabela de simbolos so com nome e tipo do identificador e variavel
    public void put(String name, TypeCSVIdent identType, TypeCSVAtribute variableType, boolean regra) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identType = identType;
        ste.variableType = variableType;
        ste.regra = regra;
        symbolTable.put(name, ste);

        if(regra){
            this.existeRegra = true;
        }
    }

    public void put(String name, TypeCSVIdent identType, TypeCSVAtribute variableType, boolean regra, int size) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identType = identType;
        ste.variableType = variableType;
        ste.regra = regra;
        ste.size = size;
        symbolTable.put(name, ste);

        if(regra){
            this.existeRegra = true;
        }
    }

    public void put(String name, TypeCSVIdent identType, TypeCSVAtribute variableType, SymbolTable definicaoAtributos) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identType = identType;
        ste.variableType = variableType;
        ste.definicaoAtributos = definicaoAtributos;
        symbolTable.put(name, ste);
    }
   
    // true ou false se o identificador existe na tabela de simbolos
    public boolean exists(String name) {
        if(global == null) {
            return symbolTable.containsKey(name);
        } else {
            return symbolTable.containsKey(name) || global.exists(name);
        }
    }
    
    //retorna uma entrada da tabela de simbolos com tal nome
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
    
    //validacao de tipo para registradores e funcoes
    public boolean validType(ArrayList<SymbolTable.TypeCSVAtribute> types){
        int counter = 0;
        
        if(symbolTable.size() != types.size())
            return false;
        for(SymbolTableEntry entry: symbolTable.values()){
            if(types.get(counter) != entry.variableType){
                return false;
            }
            counter++;
        }
        
        return true;
    }

    public int size(){
        return symbolTable.size();
    }
    
    public boolean existeRegra(){
        return existeRegra;
    }
}
