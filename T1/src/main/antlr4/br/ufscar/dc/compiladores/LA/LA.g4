lexer grammar LA;

PALAVRA_CHAVE: //separa palavras chaves que nao podem ser utilizadas para outro proposito
    'algoritmo' | 'declare' | 'literal' | 'inteiro' | 'leia' | 'escreva' | 'fim_algoritmo' 
    | 'real' | '<-' | 'logico' | 'se' | 'entao' | 'senao' | 'fim_se' | 'caso' 
    | 'seja' | '..' | 'fim_caso' | 'para' | 'ate' | 'faca' | 'fim_para' | 'enquanto' | 'fim_enquanto' 
    | '%' | '^' | 'registro' | 'fim_registro' | '.' | 'tipo' | 'procedimento' | 'var' | 'fim_procedimento' 
    | 'funcao' | 'retorne' | 'fim_funcao' | 'constante' | 'falso' | 'verdadeiro';

IDENT:    [a-zA-Z][a-zA-Z0-9_]*; //define os tokens aceitos para um identificador, e define que eles nao podem comecar com numero

OP_ARIT	:	'*' | '/' | '+' | '-'; //define os tokens que denotam operadores artimeticos

OP_REL	:	'<' | '<=' | '>=' | '>' | '=' | '<>'; //define os tokens que denotam operadores relacionais

OP_COMPR:   'e' | 'ou' | 'nao' | '&'; //define os tokens que denotam operadores logicos

DELIM:	':'; //token que denota o fim de um argumento

AP:	'('; //abre parenteses

FP:	')'; //fecha parenteses

VIRGULA :   ',';

AC: '['; //abre colchete
FC: ']'; //fecha colchete

fragment
DIGITO: ('0'..'9'); //define que digitos so podem ser compostos por numeros

NUM_INT:    ('+'|'-')? DIGITO+; //numeros inteiros nao tem virgula

NUM_REAL:   ('+'|'-')? DIGITO+ '.' DIGITO+; //numeros reais tem virgula



CADEIA: ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* ('\'' | '"'); 

fragment
ESC_SEQ:    '\\\'';

COMENTARIO: '{' ~('\n'|'\r')* '\r'? '}' '\n'  { skip(); };

WS: (' '|'\t'|'\r'|'\n') { skip(); };


CADEIA_NAO_FECHADA:  ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* '\n';
COMENTARIO_NAO_FECHADO: '{' ~('\n'|'\r')* '\r'? '\n' { skip(); };
ERRO: .;
