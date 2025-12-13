package cc.carm.plugin.scriptitems.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ScriptExecutor {

    void execute(@NotNull Player player, @Nullable String content) throws Exception;

}
