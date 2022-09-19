package javaAssist;

import asm.Test;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class JavaassistTest {
    public static void main(String[] args) throws Exception {
        // 获取ClassPool
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("asm.Test");
        // 获取sayHelloFinal方法
        CtMethod ctMethod = ctClass.getDeclaredMethod("say");
        // 方法前后进行增强
        ctMethod.insertBefore("{ System.out.println(\"start\");}");
        ctMethod.insertAfter("{ System.out.println(\"end\"); }");
        // CtClass对应的字节码加载到JVM里
        Class c = ctClass.toClass();
        //反射生成增强后的类
        Test aopDemoServiceWithoutInterface = (Test) c.newInstance();
        aopDemoServiceWithoutInterface.say();
    }
}
