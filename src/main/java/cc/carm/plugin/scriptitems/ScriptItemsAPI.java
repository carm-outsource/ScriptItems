package cc.carm.plugin.scriptitems;

import cc.carm.plugin.scriptitems.manager.ConfigManager;
import cc.carm.plugin.scriptitems.manager.ItemsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class ScriptItemsAPI {

    public static ItemsManager getItemsManager() {
        return Main.getInstance().itemsManager;
    }

    public static ConfigManager getConfigManager() {
        return Main.getInstance().configManager;
    }

    public static boolean hasEmptySlot(Player player) {
        return IntStream.range(0, 36)
                .mapToObj(i -> player.getInventory().getItem(i))
                .anyMatch(i -> i == null || i.getType() == Material.AIR);
    }

}
