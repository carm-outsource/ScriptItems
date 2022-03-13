package cc.carm.plugin.scriptitems.item;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.easyplugin.utils.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnusedReturnValue")
public class ItemStackConfig {

    protected @Nullable ItemStack item;

    public ItemStackConfig() {
    }

    public ItemStackConfig(@Nullable Material material, @Nullable String displayName, @Nullable List<String> lore) {
        if (material == null) {
            this.item = null;
            return;
        }

        ItemStackFactory factory = new ItemStackFactory(material);
        if (displayName != null) factory.setDisplayName(ColorParser.parse(displayName));
        if (lore != null && !lore.isEmpty()) factory.setLore(lore);
        this.item = factory.toItemStack();
    }

    public ItemStackConfig(@Nullable ItemStack item) {
        this.item = item;
    }

    public @Nullable ItemStack getItemStack(int amount) {
        if (amount <= 0 || item == null) return null;
        ItemStack item = this.item.clone();
        item.setAmount(amount);
        return item;
    }

    public @Nullable ItemStack getItemStack() {
        return getItemStack(1);
    }

    public static @Nullable ItemStackConfig read(@Nullable ConfigurationSection section) {
        if (section == null) return null;
        return new ItemStackConfig(
                Optional.ofNullable(section.getString("type")).map(Material::matchMaterial).orElse(null),
                section.getString("name"),
                section.getStringList("lore")
        );
    }

    public static @Nullable ItemStackConfig create(@Nullable ItemStack item) {
        if (item == null) return null;
        return new ItemStackConfig(item);
    }


}
