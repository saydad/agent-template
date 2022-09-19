package com.self.plugins.demo;

import net.bytebuddy.asm.Advice;

public class DemoInterceptor {

    @Advice.OnMethodEnter
    public static void start() {
        System.out.println("start");
    }

    @Advice.OnMethodExit
    public static void end() {
        System.out.println("end");
    }
}
