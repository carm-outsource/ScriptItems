package cc.carm.plugin.commanditem.configuration;


import cc.carm.lib.easyplugin.configuration.language.EasyMessageList;
import cc.carm.lib.easyplugin.configuration.language.MessagesRoot;

public class PluginMessages extends MessagesRoot {


    public final static EasyMessageList COOLDOWN = EasyMessageList.builder()
            .contents("&f您需要等待 &c%(time)秒 &f才可再次使用指令物品。")
            .params("time").build();

    public static class Restrictions {

        public final static EasyMessageList INVALID = EasyMessageList.builder()
                .contents("&c&l抱歉！&f由于配置的时间限制错误，该物品目前暂不可用。")
                .build();

        public final static EasyMessageList NOT_STARTED = EasyMessageList.builder()
                .contents("&f该物品目前还到可使用的时间，请在 &c%(time) &f后使用~")
                .params("time")
                .build();

        public final static EasyMessageList EXPIRED = EasyMessageList.builder()
                .contents("&c&l抱歉！&f由于该物品已过最后使用期限，故无法继续使用。")
                .params("time")
                .build();
    }

}
