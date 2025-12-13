package cc.carm.plugin.scriptitems.item;

import cc.carm.plugin.scriptitems.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScriptAction {

    @NotNull ScriptActionType type;
    @Nullable String actionContent;

    public ScriptAction(@NotNull ScriptActionType type, @Nullable String actionContent) {
        this.type = type;
        this.actionContent = actionContent;
    }

    public @NotNull ScriptActionType getType() {
        return type;
    }

    public @Nullable String getActionContent() {
        return actionContent;
    }

    public void execute(Player player) {
        try {
            getType().execute(player, getActionContent());
        } catch (Exception ex) {
            Main.severe("在为玩家执行 脚本动作 时发生错误: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static @Nullable ScriptAction read(@Nullable String actionString) {
        if (actionString == null) return null;
        int prefixStart = actionString.indexOf("[");
        int prefixEnd = actionString.indexOf("]");
        if (prefixStart < 0 || prefixEnd < 0) return null;

        String prefix = actionString.substring(prefixStart + 1, prefixEnd);
        ScriptActionType actionType = ScriptActionType.read(prefix);
        if (actionType == null) return null;

        return new ScriptAction(actionType, actionString.substring(prefixEnd + 1).trim());
    }

}
