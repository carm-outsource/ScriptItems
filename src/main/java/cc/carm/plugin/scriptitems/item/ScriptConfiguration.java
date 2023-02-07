package cc.carm.plugin.scriptitems.item;

import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import cc.carm.plugin.scriptitems.utils.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class ScriptConfiguration {

    protected final @NotNull String identifier;
    @Nullable String name;

    @Nullable ItemStackConfig item;
    @NotNull ScriptRestrictions restrictions;

    @NotNull Map<String, String> permissions;
    @NotNull Map<String, ScriptActionGroup> actions;

    public ScriptConfiguration(@NotNull String identifier, @Nullable String name,
                               @Nullable ItemStackConfig item, @NotNull ScriptRestrictions restrictions,
                               @NotNull Map<String, String> permissions,
                               @NotNull Map<String, ScriptActionGroup> actions) {
        this.identifier = identifier;
        this.name = name;
        this.item = item;
        this.restrictions = restrictions;
        this.permissions = permissions;
        this.actions = actions;
    }

    public @NotNull String getIdentifier() {
        return identifier;
    }

    public @Nullable String getName() {
        return name;
    }

    public ItemStackConfig getItemConfig() {
        return item;
    }

    public @Nullable ItemStack generateItem(int amount) {
        ItemStackConfig config = getItemConfig();
        ItemStack originalItem = config == null ? null : config.getItemStack(amount);
        if (originalItem == null) return null;
        return applyItem(originalItem);
    }

    public @Nullable ItemStack generateItem() {
        return generateItem(1);
    }

    public @NotNull Map<String, String> getPermissions() {
        return this.permissions;
    }


    public @NotNull ScriptRestrictions getRestrictions() {
        return restrictions;
    }

    public ScriptRestrictions.CheckResult checkRestrictions() {
        return getRestrictions().check();
    }

    public @NotNull Map<String, ScriptActionGroup> getActions() {
        return this.actions;
    }

    public @Nullable ScriptActionGroup getDefaultActions() {
        return getActions().get("default");
    }

    public @Nullable ScriptActionGroup getPlayerActions(@NotNull Player player) {
        @NotNull String actionGroup = getPermissions().entrySet().stream()
                .filter(entry -> player.hasPermission(entry.getValue()))
                .map(Map.Entry::getKey).findFirst().orElse("default");

        return getActions().get(actionGroup);
    }

    public @NotNull ItemStack applyItem(ItemStack originalItem) {
        return ScriptItemsAPI.getItemsManager().applyTag(originalItem, identifier, UUID.randomUUID());
    }

    public static @NotNull ScriptConfiguration load(@NotNull File file) throws Exception {
        return load(YamlConfiguration.loadConfiguration(file));
    }

    public static @NotNull ScriptConfiguration load(@NotNull FileConfiguration config) throws Exception {
        String identifier = config.getString("identifier");
        if (identifier == null) throw new Exception("identifier not provided.");
        return new ScriptConfiguration(
                identifier, config.getString("name"),
                config.isItemStack("item") ?
                        ItemStackConfig.create(config.getItemStack("item")) :
                        ItemStackConfig.read(config.getConfigurationSection("item")),
                ScriptRestrictions.read(config.getConfigurationSection("restrictions")),
                ConfigUtils.readStringMap(config.getConfigurationSection("permissions"), (s -> s)),
                ConfigUtils.readListMap(config.getConfigurationSection("actions"), ScriptActionGroup::read)
        );
    }

}
