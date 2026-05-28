/*
 * JSON Lexer G4
 * 对应 ANTLR4 JSON Lexer 规则
 */
lexer grammar JsonLexer;

// 空白字符（跳过）
Space:  [ \t\r\n]+ -> channel(HIDDEN);

// JSON 值
Null:     'null';
True:     'true';
False:    'false';

// 数字（整数 + 小数 + 科学计数法）
Number: '-'? [0-9]+ ('.' [0-9]+)? ([eE] [+-]? [0-9]+)?;

// 字符串
String: '"' (([\u0020-\u0021\u0023-\u005b\u005d-\uFFFF] | '\\' [tnrbf"\\/] | '\\u' [0-9a-fA-F]{4})*) '"';

// 符号
LCurly:  '{';
RCurly:  '}';
LBracket: '[';
RBracket: ']';
Colon:   ':';
Comma:   ',';