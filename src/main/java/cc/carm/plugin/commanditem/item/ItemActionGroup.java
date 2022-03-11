package cc.carm.plugin.commanditem.item;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemActionGroup {


    List<ItemAction> actions;

    public ItemActionGroup(List<ItemAction> actions) {
        this.actions = actions;
    }

    public void execute(Player player) {
        actions.forEach(action -> action.execute(player));
    }

    public static @NotNull ItemActionGroup read(@NotNull List<String> actionsString) {
        List<ItemAction> actions = actionsString.stream()
                .map(ItemAction::read).filter(Objects::nonNull).collect(Collectors.toList());
        return new ItemActionGroup(actions);
    }

}
