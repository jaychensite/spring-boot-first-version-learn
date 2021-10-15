package foo;

import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;
import org.springframework.instrument.InstrumentationSavingAgent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class InstrumentationLoadTimeWeaver {

    private static final boolean AGENT_CLASS_PRESENT = isPresent(
            "org.springframework.instrument.InstrumentationSavingAgent",
            InstrumentationLoadTimeWeaver.class.getClassLoader());

    public static void init() {
        addTransformer(new AspectJClassBypassingClassFileTransformer(new ClassPreProcessorAgentAdapter()));
    }


    public static void addTransformer(ClassFileTransformer transformer) {
        Instrumentation instrumentation = getInstrumentation();
        if (instrumentation != null) {
            instrumentation.addTransformer(transformer);
        }
    }


    private static Instrumentation getInstrumentation() {
        if (AGENT_CLASS_PRESENT) {
            return InstrumentationAccessor.getInstrumentation();
        }
        else {
            return null;
        }
    }

    /**
     * Inner class to avoid InstrumentationSavingAgent dependency.
     */
    private static class InstrumentationAccessor {

        public static Instrumentation getInstrumentation() {
            return InstrumentationSavingAgent.getInstrumentation();
        }
    }

    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        }
        catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }
}
