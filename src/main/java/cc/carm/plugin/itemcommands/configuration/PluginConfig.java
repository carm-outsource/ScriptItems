package cc.carm.plugin.itemcommands.configuration;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;

public class PluginConfig {

    public static final ConfigValue<Boolean> DEBUG = new ConfigValue<>(
            "debug", Boolean.class, false
    );

    public static final ConfigValue<Boolean> METRICS = new ConfigValue<>(
            "metrics", Boolean.class, true
    );

    public static final ConfigValue<Boolean> CHECK_UPDATE = new ConfigValue<>(
            "check-update", Boolean.class, true
    );

    public static final ConfigValue<String> STORAGE_METHOD = new ConfigValue<>(
            "storage.method", String.class, "YAML"
    );

}
