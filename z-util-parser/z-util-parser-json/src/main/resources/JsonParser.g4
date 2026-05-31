/*
 * JSON Parser G4
 * 对标 RFC 8259 规范
 */
parser grammar JsonParser;

options { tokenVocab=JsonLexer; }

json: value;

value: object | array | string | number | bool | Null;

object: LBrace (pair (Comma pair)*)? RBrace;

pair: string Colon value;

array: LBracket (value (Comma value)*)? RBracket;

string: StringLiteral;

number: Number;

bool: Bool;