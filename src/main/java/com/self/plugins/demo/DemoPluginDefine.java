package com.self.plugins.demo;

import com.self.plugins.AbstractPluginDefine;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class DemoPluginDefine extends AbstractPluginDefine {

    @Override
    public ElementMatcher<? super TypeDescription> getMatcher() {
        return ElementMatchers.named("asm.Test");
    }

    @Override
    public DynamicType.Builder<?> enhance(DynamicType.Builder<?> builder, ClassLoader classLoader) {
        return builder
                // 增强实现
                .visit(Advice.to(DemoInterceptor.class)
                // 增强的方法
                .on(ElementMatchers.nameMatches("say")));
    }
}
