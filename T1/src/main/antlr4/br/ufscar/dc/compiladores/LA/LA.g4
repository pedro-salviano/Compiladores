lexer grammar LA;

PALAVRA_CHAVE: //separa palavras chaves que nao podem ser utilizadas para outro proposito
    'algoritmo' | 'declare' | 'literal' | 'inteiro' | 'leia' | 'escreva' | 'fim_algoritmo' 
    | 'real' | '<-' | 'logico' | 'se' | 'entao' | 'senao' | 'fim_se' | 'caso' 
    | 'seja' | '..' | 'fim_caso' | 'para' | 'ate' | 'faca' | 'fim_para' | 'enquanto' | 'fim_enquanto' 
    | '%' | '^' | 'registro' | 'fim_registro' | '.' | 'tipo' | 'procedimento' | 'var' | 'fim_procedimento' 
    | 'funcao' | 'retorne' | 'fim_funcao' | 'constante' | 'falso' | 'verdadeiro' | 'e' | 'ou' | 'nao' | '&' ;

IDENT:    [a-zA-Z][a-zA-Z0-9_]*; //define os tokens aceitos para um identificador, e define que eles nao podem comecar com numero

OP_ARIT	:	'*' | '/' | '+' | '-'; //define os tokens que denotam operadores artimeticos

OP_REL	:	'<' | '<=' | '>=' | '>' | '=' | '<>'; //define os tokens que denotam operadores relacionais

OP_COMPR:   'e' | 'ou' | 'nao' | '&'; //define os tokens que denotam operadores logicos

DELIM:	':'; //token que denota o fim de um argumento

AP:	'('; //abre parenteses

FP:	')'; //fecha parenteses

VIRGULA :   ','; //virgula eh so uma virgula

AC: '['; //abre colchete
FC: ']'; //fecha colchete

fragment
DIGITO: ('0'..'9'); //define que digitos so podem ser compostos por numeros

NUM_INT:    DIGITO+; //numeros inteiros nao tem virgula

NUM_REAL:   DIGITO+ '.' DIGITO+; //numeros reais tem virgula

CADEIA: ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* ('\'' | '"'); // cadeias devem ser iniciadas por ' ou ", ter o finalizador (ex: \n),
            // e fechar as aspas simples ou dupla para serem validas

fragment
ESC_SEQ:    '\\\''; //aspas e barra

COMENTARIO: '{' ~('}' | '\n' | '\r')*  '}' { skip(); }; //um comentario deve ser iniciado com {, que nao pode conter \n, \r e } dentro dele,
    // e deve ser fechado com }

WS: (' '|'\t'|'\r'|'\n') { skip(); };
    // \n pula para a proxima linha
    // \r move o cursor para o comeco da linha
    // \t insere um tab


CADEIA_NAO_FECHADA:  ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* '\n'; // uma cadeia nao fechada eh aquela que abre as aspas
           // simples ou dupla, mas nao as fecha
COMENTARIO_NAO_FECHADO: '{' ~('}' | '\n' | '\r')* ('\r' | '\n'); //um comentario nao fechado eh aquele que abre uma chave, mas nao a fecha
ERRO: .; 
