package cc.carm.plugin.commanditem.command;

import cc.carm.plugin.commanditem.CommandItemAPI;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.executor.CompleterExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ItemSettingsCompleter implements CompleterExecutor<CommandSender> {

    @Override
    public List<String> execute(Context<CommandSender> context) {
        return new ArrayList<>(CommandItemAPI.getItemsManager().listItemSettings().keySet());
    }

}
