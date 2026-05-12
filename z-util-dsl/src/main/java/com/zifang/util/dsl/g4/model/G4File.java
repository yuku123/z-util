package com.zifang.util.dsl.g4.model;

/**
 * G4文件模型
 */
public class G4File {
    
    private String grammarName;
    private String lexerGrammarName;
    private String parserGrammarName;
    
    public G4File() {
    }
    
    public String getGrammarName() {
        return grammarName;
    }
    
    public void setGrammarName(String grammarName) {
        this.grammarName = grammarName;
    }
    
    public String getLexerGrammarName() {
        return lexerGrammarName;
    }
    
    public void setLexerGrammarName(String lexerGrammarName) {
        this.lexerGrammarName = lexerGrammarName;
    }
    
    public String getParserGrammarName() {
        return parserGrammarName;
    }
    
    public void setParserGrammarName(String parserGrammarName) {
        this.parserGrammarName = parserGrammarName;
    }
}