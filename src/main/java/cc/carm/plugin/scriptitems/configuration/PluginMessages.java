package cc.carm.plugin.scriptitems.configuration;


import cc.carm.lib.easyplugin.configuration.language.EasyMessageList;
import cc.carm.lib.easyplugin.configuration.language.MessagesRoot;

public class PluginMessages extends MessagesRoot {

    public static final EasyMessageList USAGE = EasyMessageList.builder().contents(
            "&2&l脚本指令 &f指令帮助",
            "&8#&f give &a<玩家名> &a<脚本ID> &2[数量]",
            "&8-&7 给予指定玩家指定数量的物品。",
            "&8#&f apply &a<脚本ID>",
            "&8-&7 为手中的物品直接绑定一个配置。",
            "&8#&f reload",
            "&8-&7 重载配置文件。"
    ).build();


    public final static EasyMessageList COOLDOWN = EasyMessageList.builder()
            .contents("&f您需要等待 &a%(time)秒 &f才可再次使用该物品。")
            .params("time").build();

    public final static EasyMessageList ONLY_PLAYER = EasyMessageList.builder()
            .contents("&c抱歉，只有作为玩家时才能使用该指令。").build();

    public final static EasyMessageList USE_ITEM = EasyMessageList.builder()
            .contents("&f请手持任意物品后再使用该指令。").build();

    public final static EasyMessageList NOT_ONLINE = EasyMessageList.builder()
            .contents("&f玩家 &a%(player) &f并不在线。")
            .params("player").build();

    public final static EasyMessageList NOT_EXISTS = EasyMessageList.builder()
            .contents("&f脚本配置 &a%(id) &f并不存在。")
            .params("id").build();

    public final static EasyMessageList WRONG_AMOUNT = EasyMessageList.builder()
            .contents("&f请输入正确的数量！")
            .build();

    public final static EasyMessageList WRONG_ITEM = EasyMessageList.builder()
            .contents("&f该脚本并未成功配置具体物品，请使用 &a/ScriptItems apply &f来绑定到指定物品上，或在配置文件中正确配置物品。")
            .build();

    public final static EasyMessageList GIVEN_ALL = EasyMessageList.builder()
            .contents("&f您成功给予 &2%(player) &f了 &a%(amount) &f个 &a%(name) &f。")
            .params("player", "amount", "name").build();

    public final static EasyMessageList GIVEN_SOME = EasyMessageList.builder().contents(
            "&f您成功给予 &2%(player) &f了 &a%(amount) &f个 &a%(name) &f。",
            "&f但由于目标玩家背包已满，仍有 &a%(remain) &f个 &a%(name) &f未成功放入背包。"
    ).params("player", "amount", "name", "remain").build();


    public final static EasyMessageList APPLIED = EasyMessageList.builder()
            .contents("&f成功为手上的 &2%(type) &f绑定了脚本 &a%(name) &f。")
            .params("type", "name").build();

    public static class Restrictions {

        public final static EasyMessageList INVALID = EasyMessageList.builder()
                .contents("&c&l抱歉！&f由于配置的时间限制错误，该物品目前暂不可用。")
                .build();

        public final static EasyMessageList NOT_STARTED = EasyMessageList.builder()
                .contents("&f该物品目前还到可使用的时间，请在 &a%(time) &f后使用~")
                .params("time")
                .build();

        public final static EasyMessageList EXPIRED = EasyMessageList.builder()
                .contents("&c&l抱歉！&f由于该物品已过最后使用期限，故无法继续使用。")
                .params("time")
                .build();
    }

}
