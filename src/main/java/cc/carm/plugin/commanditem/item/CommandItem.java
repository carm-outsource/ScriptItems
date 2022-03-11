package cc.carm.plugin.commanditem.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandItem {

    @NotNull UUID uuid;

    @NotNull ItemSettings configuration;
    @NotNull ItemStack itemStack;

    public CommandItem(@NotNull UUID uuid, @NotNull ItemSettings configuration, @NotNull ItemStack itemStack) {
        this.uuid = uuid;
        this.configuration = configuration;
        this.itemStack = itemStack;
    }

    public @NotNull UUID getUUID() {
        return uuid;
    }

    public @NotNull ItemSettings getConfiguration() {
        return configuration;
    }

    public @NotNull ItemStack getItemStack() {
        return itemStack;
    }

    
}
