package cc.carm.plugin.commanditem.item;

import cc.carm.plugin.commanditem.CommandItemAPI;
import cc.carm.plugin.commanditem.Main;
import cc.carm.plugin.commanditem.manager.ConfigManager;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class ItemSettings {

    protected final @NotNull String identifier;
    @Nullable String name;

    @Nullable ItemStackConfig item;
    @NotNull ItemRestrictions restrictions;

    @Nullable ItemActionGroup defaultActions;
    @NotNull Map<String, String> permissions;
    @NotNull Map<String, ItemActionGroup> actions;

    public ItemSettings(@NotNull String identifier, @Nullable String name,
                        @Nullable ItemStackConfig item, @NotNull ItemRestrictions restrictions,
                        @Nullable ItemActionGroup defaultActions,
                        @NotNull Map<String, String> permissions,
                        @NotNull Map<String, ItemActionGroup> actions) {
        this.identifier = identifier;
        this.name = name;
        this.item = item;
        this.restrictions = restrictions;
        this.defaultActions = defaultActions;
        this.permissions = permissions;
        this.actions = actions;

        permissions.forEach((key, value) -> Main.debugging("    Permission: " + key + " = " + value));

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

    @Unmodifiable
    public @NotNull Map<String, String> getPermissions() {
        return ImmutableSortedMap.copyOf(permissions);
    }


    public @NotNull ItemRestrictions getRestrictions() {
        return restrictions;
    }

    public ItemRestrictions.CheckResult checkRestrictions() {
        return getRestrictions().check();
    }

    @Unmodifiable
    public @NotNull Map<String, ItemActionGroup> getActions() {
        return ImmutableMap.copyOf(actions);
    }

    public @Nullable ItemActionGroup getDefaultActions() {
        return defaultActions;
    }

    public @Nullable ItemActionGroup getPlayerActions(@NotNull Player player) {
        String actionGroup = getPermissions().entrySet().stream()
                .peek(entry -> Main.debugging("Checking permission: " + entry.getValue()))
                .filter(entry -> player.hasPermission(entry.getValue()))
                .map(Map.Entry::getKey).findFirst().orElse(null);
        return getActions().getOrDefault(actionGroup, getDefaultActions());
    }

    public @NotNull ItemStack applyItem(ItemStack originalItem) {
        return CommandItemAPI.getItemsManager().applyTag(originalItem, identifier, UUID.randomUUID());
    }

    public static @NotNull ItemSettings load(@NotNull File file) throws Exception {
        return load(YamlConfiguration.loadConfiguration(file));
    }

    public static @NotNull ItemSettings load(@NotNull FileConfiguration config) throws Exception {
        String identifier = config.getString("identifier");
        if (identifier == null) throw new Exception("identifier not provided.");
        return new ItemSettings(
                identifier, config.getString("name"),
                ItemStackConfig.read(config.getConfigurationSection("item")),
                ItemRestrictions.read(config.getConfigurationSection("restrictions")),
                ItemActionGroup.read(config.getStringList("actions.default")),
                ConfigManager.readStringMap(config.getConfigurationSection("permissions"), (s -> s)),
                ConfigManager.readListMap(config.getConfigurationSection("actions"), ItemActionGroup::read)
        );
    }

}
