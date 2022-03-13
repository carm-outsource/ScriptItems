package cc.carm.plugin.commanditem.item;

import cc.carm.lib.easysql.api.util.TimeDateUtils;
import cc.carm.plugin.commanditem.Main;
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
        Main.debugging("ItemRestrictions: " + startTime + " -> " + endTime);
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

        AVAILABLE,
        INVALID,
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
