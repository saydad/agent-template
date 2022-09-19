package byteBuddy;

import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class ByteBuddyAdvice {

    // 方法签名随意
    public void doIt(@SuperCall Callable<?> callable) throws Exception {
        System.out.println("start");
        callable.call();
        System.out.println("end");
    }

}
