package cc.carm.plugin.commanditem.configuration;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;
import cc.carm.lib.easyplugin.configuration.values.ConfigValueMap;

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

    public static final ConfigValue<Boolean> LOG_STORAGE = new ConfigValue<>(
            "log-storage.enable", Boolean.class, true
    );

    public static class CustomStorage {

        public static ConfigValue<Boolean> ENABLE = new ConfigValue<>("custom-storage.enable", Boolean.class, false);

        public static ConfigValue<String> PATH = new ConfigValue<>("custom-storage.path", String.class, "items/");

    }


}
