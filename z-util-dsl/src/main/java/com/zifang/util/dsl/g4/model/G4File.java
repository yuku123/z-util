package com.zifang.util.dsl.g4.model;

/**
 * G4文件模型
 * 表示解析后的.g4文件内容
 */
/**
 * G4File类。
 */
/**
 * G4File类。
 */
public class G4File {
    
    private String grammarName;
    private String lexerGrammarName;
    private String parserGrammarName;
    
    /**
     * 默认构造函数
     */
    /**
     * G4File方法。
     */
    /**
     * G4File方法。
     */
    public G4File() {
    }
    
    /**
     * 获取语法名称
     * @return grammar名称
     */
    /**
     * getGrammarName方法。
     * @return String类型返回值
     */
    /**
     * getGrammarName方法。
     * @return String类型返回值
     */
    public String getGrammarName() {
        return grammarName;
    }
    
    /**
     * 设置语法名称
     * @param grammarName grammar名称
     */
    /**
     * setGrammarName方法。
     *      * @param grammarName String类型参数
     */
    /**
     * setGrammarName方法。
     *      * @param grammarName String类型参数
     */
    public void setGrammarName(String grammarName) {
        this.grammarName = grammarName;
    }
    
    /**
     * 获取词法分析器语法名称
     * @return lexer grammar名称
     */
    /**
     * getLexerGrammarName方法。
     * @return String类型返回值
     */
    /**
     * getLexerGrammarName方法。
     * @return String类型返回值
     */
    public String getLexerGrammarName() {
        return lexerGrammarName;
    }
    
    /**
     * 设置词法分析器语法名称
     * @param lexerGrammarName lexer grammar名称
     */
    /**
     * setLexerGrammarName方法。
     *      * @param lexerGrammarName String类型参数
     */
    /**
     * setLexerGrammarName方法。
     *      * @param lexerGrammarName String类型参数
     */
    public void setLexerGrammarName(String lexerGrammarName) {
        this.lexerGrammarName = lexerGrammarName;
    }
    
    /**
     * 获取语法分析器语法名称
     * @return parser grammar名称
     */
    /**
     * getParserGrammarName方法。
     * @return String类型返回值
     */
    /**
     * getParserGrammarName方法。
     * @return String类型返回值
     */
    public String getParserGrammarName() {
        return parserGrammarName;
    }
    
    /**
     * 设置语法分析器语法名称
     * @param parserGrammarName parser grammar名称
     */
    /**
     * setParserGrammarName方法。
     *      * @param parserGrammarName String类型参数
     */
    /**
     * setParserGrammarName方法。
     *      * @param parserGrammarName String类型参数
     */
    public void setParserGrammarName(String parserGrammarName) {
        this.parserGrammarName = parserGrammarName;
    }
}