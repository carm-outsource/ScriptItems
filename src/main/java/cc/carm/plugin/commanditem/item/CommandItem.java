package cc.carm.plugin.commanditem.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandItem {

    @NotNull UUID uuid;

    @NotNull ItemSettings settings;
    @NotNull ItemStack itemStack;

    public CommandItem(@NotNull UUID uuid, @NotNull ItemSettings settings, @NotNull ItemStack itemStack) {
        this.uuid = uuid;
        this.settings = settings;
        this.itemStack = itemStack;
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }

    public @NotNull ItemSettings getSettings() {
        return settings;
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    
}
