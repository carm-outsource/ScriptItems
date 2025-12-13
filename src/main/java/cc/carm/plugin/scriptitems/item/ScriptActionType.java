package cc.carm.plugin.scriptitems.item;

import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.scriptitems.utils.ScriptExecutor;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.base.XModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum ScriptActionType {

    /**
     * 以玩家聊天的形式执行
     * 若内容以 “/" 开头，则会以玩家身份执行命令。
     */
    CHAT((player, string) -> {
        if (string == null) return; //没有需要执行的
        String contents = MessageUtils.setPlaceholders(player, string);
        if (contents == null || contents.isEmpty()) return;
        player.chat(contents);
    }),

    /**
     * 让玩家以OP的身份执行命令
     */
    OP((player, string) -> {
        if (string == null) return;
        String cmd = MessageUtils.setPlaceholders(player, string);
        if (cmd == null || cmd.isEmpty()) return;

        boolean opBefore = player.isOp();
        player.setOp(true);
        player.chat(cmd.startsWith("/") ? cmd : "/" + cmd);
        player.setOp(opBefore);
    }),

    /**
     * 以后台的形式执行指令
     * 指令内容不需要以“/”开头。
     */
    CONSOLE((player, string) -> {
        if (string == null) return;
        String cmd = MessageUtils.setPlaceholders(player, string);
        if (cmd == null || cmd.isEmpty()) return;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.startsWith("/") ? cmd.substring(1) : cmd);
    }),

    /**
     * 向玩家发送消息。
     */
    MESSAGE(MessageUtils::send),

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
        if (string == null) return;
        String[] args = string.contains(":") ? string.split(":") : new String[]{string};
        Sound sound = XSound.of(args[0].toUpperCase()).map(XModule::get).orElse(null);

        if (sound == null) throw new IllegalArgumentException("不存在该声音类型: " + args[0]);
        float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1F;
        float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1F;

        player.playSound(player.getLocation(), sound, volume, pitch);
    }),

    /**
     * 拿取玩家手上的一个物品
     */
    TAKE((player, string) -> {
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            int current = player.getInventory().getItemInMainHand().getAmount();
            player.getInventory().getItemInMainHand().setAmount(current - 1);
        } else {
            throw new IllegalStateException("玩家手上没有物品可供拿取");
        }
    });

    final @NotNull ScriptExecutor executor;

    ScriptActionType(@NotNull ScriptExecutor executor) {
        this.executor = executor;
    }

    public @NotNull ScriptExecutor getExecutor() {
        return executor;
    }

    public void execute(@NotNull Player player, @Nullable String content) throws Exception {
        getExecutor().execute(player, content);
    }

    public static ScriptActionType read(String string) {
        return Arrays.stream(ScriptActionType.values())
                .filter(action -> action.name().equalsIgnoreCase(string))
                .findFirst().orElse(null);
    }

}