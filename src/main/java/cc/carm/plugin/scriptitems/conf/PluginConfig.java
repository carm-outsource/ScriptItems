package cc.carm.plugin.scriptitems.conf;


import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;

@ConfigPath(root = true)
public interface PluginConfig extends Configuration {

    ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComments({
            "统计数据设定",
            "该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            "当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments({
            "物品使用冷却，避免短时间重复使用物品，也避免网络延迟而导致物品被错误使用而对玩家造成的损失。",
            "强烈建议开启，且建议设置为 2000毫秒 以上。(1s = 1000ms)"
    })
    interface COOLDOWN extends Configuration {

        @HeaderComments("是否启用冷却功能")
        ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, true);

        @HeaderComments("冷却的时长，单位为毫秒 1秒 = 1000毫秒 = 20ticks")
        ConfiguredValue<Long> DURATION = ConfiguredValue.of(Long.class, 3000L);

    }


}
