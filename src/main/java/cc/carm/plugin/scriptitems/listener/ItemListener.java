package cc.carm.plugin.scriptitems.listener;

import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import cc.carm.plugin.scriptitems.configuration.PluginConfig;
import cc.carm.plugin.scriptitems.configuration.PluginMessages;
import cc.carm.plugin.scriptitems.item.ScriptItem;
import cc.carm.plugin.scriptitems.item.ScriptActionGroup;
import cc.carm.plugin.scriptitems.item.ScriptRestrictions;
import cc.carm.plugin.scriptitems.item.ScriptConfiguration;
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
        ScriptItem scriptItem = ScriptItemsAPI.getItemsManager().parseItem(item);
        if (scriptItem == null) return;
        event.setCancelled(true); // 阻止事件执行

        Player player = event.getPlayer();
        if (!isClickable(player.getUniqueId())) {
            PluginMessages.COOLDOWN.send(player, getRemainSeconds(player.getUniqueId()));
            return;
        }
        updateTime(player.getUniqueId());

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

    public int getRemainSeconds(UUID uuid) {
        if (!this.clickTime.containsKey(uuid)) return 0;
        if (!PluginConfig.CoolDown.ENABLE.get()) return 0;
        long start = this.clickTime.get(uuid);
        return (int) ((PluginConfig.CoolDown.TIME.get() - (System.currentTimeMillis() - start)) / 1000) + 1;
    }


}
