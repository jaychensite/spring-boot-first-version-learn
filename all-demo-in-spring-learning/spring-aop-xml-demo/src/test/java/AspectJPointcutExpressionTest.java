import foo.Perform;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParameter;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.util.HashSet;
import java.util.Set;

public class AspectJPointcutExpressionTest {
    @Test
    public void testExpression() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        PointcutExpression pointcutExpression = buildPointcutExpression(classLoader);
        boolean b = pointcutExpression.couldMatchJoinPointsInType(Perform.class);

        System.out.println(b);
    }
    /**
     * Build the underlying AspectJ pointcut expression.
     */
    private PointcutExpression buildPointcutExpression(ClassLoader classLoader) {
        PointcutParser parser = initializePointcutParser(classLoader);

        return parser.parsePointcutExpression("execution(public * foo.Perform.sing(..))");
    }


    /**
     * Initialize the underlying AspectJ pointcut parser.
     */
    private PointcutParser initializePointcutParser(ClassLoader cl) {
        PointcutParser parser = PointcutParser
                .getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                        SUPPORTED_PRIMITIVES, cl);
        return parser;
    }



    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }
}
