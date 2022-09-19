package com.self.plugins;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * 插件抽象类
 */
public abstract class AbstractPluginDefine {

    public abstract ElementMatcher<? super TypeDescription> getMatcher();

    public DynamicType.Builder<?> enhance(DynamicType.Builder<?> builder, ClassLoader classLoader) {
        return builder;
    }
}
