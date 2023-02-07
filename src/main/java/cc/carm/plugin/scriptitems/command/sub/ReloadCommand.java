package cc.carm.plugin.scriptitems.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.scriptitems.Main;
import cc.carm.plugin.scriptitems.ScriptItemsAPI;
import cc.carm.plugin.scriptitems.command.MainCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<MainCommand> {

    public ReloadCommand(@NotNull MainCommand parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        try {
            Main.getInstance().getConfigProvider().reload();
            Main.getInstance().getMessageProvider().reload();
            ScriptItemsAPI.getItemsManager().loadItems();
            sender.sendMessage("配置文件重载完成！");
        } catch (Exception e) {
            sender.sendMessage("配置文件重载失败: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
