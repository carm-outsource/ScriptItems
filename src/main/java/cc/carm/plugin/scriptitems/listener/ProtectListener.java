package cc.carm.plugin.scriptitems.listener;

import cc.carm.lib.easyplugin.listener.EasyListener;
import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class ProtectListener extends EasyListener {

    public ProtectListener(Plugin plugin) {
        super(plugin);

        //监听玩家合成，阻止玩家将指令物品合成浪费掉。
        cancel(CraftItemEvent.class, event -> isScriptItem(event.getInventory().getMatrix()));

        // 阻止铁砧和附魔台对指令物品的操作
        cancel(PrepareItemEnchantEvent.class, event -> isScriptItem(event.getItem()));
        handleEvent(PrepareAnvilEvent.class)
                .filter(e -> isScriptItem(e.getResult()))
                .handle(e -> e.setResult(null));

        // 阻止非玩家捡起指令物品
        handleEvent(EntityPickupItemEvent.class)
                .filter(e -> e.getEntity().getType() != EntityType.PLAYER)
                .filter(e -> isScriptItem(e.getItem().getItemStack()))
                .cancel();

        // 阻止物品被烧掉
        handleEvent(EntityDamageEvent.class)
                .filter(e -> e.getEntity().getType() == EntityType.DROPPED_ITEM)
                .filter(e -> isScriptItem(((Item) e.getEntity()).getItemStack()))
                .cancel();

    }

    public boolean isScriptItem(ItemStack item) {
        return ScriptItemsAPI.getItemsManager().isScriptItem(item);
    }

    public boolean isScriptItem(ItemStack... items) {
        return Arrays.stream(items).anyMatch(this::isScriptItem);
    }

}
