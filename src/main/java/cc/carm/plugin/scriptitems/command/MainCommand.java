package cc.carm.plugin.scriptitems.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.scriptitems.command.sub.ApplyCommand;
import cc.carm.plugin.scriptitems.command.sub.GetCommand;
import cc.carm.plugin.scriptitems.command.sub.GiveCommand;
import cc.carm.plugin.scriptitems.command.sub.ReloadCommand;
import cc.carm.plugin.scriptitems.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MainCommand extends CommandHandler {
    public MainCommand(@NotNull JavaPlugin plugin) {
        super(plugin);
        registerSubCommand(new ReloadCommand(this, "reload"));
        registerSubCommand(new ApplyCommand(this, "apply"));
        registerSubCommand(new GetCommand(this, "get"));
        registerSubCommand(new GiveCommand(this, "give"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        PluginMessages.USAGE.send(sender);
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        PluginMessages.NO_PERMISSION.send(sender);
        return null;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("ScriptItems.admin");
    }

    public Void onlyPlay(CommandSender sender) {
        PluginMessages.ONLY_PLAYER.send(sender);
        return null;
    }
}
