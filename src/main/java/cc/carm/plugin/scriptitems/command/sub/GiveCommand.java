package cc.carm.plugin.scriptitems.command.sub;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import cc.carm.plugin.scriptitems.command.MainCommand;
import cc.carm.plugin.scriptitems.conf.PluginMessages;
import cc.carm.plugin.scriptitems.item.ScriptConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class GiveCommand extends SubCommand<MainCommand> {

    public GiveCommand(@NotNull MainCommand parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length < 2) return getParent().noArgs(sender);

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            PluginMessages.NOT_ONLINE.sendTo(sender, args[0]);
            return null;
        }

        ScriptConfiguration settings = ScriptItemsAPI.getItemsManager().getItemSettings(args[1]);
        if (settings == null) {
            PluginMessages.NOT_EXISTS.sendTo(sender, args[1]);
            return null;
        }

        int amount = 1;
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (Exception ignored) {
                amount = -1;
            }
        }

        if (amount < 1) {
            PluginMessages.WRONG_AMOUNT.sendTo(sender);
            return null;
        }

        ItemStack item = settings.generateItem(amount);
        if (item == null) {
            PluginMessages.WRONG_ITEM.sendTo(sender);
            return null;
        }

        HashMap<Integer, ItemStack> remain = player.getInventory().addItem(item);
        if (remain.isEmpty()) {
            PluginMessages.GIVEN_ALL.sendTo(sender, player.getName(), amount, settings.getName());
        } else {
            int remainAmount = remain.values().stream().mapToInt(ItemStack::getAmount).sum();
            PluginMessages.GIVEN_SOME.sendTo(sender, player.getName(), amount - remainAmount, settings.getName(), remainAmount);
        }

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.allPlayers(args[0]);
        } else if (args.length == 2) {
            return SimpleCompleter.objects(args[1], ScriptItemsAPI.getItemsManager().listItemSettings().keySet());
        } else return SimpleCompleter.none();
    }

}
