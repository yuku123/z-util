package com.zifang.util.http.dedao;

import com.zifang.util.core.io.FileUtil;
import com.zifang.util.json.JsonUtil;
import com.zifang.util.json.model.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DedaoC {


    public static String base = "/Users/zifang/Downloads/dedao";
    @Test
    public void test() throws IOException {

        int page = 0;
        int pageSize = 100;
        while(true){
            List<Map<String,Object>> books = getBooks(page, pageSize);
            System.out.println("开始获取第"+page+"页数据");
            if(books != null && books.size() > 0){
                page = page +1;
            } else{
                break;
            }

            for(Map<String,Object> book : books){
                try {

                    String aliasId = null;
                    if(book.get("alias_id") == null || String.valueOf(book.get("alias_id")).equals("")){
                        aliasId = ((List)book.get("odob_alias_list")).get(0).toString();
                    } else {
                        aliasId = book.get("alias_id").toString();
                    }


                    // 获得听书的描述信息
                    Map<String,Object> bookDescribeInfo = getBookDescribeInfo(aliasId);

                    String dd_article_token = bookDescribeInfo.get("dd_article_token").toString();
                    String urlEncode = URLEncoder.encode(dd_article_token, "utf-8");
                    Map<String, Object> bookDetail = getBookDetail(urlEncode);

                    String booName = String.valueOf(book.get("name")).split("\\|")[0].replace("《","").replace("》","")
                            .replace("：","")
                            .replace(".","_")
                            .replace("?","？")
                            .replace(" ","")
                            .replace("<","_")
                            .replace(">","_");
                    Map<String,Object> context = new HashMap<>();
                    context.put("book",book);
                    context.put("bookDescribeInfo",bookDescribeInfo);
                    context.put("bookDetail",bookDetail);

                    FileUtil.write(new File(base+"/"+booName+".json"), JsonUtil.toJsonPretty(context),"utf-8");
                    System.out.println("----开始写出+"+booName );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private Map<String, Object> getBookDetail(String urlEncode) throws IOException {
        HttpGet httpGet = new HttpGet("https://www.dedao.cn/pc/odob/pc/audio/article/get?token="+urlEncode+"&is_new=1");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String res = EntityUtils.toString(response.getEntity());
        Map<String,Object> ress = jsonToMap(JsonUtil.parseObject(res));
        return ress;
    }

    private Map<String, Object> getBookDescribeInfo(String aliasId) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("audio_alias_id", aliasId);

        HttpPost httpPost = new HttpPost("https://www.dedao.cn/pc/bauhinia/pc/article/info");
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setEntity(new StringEntity(JsonUtil.toJson(params)));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        @SuppressWarnings("unchecked")
        Map<String,Object> ress = (Map<String, Object>) jsonPathRead(res, "$.c");
        return ress;
    }

    private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();


    private List<Map<String, Object>> getBooks(int page, int pageSize) throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("page_size", pageSize);
        params.put("sort_strategy", "NEW");
        params.put("product_types", "13,1013");


        HttpPost httpPost = new HttpPost("https://www.dedao.cn/pc/label/v2/algo/pc/product/list");
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setEntity(new StringEntity(JsonUtil.toJson(params)));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String res = EntityUtils.toString(response.getEntity());
        List<Map<String,Object>> ress = (List<Map<String, Object>>) jsonPathRead(res, "$.c.product_list");

        return ress;
    }

    /**
     * 简单 JSONPath：仅支持 $.a.b.c 这种点分路径。
     * 替代 fastjson 的 JSONPath.read，仅用于本爬虫。
     */
    @SuppressWarnings("unchecked")
    private static Object jsonPathRead(String json, String path) {
        Object root = JsonUtil.parseObject(json);
        if (path == null || path.isEmpty()) return root;
        String p = path.startsWith("$") ? path.substring(1) : path;
        if (p.startsWith(".")) p = p.substring(1);
        if (p.isEmpty()) return root;
        Object cur = root;
        for (String seg : p.split("\\.")) {
            if (cur == null) return null;
            if (cur instanceof JsonObject) {
                cur = ((JsonObject) cur).get(seg);
            } else if (cur instanceof Map) {
                cur = ((Map<String, Object>) cur).get(seg);
            } else {
                return null;
            }
        }
        return cur;
    }

    /** 把 JsonObject 递归转成 java.util.Map，使调用方 (Map) 转型可用。 */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> jsonToMap(JsonObject obj) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> e : obj.getAllKeyValue()) {
            Object v = e.getValue();
            if (v instanceof JsonObject) v = jsonToMap((JsonObject) v);
            result.put(e.getKey(), v);
        }
        return result;
    }
}
