grammar Sample;

gram : expression* token*;

expression : NODE attr_init? attr_parent? EQUALS declaration;

declaration: declaration_one ( STICK declaration_one )* (STICK declaration_empty)? DOTCOMA;

declaration_one: syntez_attr? chain;

declaration_empty: EMPTY;

token: LEAF syntez_attr? attr_parent? EQUALS PHRASE DOTCOMA;

attr_init: SLASH PHRASE SLASH;

syntez_attr: FIGURE_LPAR PHRASE FIGURE_RPAR;

attr_parent: SQUARE_LPAR PHRASE SQUARE_RPAR;

name : LEAF | NODE;

chain: name+;

PHRASE : '"' (~('"'))+ '"';
NODE : [a-z][a-zA-Z]*;
LEAF : [A-Z]+;

WHITESPACE : [ \t\r\n]+ -> skip;

DOTCOMA : ';';
LPAR : '(';
RPAR : ')';
PLUS : '+';
SLASH : '/';
EQUALS : '=';
STICK : '|';
QUOTE : '\'';
SQUARE_RPAR : ']';
SQUARE_LPAR : '[';
FIGURE_LPAR: '{';
FIGURE_RPAR: '}';
EMPTY: '<>';
