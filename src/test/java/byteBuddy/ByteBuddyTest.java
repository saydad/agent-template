package byteBuddy;

import asm.Test;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Class<? extends Test> testClass = new ByteBuddy()
                // 指定父类
                .subclass(Test.class)
                // 代理的方法
                .method(ElementMatchers.nameMatches("say"))
                // 代理类
                .intercept(MethodDelegation.to(new ByteBuddyAdvice()))
                // 生成字节码
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded();

        testClass.newInstance().say();
    }
}
