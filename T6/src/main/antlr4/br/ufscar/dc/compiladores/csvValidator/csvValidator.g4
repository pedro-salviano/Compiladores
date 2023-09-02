grammar csvValidator;

DEFINE_STRING: 'string' ;

TIPO_BASE:
    'inteiro'   |   'real'  |    'booleano';

REGRA:
    'PK';

NUM_INT_POS:    DIGITO+; //numeros inteiros nao tem virgula

NUM_REAL:   DIGITO+ '.' DIGITO+; //numeros reais tem virgula

fragment
DIGITO: ('0'..'9'); //define que digitos so podem ser compostos por numeros

CHECK: 'verificar';

IDENT:    [a-zA-Z0-9]+; //define os tokens aceitos para um identificador, e define que eles podem conter numeros

DELIM:	':'; //token que denota o fim de um argumento

VIRGULA :   ','; //virgula eh so uma virgula

WS: (' '|'\t'|'\r'|'\n') { skip(); };
    // \n pula para a proxima linha
    // \r move o cursor para o comeco da linha
    // \t insere um tab

AP: '(';
FP: ')';

OPERADOR: '->';

AC: '{';
FC: '}';

CADEIA: ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* ('\'' | '"'); // cadeias devem ser iniciadas por ' ou ", ter o finalizador (ex: \n),
            // e fechar as aspas simples ou dupla para serem validas

CADEIA_NAO_FECHADA:  ('\'' | '"') (ESC_SEQ | ~('\n'|'\''|'\\'|'"'))* '\n'; // uma cadeia nao fechada eh aquela que abre as aspas
           // simples ou dupla, mas nao as fecha


fragment
ESC_SEQ:    '\\\'' | '\\n'; //barra e aspas ou barra n

ERRO: .; 


//  Syntax
script:
    definicao+ execucao;

execucao:
    CHECK AC CADEIA OPERADOR IDENT FC;

definicao:
    IDENT AC corpo FC;

corpo:
    atributo*;

atributo:
    IDENT ':' tipo REGRA?;

tipo:
    literal   |   TIPO_BASE;

literal:
    DEFINE_STRING tamanho;

tamanho: 
    AP NUM_INT_POS FP;


