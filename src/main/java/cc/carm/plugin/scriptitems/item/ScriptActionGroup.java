package cc.carm.plugin.scriptitems.item;

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
        actions.forEach(action -> action.execute(player));
    }

    public static @NotNull ScriptActionGroup read(@NotNull List<String> actionsString) {
        List<ScriptAction> actions = actionsString.stream()
                .map(ScriptAction::read).filter(Objects::nonNull).collect(Collectors.toList());
        return new ScriptActionGroup(actions);
    }

}
