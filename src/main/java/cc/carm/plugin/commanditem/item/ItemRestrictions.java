package cc.carm.plugin.commanditem.item;

import cc.carm.lib.easysql.api.util.TimeDateUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (startTime > 0 && startTime > System.currentTimeMillis()) return CheckResult.NOT_STARTED;
        if (endTime > 0 && endTime < System.currentTimeMillis()) return CheckResult.EXPIRED;
        return CheckResult.AVAILABLE;
    }

    public enum CheckResult {

        AVAILABLE,
        NOT_STARTED,
        EXPIRED;

    }

    public static @NotNull ItemRestrictions read(@Nullable ConfigurationSection section) {
        if (section == null) return new ItemRestrictions();
        return new ItemRestrictions(
                TimeDateUtils.parseTimeMillis(section.getString("time.start")),
                TimeDateUtils.parseTimeMillis(section.getString("time.end"))
        );
    }
}
