package cc.carm.plugin.scriptitems.conf;


import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;

@ConfigPath(root = true)
public interface PluginMessages extends Configuration {

    ConfiguredMessage<String> USAGE = ConfiguredMessage.asString().defaults(
            "&2&l脚本物品 &f指令帮助",
            "&8#&f give &a<玩家名> &a<脚本ID> &2[数量]",
            "&8-&7 给予指定玩家指定数量的物品。",
            "&8#&f get &a<脚本ID> &2[数量]",
            "&8-&7 给予自己指定数量的物品。",
            "&8#&f apply &a<脚本ID>",
            "&8-&7 为手中的物品直接绑定一个配置。",
            "&8#&f reload",
            "&8-&7 重载配置文件。"
    ).build();

    ConfiguredMessage<String> NO_PERMISSION = ConfiguredMessage.asString()
            .defaults("&c&l抱歉！&f但您没有权限使用该命令。")
            .build();

    ConfiguredMessage<String> COOLDOWN = ConfiguredMessage.asString()
            .defaults("&f您需要等待 &a%(time)秒 &f才可再次使用该物品。")
            .params("time").build();

    ConfiguredMessage<String> ONLY_PLAYER = ConfiguredMessage.asString()
            .defaults("&c抱歉，只有作为玩家时才能使用该指令。").build();

    ConfiguredMessage<String> USE_ITEM = ConfiguredMessage.asString()
            .defaults("&f请手持任意物品后再使用该指令。").build();

    ConfiguredMessage<String> NOT_ONLINE = ConfiguredMessage.asString()
            .defaults("&f玩家 &a%(player) &f并不在线。")
            .params("player").build();

    ConfiguredMessage<String> NOT_EXISTS = ConfiguredMessage.asString()
            .defaults("&f脚本配置 &a%(id) &f并不存在。")
            .params("id").build();

    ConfiguredMessage<String> WRONG_AMOUNT = ConfiguredMessage.asString()
            .defaults("&f请输入正确的数量！")
            .build();

    ConfiguredMessage<String> WRONG_ITEM = ConfiguredMessage.asString()
            .defaults("&f该脚本并未成功配置具体物品，请使用 &a/ScriptItems apply &f来绑定到指定物品上，或在配置文件中正确配置物品。")
            .build();

    ConfiguredMessage<String> GIVEN_ALL = ConfiguredMessage.asString()
            .defaults("&f您成功给予 &2%(player) &f了 &a%(amount) &f个 &a%(name) &f。")
            .params("player", "amount", "name").build();

    ConfiguredMessage<String> GIVEN_SOME = ConfiguredMessage.asString()
            .defaults(
                    "&f您成功给予 &2%(player) &f了 &a%(amount) &f个 &a%(name) &f。",
                    "&f但由于目标玩家背包已满，仍有 &a%(remain) &f个 &a%(name) &f未成功放入背包。"
            ).params("player", "amount", "name", "remain").build();


    ConfiguredMessage<String> APPLIED = ConfiguredMessage.asString()
            .defaults("&f成功为手上的 &2%(type) &f绑定了脚本 &a%(name) &f。")
            .params("type", "name").build();

    interface RESTRICTIONS extends Configuration {

        ConfiguredMessage<String> INVALID = ConfiguredMessage.asString()
                .defaults("&c&l抱歉！&f由于配置的时间限制错误，该物品目前暂不可用。")
                .build();

        ConfiguredMessage<String> NOT_STARTED = ConfiguredMessage.asString()
                .defaults("&f该物品目前还到可使用的时间，请在 &a%(time) &f后使用~")
                .params("time")
                .build();

        ConfiguredMessage<String> EXPIRED = ConfiguredMessage.asString()
                .defaults("&c&l抱歉！&f由于该物品已过最后使用期限，故无法继续使用。")
                .params("time")
                .build();
    }

}
