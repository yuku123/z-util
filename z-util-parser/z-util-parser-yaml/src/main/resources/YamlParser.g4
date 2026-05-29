/*
 * YAML Parser G4
 * 对标 YAML 1.2 规范的核心结构
 * 使用 DynamicParser 加载并运行
 */
parser grammar YamlParser;

options { tokenVocab=YamlLexer; }

// ==================== 顶层 ====================

yaml
    : document* EOF
    ;

document
    : DocStart? directive* blockNode DocEnd?
    | directive* blockNode
    ;

directive
    : Directive plainScalar
    ;

// ==================== 节点 ====================

blockNode
    : props? blockContent
    ;

blockContent
    : blockMap
    | blockSeq
    | blockScalar
    ;

// ==================== 属性（锚点、标签） ====================

props
    : anchor? tag?
    | tag? anchor?
    ;

anchor
    : AnchorName
    ;

tag
    : NonSpecificTag
    | VerbatimTag
    ;

// ==================== 块映射（缩进驱动） ====================

blockMap
    : (mapPair EOL?)+
    ;

mapPair
    : (indent keyNode blockValue?)
    | (indent blockKey blockValue?)
    ;

blockKey
    : plainScalar
    | DqString
    | SqString
    ;

blockValue
    : EOL indent blockContent
    |
    ;

keyNode
    : plainScalar
    | DqString
    | SqString
    | AnchorName
    ;

// ==================== 块序列 ====================

blockSeq
    : (seqEntry EOL?)+
    ;

seqEntry
    : indent Dash (Space+ blockContent | Space* EOL indent blockContent)
    ;

// ==================== 块标量（Literal/Folded） ====================

blockScalar
    : BlockScalarStart BlockIndent? EOL blockContent
    ;

// ==================== 流式（行内）结构 ====================

flowNode
    : flowMap
    | flowSeq
    | flowScalar
    ;

flowSeq
    : LBracket (flowNode (Comma flowNode)*)? RBracket
    ;

flowMap
    : LBrace (flowPair (Comma flowPair)*)? RBrace
    ;

flowPair
    : flowNode Colon flowNode
    ;

flowScalar
    : DqString
    | SqString
    | Number
    | Bool
    | Null
    | AnchorName
    | AliasName
    ;