package com.zifang.util.json;


import com.zifang.util.json.parser.Parser;
import com.zifang.util.json.tokenizer.CharReader;
import com.zifang.util.json.tokenizer.TokenList;
import com.zifang.util.json.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * JSON 解析器，将 JSON 字符串解析为 Java 对象。
 *
 * @author zifang
 */
public class JSONParser {

    private Tokenizer tokenizer = new Tokenizer();
    private Parser parser = new Parser();

    /**
     * 将 JSON 字符串解析为 Java 对象。
     *
     * @param json JSON 字符串
     * @return 解析后的对象（可能是 {@link com.zifang.util.json.model.JsonObject} 或 {@link com.zifang.util.json.model.JsonArray}）
     * @throws IOException 如果解析过程中发生 I/O 错误或 JSON 格式非法
     */
    public Object fromJSON(String json) throws IOException {
        CharReader charReader = new CharReader(new StringReader(json));
        TokenList tokens = tokenizer.tokenize(charReader);
        return parser.parse(tokens);
    }
}
