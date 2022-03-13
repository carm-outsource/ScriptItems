package cc.carm.plugin.commanditem.item;

import cc.carm.plugin.commanditem.CommandItemAPI;
import cc.carm.plugin.commanditem.manager.ConfigManager;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class ItemSettings {

    protected final @NotNull String identifier;
    @Nullable String name;

    @Nullable ItemStackConfig item;
    @NotNull ItemRestrictions restrictions;

    @NotNull Map<String, String> permissions;
    @NotNull Map<String, ItemActionGroup> actions;

    public ItemSettings(@NotNull String identifier, @Nullable String name,
                        @Nullable ItemStackConfig item, @NotNull ItemRestrictions restrictions,
                        @NotNull Map<String, String> permissions,
                        @NotNull Map<String, ItemActionGroup> actions) {
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


    public @NotNull ItemRestrictions getRestrictions() {
        return restrictions;
    }

    public ItemRestrictions.CheckResult checkRestrictions() {
        return getRestrictions().check();
    }

    public @NotNull Map<String, ItemActionGroup> getActions() {
        return this.actions;
    }

    public @Nullable ItemActionGroup getDefaultActions() {
        return getActions().get("default");
    }

    public @Nullable ItemActionGroup getPlayerActions(@NotNull Player player) {
        @NotNull String actionGroup = getPermissions().entrySet().stream()
                .filter(entry -> player.hasPermission(entry.getValue()))
                .map(Map.Entry::getKey).findFirst().orElse("default");

        return getActions().get(actionGroup);
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
                config.isItemStack("item") ?
                        ItemStackConfig.create(config.getItemStack("item")) :
                        ItemStackConfig.read(config.getConfigurationSection("item")),
                ItemRestrictions.read(config.getConfigurationSection("restrictions")),
                ConfigManager.readStringMap(config.getConfigurationSection("permissions"), (s -> s)),
                ConfigManager.readListMap(config.getConfigurationSection("actions"), ItemActionGroup::read)
        );
    }
}
