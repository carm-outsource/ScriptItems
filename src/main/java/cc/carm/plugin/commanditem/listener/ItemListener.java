package cc.carm.plugin.commanditem.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemListener implements Listener {


    /**
     * 监听玩家点击，并执行物品对应的操作。
     *
     * @param event 玩家点击事件
     */
    @EventHandler
    public void onClick(PlayerInteractEvent event) {

    }

    /**
     * 监听玩家合成，阻止玩家将指令物品合成浪费掉。
     *
     * @param event 合成事件
     */
    @EventHandler
    public void onCraft(CraftItemEvent event) {

    }

    /**
     * 阻止非玩家捡起指令物品
     *
     * @param event 捡起事件
     */
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {

    }


}
