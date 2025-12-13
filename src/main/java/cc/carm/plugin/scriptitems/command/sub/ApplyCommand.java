package cc.carm.plugin.scriptitems.command.sub;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import cc.carm.plugin.scriptitems.command.MainCommand;
import cc.carm.plugin.scriptitems.conf.PluginMessages;
import cc.carm.plugin.scriptitems.item.ScriptConfiguration;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ApplyCommand extends SubCommand<MainCommand> {

    public ApplyCommand(@NotNull MainCommand parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (!(sender instanceof Player)) return getParent().onlyPlay(sender);
        if (args.length < 1) return getParent().noArgs(sender);

        ScriptConfiguration settings = ScriptItemsAPI.getItemsManager().getItemSettings(args[0]);
        if (settings == null) {
            PluginMessages.NOT_EXISTS.sendTo(sender, args[1]);
            return null;
        }

        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            PluginMessages.USE_ITEM.sendTo(sender);
            return null;
        }

        ItemStack after = settings.applyItem(item.clone());
        player.getInventory().setItemInMainHand(after);
        PluginMessages.APPLIED.sendTo(sender, item.getType().name(), settings.getName());
        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.objects(args[0], ScriptItemsAPI.getItemsManager().listItemSettings().keySet());
        } else return SimpleCompleter.none();
    }

}
