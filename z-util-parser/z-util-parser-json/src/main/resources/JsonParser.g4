/*
 * JSON Parser G4
 * 对应 ANTLR4 JSON Parser 规则
 */
parser grammar JsonParser;

options { tokenVocab=JsonLexer; }

json
    : value
    ;

value
    : object
    | array
    | String
    | Number
    | Null
    | True
    | False
    ;

object
    : LCurly (pair (Comma pair)*)? RCurly
    ;

pair
    : String Colon value
    ;

array
    : LBracket (value (Comma value)*)? RBracket
    ;