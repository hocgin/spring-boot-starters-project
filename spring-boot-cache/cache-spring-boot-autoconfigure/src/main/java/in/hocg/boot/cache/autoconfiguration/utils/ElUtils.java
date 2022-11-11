package in.hocg.boot.cache.autoconfiguration.utils;

import lombok.experimental.UtilityClass;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@UtilityClass
public class ElUtils {

    public static <T> T parseSpEl(String text, String[] parameterNames, Object[] args) {
        // 创建解析器
        ExpressionParser parser = new SpelExpressionParser();

        // 构造上下文
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0, len = parameterNames.length; i < len; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return (T) parser.parseExpression(text).getValue(context);
    }
}
