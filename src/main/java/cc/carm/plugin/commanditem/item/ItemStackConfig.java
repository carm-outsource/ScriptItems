package cc.carm.plugin.commanditem.item;

import cc.carm.lib.easyplugin.utils.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public class ItemStackConfig {

    protected @Nullable ItemStack original;

    protected @Nullable Material material;
    protected @Nullable String displayName;
    protected @Nullable List<String> lore;

    public ItemStackConfig() {
    }

    public ItemStackConfig(@Nullable Material material, @Nullable String displayName, @Nullable List<String> lore) {
        this(null, material, displayName, lore);
    }

    public ItemStackConfig(@Nullable ItemStack original) {
        this(original, null, null, null);
    }

    public ItemStackConfig(@Nullable ItemStack original,
                           @Nullable Material material, @Nullable String displayName, @Nullable List<String> lore) {
        this.original = original;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    public @Nullable ItemStack getItemStack(int amount) {
        if (amount <= 0) return null;
        if (original != null) return original.clone();
        if (material == null) return null;
        ItemStackFactory factory = new ItemStackFactory(material, amount);
        if (displayName != null) factory.setDisplayName(displayName);
        if (lore != null && !lore.isEmpty()) factory.setLore(lore);
        return factory.toItemStack();
    }

    public @Nullable ItemStack getItemStack() {
        return getItemStack(1);
    }

    public @Nullable ItemStack getOriginal() {
        return original;
    }

    public ItemStackConfig setOriginal(@Nullable ItemStack original) {
        this.original = original;
        return this;
    }

    public @Nullable Material getMaterial() {
        return material;
    }

    public ItemStackConfig setMaterial(@Nullable Material material) {
        this.material = material;
        return this;
    }

    public @Nullable String getDisplayName() {
        return displayName;
    }

    public ItemStackConfig setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
        return this;
    }

    public @Nullable List<String> getLore() {
        return lore;
    }

    public ItemStackConfig setLore(@Nullable List<String> lore) {
        this.lore = lore;
        return this;
    }

    public static @Nullable ItemStackConfig read(@Nullable ConfigurationSection section) {
        if (section == null) return null;
        ItemStackConfig config = new ItemStackConfig();
        if (section.contains("original") && section.isItemStack("original")) {
            config.setOriginal(section.getItemStack("original"));
        }
        Optional.ofNullable(section.getString("material")).map(Material::matchMaterial).ifPresent(config::setMaterial);
        Optional.ofNullable(section.getString("displayName")).ifPresent(config::setDisplayName);
        Optional.of(section.getStringList("lore")).filter(l -> !l.isEmpty()).ifPresent(config::setLore);
        return config;
    }


}
