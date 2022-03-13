package cc.carm.plugin.scriptitems.manager;

import cc.carm.lib.easyplugin.configuration.file.FileConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesConfig;
import cc.carm.lib.easyplugin.configuration.language.MessagesInitializer;
import cc.carm.plugin.scriptitems.configuration.PluginMessages;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConfigManager {

    private final JavaPlugin plugin;

    private FileConfig config;
    private MessagesConfig messageConfig;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean initConfig() {
        try {
            this.config = new FileConfig(plugin);
            this.messageConfig = new MessagesConfig(plugin);

            FileConfig.pluginConfiguration = () -> config;
            FileConfig.messageConfiguration = () -> messageConfig;

            MessagesInitializer.initialize(messageConfig, PluginMessages.class);

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public FileConfig getPluginConfig() {
        return config;
    }

    public FileConfig getMessageConfig() {
        return messageConfig;
    }

    public void reload() throws Exception {
        getPluginConfig().reload();
        getMessageConfig().reload();
    }

    public void saveConfig() throws Exception {
        getPluginConfig().save();
        getMessageConfig().save();
    }

    public static <V> Map<String, V> readStringMap(@Nullable ConfigurationSection section,
                                                   @NotNull Function<String, V> valueCast) {
        if (section == null) return new LinkedHashMap<>();
        Map<String, V> result = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            V finalValue = valueCast.apply(section.getString(key));
            if (finalValue != null) result.put(key, finalValue);
        }
        return result;
    }

    public static <V> Map<String, V> readSectionMap(@Nullable ConfigurationSection section,
                                                    @NotNull Function<ConfigurationSection, V> valueCast) {
        if (section == null) return new LinkedHashMap<>();
        Map<String, V> result = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            if (!section.isConfigurationSection(key)) continue;
            V finalValue = valueCast.apply(section.getConfigurationSection(key));
            if (finalValue != null) result.put(key, finalValue);
        }
        return result;
    }


    public static <V> Map<String, V> readListMap(@Nullable ConfigurationSection section,
                                                 @NotNull Function<@NotNull List<String>, V> valueCast) {
        if (section == null) return new LinkedHashMap<>();
        Map<String, V> result = new LinkedHashMap<>();
        for (String key : section.getKeys(false)) {
            if (!section.isList(key)) continue;
            V finalValue = valueCast.apply(section.getStringList(key));
            if (finalValue != null) result.put(key, finalValue);
        }
        return result;
    }


}
