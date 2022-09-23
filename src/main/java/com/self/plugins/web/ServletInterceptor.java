package com.self.plugins.web;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Argument;

import javax.servlet.http.HttpServletRequest;

public class ServletInterceptor {

    public static final ThreadLocal<Long> START_TIME = ThreadLocal.withInitial(() -> null);

    @Advice.OnMethodEnter
    public static void start() {
        // 记录起始时间
        START_TIME.set(System.currentTimeMillis());

    }

    @Advice.OnMethodExit
    public static void end(@Argument(0) Object obj) {
        // 打印耗时
        HttpServletRequest request = (HttpServletRequest) obj;
        System.out.println("agent日志: " + request.getServletPath() + " 耗时: " + (System.currentTimeMillis() - START_TIME.get()) + "ms");
        START_TIME.remove();
    }

}
