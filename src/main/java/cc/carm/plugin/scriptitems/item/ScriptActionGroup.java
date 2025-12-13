package cc.carm.plugin.scriptitems.item;

import cc.carm.plugin.scriptitems.Main;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScriptActionGroup {


    List<ScriptAction> actions;

    public ScriptActionGroup(List<ScriptAction> actions) {
        this.actions = actions;
    }

    public void execute(Player player) {
        for (ScriptAction action : actions) { // Take actions first
            if (action.getType() == ScriptActionType.TAKE) {
                action.execute(player);
            }
        }

        Main.execute(() -> {
            for (ScriptAction action : actions) {
                if (action.getType() != ScriptActionType.TAKE) {
                    action.execute(player);
                }
            }
        });

    }

    public static @NotNull ScriptActionGroup read(@NotNull List<String> actionsString) {
        List<ScriptAction> actions = actionsString.stream()
                .map(ScriptAction::read).filter(Objects::nonNull).collect(Collectors.toList());
        return new ScriptActionGroup(actions);
    }

}
