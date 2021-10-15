package foo;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class MyTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println("transform()");
        if (className.equals("foo/OriginClass")) {
            ClassPool classPool = ClassPool.getDefault();

            try {
                CtClass class1 = classPool.get(className.replaceAll("/", "."));
                CtMethod ctMethod = class1.getDeclaredMethod("sayHello");
                if (!ctMethod.isEmpty()) {
                    ctMethod.insertBefore("System.out.println(\"before hello!!!\");");
                }
                return class1.toBytecode();
            } catch (NotFoundException | CannotCompileException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}