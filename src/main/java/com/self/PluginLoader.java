package com.self;

import com.self.plugins.AbstractPluginDefine;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.BooleanMatcher;
import net.bytebuddy.matcher.ElementMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 插件加载loader
 */
@Slf4j
public class PluginLoader {
    private static final String PLUGIN_FILE_NAME = "/plugin.properties";

    /**
     * 插件定义实例列表
     */
    private List<AbstractPluginDefine> pluginDefines = new ArrayList<>();

    /**
     * load plugin properties and instance
     * @return true - 加载成功
     */
    public boolean loadPluginProperties() {
        Properties properties = new Properties();
        try {
            properties.load(Agent.class.getResourceAsStream(PLUGIN_FILE_NAME));
            if (properties.isEmpty()) {
                log.warn("加载 {} 为空!!", PLUGIN_FILE_NAME);
                return false;
            }

            String className;
            Set<String> keys = properties.stringPropertyNames();
            for (String key : keys) {
                className = (String) properties.get(key);
                pluginDefines.add((AbstractPluginDefine) Class.forName(className).newInstance());
            }
            return true;
        } catch (Exception e) {
            log.error("加载 plugin.properties 失败", e);
            return false;
        }
    }

    /**
     * 将所有插件匹配逻辑构造为一个chain
     */
    public ElementMatcher.Junction<Object> buildFocusedTypeChain() {
        ElementMatcher.Junction<Object> focusedType = BooleanMatcher.of(true);
        for (AbstractPluginDefine pluginDefine : pluginDefines) {
            focusedType.or(pluginDefine.getMatcher());
        }
        return focusedType;
    }

    public AbstractPluginDefine find(TypeDescription typeDescription) {
        for (AbstractPluginDefine pluginDefine : pluginDefines) {
            if (pluginDefine.getMatcher().matches(typeDescription)) {
                return pluginDefine;
            }
        }
        return null;
    }
}
