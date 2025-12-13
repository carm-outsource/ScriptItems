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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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
            PluginMessages.COOLDOWN.sendTo(player, cooldown.getCooldownSeconds(player));
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


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        cooldown.clear(event.getPlayer());
    }


}
