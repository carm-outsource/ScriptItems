package cc.carm.plugin.scriptitems.listener;

import cc.carm.lib.easyplugin.utils.EasyCooldown;
import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import cc.carm.plugin.scriptitems.conf.PluginConfig;
import cc.carm.plugin.scriptitems.conf.PluginMessages;
import cc.carm.plugin.scriptitems.item.ScriptActionGroup;
import cc.carm.plugin.scriptitems.item.ScriptConfiguration;
import cc.carm.plugin.scriptitems.item.ScriptItem;
import cc.carm.plugin.scriptitems.item.ScriptRestrictions;
import org.bukkit.entity.Entity;
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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.UUID;

public class ItemListener implements Listener {

    private final EasyCooldown<Player, UUID> cooldown = new EasyCooldown<Player, UUID>(Entity::getUniqueId) {
        @Override
        public long getDuration(@NotNull Player provider) {
            return PluginConfig.COOLDOWN.ENABLE.getNotNull() ? PluginConfig.COOLDOWN.DURATION.getNotNull() : -1;
        }
    };

    /**
     * 监听玩家点击，并执行物品对应的操作。
     *
     * @param event 玩家点击事件
     */
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        ScriptItem scriptItem = ScriptItemsAPI.getItemsManager().parseItem(item);
        if (scriptItem == null) return;
        event.setCancelled(true); // 阻止事件执行

        Player player = event.getPlayer();
        if (cooldown.isCoolingDown(player)) {
            PluginMessages.COOLDOWN.send(player, cooldown.getCooldownSeconds(player));
            return;
        }
        cooldown.updateTime(player);

        ScriptConfiguration settings = scriptItem.getSettings();

        // 检查物品的相关使用限制是否满足要求
        ScriptRestrictions.CheckResult result = settings.getRestrictions().check();
        if (result != ScriptRestrictions.CheckResult.AVAILABLE) {
            result.send(player, settings.getRestrictions()); // 发送提示
            return;
        }

        // 获取玩家的对应操作组
        ScriptActionGroup actions = settings.getPlayerActions(player);
        if (actions == null) return;

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
                .anyMatch(matrix -> ScriptItemsAPI.getItemsManager().isScriptItem(matrix));

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
        if (ScriptItemsAPI.getItemsManager().isScriptItem(item)) {
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
        if (ScriptItemsAPI.getItemsManager().isScriptItem(item)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        cooldown.clear(event.getPlayer());
    }


}
