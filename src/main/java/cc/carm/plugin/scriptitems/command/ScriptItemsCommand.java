package cc.carm.plugin.scriptitems.command;

import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import cc.carm.plugin.scriptitems.configuration.PluginMessages;
import cc.carm.plugin.scriptitems.item.ScriptConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ScriptItemsCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String alias, @NotNull String[] args) {
        if (args.length >= 1) {
            String aim = args[0];
            if (aim.equalsIgnoreCase("reload")) {
                try {
                    ScriptItemsAPI.getConfigManager().reload();
                    sender.sendMessage("配置文件重载完成！");
                } catch (Exception e) {
                    sender.sendMessage("配置文件重载失败！");
                    e.printStackTrace();
                }
                return true;
            } else if (aim.equalsIgnoreCase("apply")) {
                if (args.length < 2) {
                    PluginMessages.USAGE.send(sender);
                    return true;
                }

                if (!(sender instanceof Player)) {
                    PluginMessages.ONLY_PLAYER.send(sender);
                    return true;
                }

                ScriptConfiguration settings = ScriptItemsAPI.getItemsManager().getItemSettings(args[1]);
                if (settings == null) {
                    PluginMessages.NOT_EXISTS.send(sender, args[1]);
                    return true;
                }

                Player player = (Player) sender;
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    PluginMessages.USE_ITEM.send(sender);
                    return true;
                }

                player.getInventory().setItemInMainHand(settings.applyItem(item.clone()));
                PluginMessages.APPLIED.send(sender, item.getType().name(), settings.getName());

                return true;
            } else if (aim.equalsIgnoreCase("give")) {

                if (args.length < 3) {
                    PluginMessages.USAGE.send(sender);
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    PluginMessages.NOT_ONLINE.send(sender, args[1]);
                    return true;
                }

                ScriptConfiguration settings = ScriptItemsAPI.getItemsManager().getItemSettings(args[2]);
                if (settings == null) {
                    PluginMessages.NOT_EXISTS.send(sender, args[2]);
                    return true;
                }

                int amount = 1;
                if (args.length >= 4) {
                    try {
                        amount = Integer.parseInt(args[3]);
                    } catch (Exception ignored) {
                        amount = -1;
                    }
                }
                if (amount < 1) {
                    PluginMessages.WRONG_AMOUNT.send(sender);
                    return true;
                }

                ItemStack item = settings.generateItem(amount);
                if (item == null) {
                    PluginMessages.WRONG_ITEM.send(sender);
                    return true;
                }

                HashMap<Integer, ItemStack> remain = player.getInventory().addItem(item);
                if (remain.isEmpty()) {
                    PluginMessages.GIVEN_ALL.send(sender, player.getName(), amount, settings.getName());
                } else {
                    int remainAmount = remain.values().stream().mapToInt(ItemStack::getAmount).sum();
                    PluginMessages.GIVEN_SOME.send(sender, player.getName(), amount - remainAmount, settings.getName(), remainAmount);
                }

                return true;
            } else {
                PluginMessages.USAGE.send(sender);
                return true;
            }
        } else {
            PluginMessages.USAGE.send(sender);
            return true;
        }
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {
        List<String> allCompletes = new ArrayList<>();
        switch (args.length) {
            case 1: {
                allCompletes.add("help");
                allCompletes.add("give");
                if (sender instanceof Player) allCompletes.add("apply");
                allCompletes.add("reload");
                break;
            }
            case 2: {
                String aim = args[0];
                if (aim.equalsIgnoreCase("give")) {
                    allCompletes = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                } else if (aim.equalsIgnoreCase("apply")) {
                    allCompletes = new ArrayList<>(ScriptItemsAPI.getItemsManager().listItemSettings().keySet());
                }
                break;
            }
            case 3: {
                String aim = args[0];
                if (aim.equalsIgnoreCase("give")) {
                    allCompletes = new ArrayList<>(ScriptItemsAPI.getItemsManager().listItemSettings().keySet());
                }
                break;
            }
        }

        return allCompletes.stream()
                .filter(s -> StringUtil.startsWithIgnoreCase(s, args[args.length - 1]))
                .limit(10).collect(Collectors.toList());
    }

}
