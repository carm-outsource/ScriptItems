package cc.carm.plugin.commanditem.listener;

import cc.carm.plugin.commanditem.CommandItemAPI;
import cc.carm.plugin.commanditem.configuration.PluginConfig;
import cc.carm.plugin.commanditem.item.CommandItem;
import cc.carm.plugin.commanditem.item.ItemActionGroup;
import cc.carm.plugin.commanditem.item.ItemRestrictions;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class ItemListener implements Listener {

    private final HashMap<UUID, Long> clickTime = new HashMap<>();

    /**
     * 监听玩家点击，并执行物品对应的操作。
     *
     * @param event 玩家点击事件
     */
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        CommandItem commandItem = CommandItemAPI.getItemsManager().parseCommandItem(item);
        if (commandItem == null) return;
        event.setCancelled(true); // 阻止事件执行

        Player player = event.getPlayer();
        if (!isClickable(player.getUniqueId())) {
            // TODO 给玩家发消息告诉他还在冷却
            return;
        }

        if (commandItem.getConfiguration().checkRestrictions() != ItemRestrictions.CheckResult.AVAILABLE) {
            // TODO 给玩家发消息告诉他还不能用
            return;
        }

        ItemActionGroup actions = commandItem.getConfiguration().getPlayerActions(player);
        if (actions == null) return;

        updateTime(player.getUniqueId());
        actions.execute(player);
    }

    /**
     * 监听玩家合成，阻止玩家将指令物品合成浪费掉。
     *
     * @param event 合成事件
     */
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        boolean shouldCancel = Arrays.stream(event.getInventory().getMatrix())
                .anyMatch(matrix -> CommandItemAPI.getItemsManager().isCommandItem(matrix));

        if (shouldCancel) event.setCancelled(true);
    }

    /**
     * 阻止非玩家捡起指令物品
     *
     * @param event 捡起事件
     */
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER) return;

        ItemStack item = event.getItem().getItemStack();
        if (CommandItemAPI.getItemsManager().isCommandItem(item)) {
            event.setCancelled(true);
        }
    }

    /**
     * 阻止物品被烧掉
     *
     * @param event 伤害事件
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() != EntityType.DROPPED_ITEM) return;
        Item droppedItem = ((org.bukkit.entity.Item) event.getEntity());
        ItemStack item = droppedItem.getItemStack();
        if (CommandItemAPI.getItemsManager().isCommandItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        this.clickTime.remove(event.getPlayer().getUniqueId());
    }

    public void updateTime(UUID uuid) {
        this.clickTime.put(uuid, System.currentTimeMillis());
    }

    public boolean isClickable(UUID uuid) {
        return !PluginConfig.CoolDown.ENABLE.get()
                || !this.clickTime.containsKey(uuid)
                || System.currentTimeMillis() - this.clickTime.get(uuid) > PluginConfig.CoolDown.TIME.get();
    }


}
