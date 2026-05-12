lexer grammar JsonLexer;

LBrace: '{';
RBrace: '}';
LBracket: '[';
RBracket: ']';
Colon: ':';
Comma: ',';

StringLiteral: '"' (~["\\] | '\\' .)* '"';
Number: '-'? [0-9]+ ('.' [0-9]+)? ([eE] [+-]? [0-9]+)?;
True: 'true';
False: 'false';
Null: 'null';

Whitespace: [ \t\r\n]+ -> channel(HIDDEN);

parser grammar JsonParser;

options { tokenVocab=JsonLexer; }

json: value;

object
    : LBrace (pair (Comma pair)*)? RBrace
    ;

pair: StringLiteral Colon value;

array
    : LBracket (value (Comma value)*)? RBracket
    ;

value
    : object
    | array
    | StringLiteral
    | Number
    | True
    | False
    | Null
    ;
