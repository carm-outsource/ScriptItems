package cc.carm.plugin.commanditem.item;

import cc.carm.lib.easyplugin.configuration.language.EasyMessageList;
import cc.carm.lib.easysql.api.util.TimeDateUtils;
import cc.carm.plugin.commanditem.configuration.PluginMessages;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class ItemRestrictions {

    long startTime;
    long endTime;

    public ItemRestrictions() {
        this(-1, -1);
    }

    public ItemRestrictions(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * @return 限定的开始时间，-1表示不限定。
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return 限定的结束时间，-1表示不限定。
     */
    public long getEndTime() {
        return endTime;
    }

    public CheckResult check() {
        if (getStartTime() < 0 && getEndTime() < 0) return CheckResult.AVAILABLE;
        if (getStartTime() > 0 && getEndTime() > 0 && getStartTime() > getEndTime()) return CheckResult.INVALID;
        if (getStartTime() > 0 && getStartTime() > System.currentTimeMillis()) return CheckResult.NOT_STARTED;
        if (getEndTime() > 0 && getEndTime() < System.currentTimeMillis()) return CheckResult.EXPIRED;
        return CheckResult.AVAILABLE;
    }

    public enum CheckResult {

        AVAILABLE(() -> null, (res) -> null),

        INVALID(() -> PluginMessages.Restrictions.INVALID, (res) -> null),

        NOT_STARTED(
                () -> PluginMessages.Restrictions.NOT_STARTED,
                (res) -> new Object[]{TimeDateUtils.getTimeString(res.getStartTime())}
        ),

        EXPIRED(
                () -> PluginMessages.Restrictions.EXPIRED,
                (res) -> new Object[]{TimeDateUtils.getTimeString(res.getEndTime())}
        );

        Supplier<@Nullable EasyMessageList> message;
        Function<@NotNull ItemRestrictions, Object[]> params;

        CheckResult(@NotNull Supplier<@Nullable EasyMessageList> message,
                    @NotNull Function<@NotNull ItemRestrictions, @Nullable Object[]> params) {
            this.message = message;
            this.params = params;
        }

        public Supplier<EasyMessageList> getMessage() {
            return message;
        }

        public void send(Player player, ItemRestrictions restrictions) {
            Object[] params = this.params.apply(restrictions);
            if (params == null) {
                getMessage().get().send(player);
            } else {
                getMessage().get().send(player, params);
            }
        }

    }

    public static @NotNull ItemRestrictions read(@Nullable ConfigurationSection section) {
        if (section == null) return new ItemRestrictions();
        return new ItemRestrictions(
                TimeDateUtils.parseTimeMillis(section.getString("time.start")),
                TimeDateUtils.parseTimeMillis(section.getString("time.end"))
        );
    }

}
