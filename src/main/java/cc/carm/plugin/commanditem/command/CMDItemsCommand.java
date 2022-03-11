package cc.carm.plugin.commanditem.command;

import cc.carm.plugin.commanditem.CommandItemAPI;
import cc.carm.plugin.commanditem.item.ItemSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CMDItemsCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemSettings settings = CommandItemAPI.getItemsManager().listItemSettings().values().stream().findFirst().orElse(null);
            if (settings != null) {
                player.getInventory().addItem(settings.generateItem(1));
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        List<String> allCompletes = new ArrayList<>();

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }
}
