lexer grammar PropertiesLexer;

// 空白字符（跳过）
WS: [ \t\r\n\f]+ -> channel(HIDDEN);
// 注释行（# 或 ! 开头）
COMMENT: ('#' | '!') ~[\r\n]* -> channel(HIDDEN);
// 等号或冒号
SEPARATOR: '=' | ':';
// Key：直到 = 或 : 或空白
KEY: ~[=\r\n\t\f: ]+;
// Value：直到行尾
VALUE: ~[\r\n]*;
