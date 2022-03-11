package cc.carm.plugin.commanditem.item;

import cc.carm.lib.easyplugin.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public enum ItemActionType {

    /**
     * 以玩家聊天的形式执行
     * 若内容以 “/" 开头，则会以玩家身份执行命令。
     */
    CHAT((player, string) -> {
        if (string == null) return true; //没有需要执行的
        List<String> finalContents = MessageUtils.setPlaceholders(player, Collections.singletonList(string));
        boolean success = true;
        for (String finalContent : finalContents) {
            try {
                player.chat(finalContent);
            } catch (Exception ex) {
                success = false;
            }
        }
        return success;
    }),

    /**
     * 以后台的形式执行指令
     * 指令内容不需要以“/”开头。
     */
    CONSOLE((player, string) -> {
        if (string == null) return true;
        List<String> finalCommands = MessageUtils.setPlaceholders(player, Collections.singletonList(string));
        boolean success = true;
        for (String finalCommand : finalCommands) {
            try {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);
            } catch (Exception ex) {
                success = false;
            }
        }
        return success;
    }),

    /**
     * 向玩家发送消息。
     */
    MESSAGE((sender, messages) -> {
        MessageUtils.send(sender, messages);
        return true;
    }),

    /**
     * 向玩家发送声音。
     * 允许配置音量与音调
     * <ul>
     *   <li>SOUND_NAME</li>
     *   <li>SOUND_NAME:VOLUME</li>
     *   <li>SOUND_NAME:VOLUME:PITCH</li>
     * </ul>
     */
    SOUND((player, string) -> {
        if (string == null) return true;
        try {
            String[] args = string.contains(":") ? string.split(":") : new String[]{string};
            Sound sound = Arrays.stream(Sound.values())
                    .filter(s -> s.name().equals(args[0]))
                    .findFirst().orElse(null);

            if (sound == null) return true;
            float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1F;
            float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1F;

            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (Exception ignored) {
        }
        return true; // 声音放不放无关紧要
    }),

    /**
     * 拿取玩家手上的一个物品
     */
    TAKE((player, string) -> {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            int current = player.getInventory().getItemInMainHand().getAmount();
            player.getInventory().getItemInMainHand().setAmount(current - 1);
            return true;
        }
        return false;
    });

    BiFunction<@NotNull Player, @Nullable String, @NotNull Boolean> executor;

    ItemActionType(BiFunction<@NotNull Player, @Nullable String, @NotNull Boolean> executor) {
        this.executor = executor;
    }

    public BiFunction<Player, String, Boolean> getExecutor() {
        return executor;
    }

    public boolean execute(@NotNull Player player, @Nullable String content) {
        return getExecutor().apply(player, content);
    }

    public static ItemActionType read(String string) {
        return Arrays.stream(ItemActionType.values())
                .filter(action -> action.name().equalsIgnoreCase(string))
                .findFirst().orElse(null);
    }

}