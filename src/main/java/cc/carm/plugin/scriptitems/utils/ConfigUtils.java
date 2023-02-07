package cc.carm.plugin.scriptitems.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConfigUtils {

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
