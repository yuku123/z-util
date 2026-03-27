/**
 * cURL 命令解析器包
 *
 * <p>本包提供将 cURL 命令文本解析为 HTTP 标准结构的功能。</p>
 *
 * <h2>主要功能</h2>
 * <ul>
 *     <li>解析 cURL 命令为 {@link com.zifang.util.http.base.pojo.HttpRequestDefinition}</li>
 *     <li>支持各种 cURL 选项：-X, -H, -d, -u, -b, --data, --header 等</li>
 *     <li>将 HttpRequestDefinition 转换回 cURL 命令</li>
 *     <li>支持解析多个 cURL 命令</li>
 * </ul>
 *
 * <h2>快速开始</h2>
 * <pre>
 * // 解析 cURL 命令
 * String curl = "curl -X POST -H 'Content-Type: application/json' -d '{\"name\":\"test\"}' https://api.example.com/users";
 * HttpRequestDefinition definition = CurlParser.parse(curl);
 *
 * // 访问解析结果
 * System.out.println(definition.getHttpRequestLine().getRequestMethod()); // POST
 * System.out.println(definition.getHttpRequestLine().getUrl()); // https://api.example.com/users
 * System.out.println(definition.getHttpRequestHeader().get("Content-Type")); // application/json
 * </pre>
 *
 * <h2>核心类</h2>
 * <ul>
 *     <li>{@link CurlParser} - 基于正则表达式的 cURL 解析器（推荐）</li>
 *     <li>{@link CurlLexer} - 词法分析器</li>
 *     <li>{@link CurlTokenParser} - 基于 Token 的解析器</li>
 *     <li>{@link CurlBuilder} - 将 HttpRequestDefinition 转换为 cURL 命令</li>
 *     <li>{@link CurlParserUtils} - 工具类，提供便捷方法</li>
 *     <li>{@link CurlParserFactory} - 工厂类，统一入口</li>
 * </ul>
 *
 * @author zifang
 * @version 1.0.0
 * @since 1.0.2-SNAPSHOT
 */
package com.zifang.util.http.parser.curl;
