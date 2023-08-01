package br.ufscar.dc.compiladores.LA;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {

    public enum TypeLAVariable{ //tipos de variavel em LA
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

    public enum TypeLAIdentifier{ //tipos de identificadores
        VARIAVEL,
        CONSTANTE,
        TIPO,
        PROCEDIMENTO,
        FUNCAO,
        REGISTRO
    }

    private HashMap<String, SymbolTableEntry> symbolTable; //tabela de simbolos em hashmap
    private SymbolTable global;

    public SymbolTable() {
        this.symbolTable = new HashMap<>();
        this.global = null;
    }

    //define tabela de simbolos global
    void setGlobal(SymbolTable global){
        this.global = global;
    }

    //add uma entrada na tabela de simbolos so com nome e tipo do identificador e variavel
    public void put(String name, TypeLAIdentifier identifierType, TypeLAVariable variableType) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identifierType = identifierType;
        ste.variableType = variableType;
        symbolTable.put(name, ste);
    }

    //add na tabela de simbolos com nome e tipo do ident e variavel, e uma tabela de simbolos de argumentos 
    public void put(String name, TypeLAIdentifier identifierType, TypeLAVariable variableType, SymbolTable argsRegFunc) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identifierType = identifierType;
        ste.variableType = variableType;
        ste.argsRegFunc = argsRegFunc;
        symbolTable.put(name, ste);
    }

    //add na tabela de simbolos com nome e tipo do ident e variavel, uma tabela de simbolos de argumentos e o tipo de uma função
    public void put(String name, TypeLAIdentifier identifierType, TypeLAVariable variableType, SymbolTable argsRegFunc, String funcType) {
        SymbolTableEntry ste = new SymbolTableEntry();
        ste.name = name;
        ste.identifierType = identifierType;
        ste.variableType = variableType;
        ste.argsRegFunc = argsRegFunc;
        ste.functionType = funcType;
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
    public boolean validType(ArrayList<SymbolTable.TypeLAVariable> types){
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
    
}
