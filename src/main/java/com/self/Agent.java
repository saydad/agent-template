package com.self;

import com.self.plugins.AbstractPluginDefine;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * 入口
 */
@Slf4j
public class Agent {

    public static void premain(String arg, Instrumentation instrumentation) {
        // 加载插件文件
        PluginLoader pluginLoader = new PluginLoader();
        boolean loadRes = pluginLoader.loadPluginProperties();
        if (!loadRes) {
            return;
        }

        // 构建所有插件的插入点为chain
        ElementMatcher.Junction<Object> focusedType = pluginLoader.buildFocusedTypeChain();

        // 使用byte-buddy构建agent,并加载所有插件
        new AgentBuilder.Default()
                // 增强类型过滤
                .type(focusedType)
                // 增强点执行入口
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
                        // 查找对应插件instance
                        AbstractPluginDefine pluginDefine = pluginLoader.find(typeDescription);
                        if (pluginDefine == null) {
                            return builder;
                        }
                        // 加载增强的class
                        return pluginDefine.enhance(builder, classLoader);
                    }})
                .installOn(instrumentation);
    }
}
