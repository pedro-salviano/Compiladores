lexer grammar LA;

PALAVRA_CHAVE:
    'algoritmo' | 'declare' | 'literal' | 'inteiro' | 'leia' | 'escreva' | 'fim_algoritmo' 
    | 'real' | '<-' | 'logico' | 'se' | 'entao' | 'senao' | 'fim_se' | 'caso' 
    | 'seja' | '..' | 'fim_caso' | 'para' | 'ate' | 'faca' | 'fim_para' | 'enquanto' | 'fim_enquanto' 
    | '%' | '^' | 'registro' | 'fim_registro' | '.' | 'tipo' | 'procedimento' | 'var' | 'fim_procedimento' 
    | 'funcao' | 'retorne' | 'fim_funcao' | 'constante' | 'falso' | 'verdadeiro';

IDENT:    [a-zA-Z][a-zA-Z0-9_]*;

OP_ARIT	:	'*' | '/' | '+' | '-';

OP_REL	:	'<' | '<=' | '>=' | '>' | '=' | '<>';

OP_COMPR:   'e' | 'ou' | 'nao' | '&';

DELIM:	':';

AP:	'(';

FP:	')';

VIRGULA :   ',';

AC: '[';
FC: ']'; 

fragment
DIGITO: ('0'..'9');

NUM_INT:    ('+'|'-')? DIGITO+;

NUM_REAL:   ('+'|'-')? DIGITO+ '.' DIGITO+;



CADEIA: ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* ('\'' | '"');

fragment
ESC_SEQ:    '\\\'';

COMENTARIO: '{' ~('\n'|'\r')* '\r'? '}' '\n'  { skip(); };

WS: (' '|'\t'|'\r'|'\n') { skip(); };


CADEIA_NAO_FECHADA:  ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* '\n';
COMENTARIO_NAO_FECHADO: '{' ~('\n'|'\r')* '\r'? '\n' { skip(); };
ERRO: .;