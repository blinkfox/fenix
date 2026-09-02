package com.blinkfox.fenix.helper;

import com.blinkfox.fenix.exception.ParseExpressionException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mvel2.MVEL;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import java.util.Map;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MVEL 表达式解析相关的工具类.
 *
 * @author blinkfox on 2019-08-04.
 * @since v1.0.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ParseHelper {

    /**
     * 模板编译缓存，使用 SoftReference 实现内存敏感缓存.
     * 当内存不足时，JVM 会回收软引用，防止内存溢出.
     */
    private static final Map<String, SoftReference<CompiledTemplate>> TEMPLATE_CACHE = new ConcurrentHashMap<>();

    /**
     * 通过 MVEL 来解析表达式的值，该方法如果解析出错也不抛出异常.
     *
     * @param exp 待解析表达式
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回解析后的值
     */
    public static Object parseExpress(String exp, Object context) {
        try {
            return MVEL.eval(exp, context);
        } catch (Exception e) {
            log.error("【Fenix 错误警示】解析表达式出错，表达式为:【{}】.", exp, e);
            return null;
        }
    }

    /**
     * 通过 MVEL 来解析表达式的值，该方法如果解析出错就抛出 {@link ParseExpressionException} 异常.
     *
     * @param exp 待解析表达式
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回解析后的值
     */
    public static Object parseExpressWithException(String exp, Object context) {
        try {
            return MVEL.eval(exp, context);
        } catch (Exception e) {
            throw new ParseExpressionException("【Fenix 异常提示】解析表达式异常，解析出错的表达式为:【" + exp + "】.", e);
        }
    }

    /**
     * 通过 MVEL 来解析模板的值，该方法如果解析出错就抛出 {@link ParseExpressionException} 异常.
     *
     * @param template 待解析表达式
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 返回解析后的字符串结果
     */
    public static String parseTemplate(String template, Object context) {
        try {
            SoftReference<CompiledTemplate> ref = TEMPLATE_CACHE.get(template);
            CompiledTemplate compiledTemplate = ref != null ? ref.get() : null;
            if (compiledTemplate == null) {
                compiledTemplate = TemplateCompiler.compileTemplate(template);
                TEMPLATE_CACHE.put(template, new SoftReference<>(compiledTemplate));
            }
            return (String) TemplateRuntime.execute(compiledTemplate, context);
        } catch (Exception e) {
            throw new ParseExpressionException("【Fenix 异常提示】解析模板异常，解析出错的模板为:【" + template + "】.", e);
        }
    }

    /**
     * 判断 Fenix 标签中 `match` 属性中的表达式是否匹配通过.
     *
     * @param match match表达式
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 布尔值
     */
    public static boolean isMatch(String match, Object context) {
        return StringHelper.isBlank(match) || isTrue(match, context);
    }

    /**
     * 判断 Fenix 标签中 `match` 属性中的表达式是否`不`匹配通过.
     *
     * @param match match表达式
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 布尔值
     */
    public static boolean isNotMatch(String match, Object context) {
        return !isMatch(match, context);
    }

    /**
     * 判断解析的表达式是否为 true.
     *
     * @param exp 表达式
     * @param context 上下文参数（一般是 Bean 或者 map）
     * @return 布尔值
     */
    public static boolean isTrue(String exp, Object context) {
        return Boolean.TRUE.equals(ParseHelper.parseExpressWithException(exp, context));
    }

}
