package com.zifang.util.json;

import com.zifang.util.json.parser.Parser;
import com.zifang.util.json.tokenizer.CharReader;
import com.zifang.util.json.tokenizer.TokenList;
import com.zifang.util.json.tokenizer.Tokenizer;

/**
 * JSON 解析器，将 JSON 字符串解析为 Java 对象。
 *
 * @author zifang
 */
public class JSONParser {

    private final Tokenizer tokenizer = new Tokenizer();
    private final Parser parser = new Parser();

    /**
     * 将 JSON 字符串解析为 Java 对象。
     *
     * @param json JSON 字符串
     * @return 解析后的对象（JsonObject 或 JsonArray）
     */
    public Object fromJSON(String json) {
        CharReader charReader = new CharReader(json);
        TokenList tokens;
        try {
            tokens = tokenizer.tokenize(charReader);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return parser.parse(tokens);
    }
}
