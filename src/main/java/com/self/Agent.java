package com.self;

import com.self.plugins.AbstractPluginDefine;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

/**
 * 入口
 */
@Slf4j
public class Agent {

    public static void agentmain(String arg, Instrumentation instrumentation) {
        try {
            // 获取ClassPool
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get("com.daily.web.TestController");
            // 获取sayHelloFinal方法
            CtMethod ctMethod = ctClass.getDeclaredMethod("say");
            // 方法前后进行增强
            ctMethod.addLocalVariable("startTime", CtClass.longType);
            ctMethod.insertBefore("{long startTime = System.currentTimeMillis();}");
            ctMethod.insertAfter("{ System.out.println($proceed + \"agent耗时日志: \" + (System.currentTimeMillis() - startTime) + \"ms\"); }");

            // 重载class
            Class c = Class.forName("com.daily.web.TestController");
            instrumentation.redefineClasses(new ClassDefinition(c, ctClass.toBytecode()));
            instrumentation.retransformClasses(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void premain(String arg, Instrumentation instrumentation) {
        // 加载插件文件
        PluginLoader pluginLoader = new PluginLoader();
        boolean loadRes = pluginLoader.loadPluginProperties();
        if (!loadRes) {
            System.out.println("load plugin fail");
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
