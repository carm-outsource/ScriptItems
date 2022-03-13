package cc.carm.plugin.commanditem.command;

import cc.carm.plugin.commanditem.CommandItemAPI;
import cc.carm.plugin.commanditem.item.ItemSettings;
import me.saiintbrisson.minecraft.command.argument.TypeAdapter;

public class ItemSettingsAdapter implements TypeAdapter<ItemSettings> {

    @Override
    public ItemSettings convert(String s) {
        return CommandItemAPI.getItemsManager().getItemSettings(s);
    }

}
