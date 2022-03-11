package cc.carm.plugin.commanditem.item;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemAction {

    @NotNull ItemActionType type;
    @Nullable String actionContent;

    public ItemAction(@NotNull ItemActionType type, @Nullable String actionContent) {
        this.type = type;
        this.actionContent = actionContent;
    }

    public @NotNull ItemActionType getType() {
        return type;
    }

    public @Nullable String getActionContent() {
        return actionContent;
    }

    public boolean execute(Player player) {
        return getType().execute(player, getActionContent());
    }

    public static @Nullable ItemAction read(@Nullable String actionString) {
        if (actionString == null) return null;
        int prefixStart = actionString.indexOf("[");
        int prefixEnd = actionString.indexOf("]");
        if (prefixStart < 0 || prefixEnd < 0) return null;

        String prefix = actionString.substring(prefixStart + 1, prefixEnd);
        ItemActionType actionType = ItemActionType.read(prefix);
        if (actionType == null) return null;

        return new ItemAction(actionType, actionString.substring(prefixEnd + 1).trim());
    }


}
