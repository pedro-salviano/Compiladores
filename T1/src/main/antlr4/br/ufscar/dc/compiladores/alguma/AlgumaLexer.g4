lexer grammar AlgumaLexer;

PALAVRA_CHAVE:
    'DECLARACOES' | 'ALGORITMO' | 'INTEIRO' | 'REAL' | 'ATRIBUIR' | 'A' | 'LER' 
    | 'IMPRIMIR' | 'SE' | 'ENTAO' | 'ENQUANTO' | 'INICIO' | 'FIM' 
    ;

OpArit	:	'*' | '/' | '+' | '-'
	;

OpRel	:	'<' | '<=' | '>=' | '>' | '=' | '<>'
	;

OpBool  :   'E' | 'OU'
    ;

DELIM	:	':'
	;

AP  :	'('
	;
FP:	')'
	;

VARIAVEL:
    [a-zA-Z][a-zA-Z0-0]*;

DIGITO: ('0'..'9');

fragment
NumI:
    ('+'|'-')? DIGITO+;

NumR:
    ('+'|'-')? DIGITO+ '.' DIGITO+;



Str:
    ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'))* ('\'' | '"');

fragment
ESC_SEQ:
    '\\\'';

COMENTARIO:
    '{' ~('\n'|'\r')* '\r'? '}' '\n'  { skip(); };

WS: (' '|'\t'|'\r'|'\n') { skip(); };


CADEIA_NAO_FECHADA:  ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'))* '\n';
COMENTARIO_NAO_FECHADO: '{' ~('\n'|'\r')* '\r'? '\n' { skip(); };
ERRO: .;