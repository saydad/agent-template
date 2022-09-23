package com.self.plugins.web;

import com.self.plugins.AbstractPluginDefine;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class ServletPluginDefine extends AbstractPluginDefine {

    @Override
    public DynamicType.Builder<?> enhance(DynamicType.Builder<?> builder, ClassLoader classLoader) {
        return builder
                // 增强实现
                .visit(Advice.to(ServletInterceptor.class)
                        // 增强的方法
                        .on(ElementMatchers.nameMatches("doDispatch")));
    }

    @Override
    public ElementMatcher<? super TypeDescription> getMatcher() {
        return ElementMatchers.nameMatches("org.springframework.web.servlet.DispatcherServlet");
    }
}
