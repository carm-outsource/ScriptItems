package cc.carm.plugin.scriptitems;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.updatechecker.GHUpdateChecker;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.plugin.scriptitems.command.MainCommand;
import cc.carm.plugin.scriptitems.conf.PluginConfig;
import cc.carm.plugin.scriptitems.conf.PluginMessages;
import cc.carm.plugin.scriptitems.listener.ItemListener;
import cc.carm.plugin.scriptitems.listener.ProtectListener;
import cc.carm.plugin.scriptitems.manager.ItemsManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;

public class Main extends EasyPlugin {

    private static Main instance;

    public Main() {
        instance = this;
    }

    protected ConfigurationHolder<?> configProvider;
    protected ConfigurationHolder<?> messageProvider;

    protected ItemsManager itemsManager;

    @Override
    protected boolean initialize() {

        log("加载插件配置文件...");
        this.configProvider = MineConfiguration.from(this, "config.yml");
        this.configProvider.initialize(PluginConfig.class);

        this.messageProvider = MineConfiguration.from(this, "messages.yml");
        this.messageProvider.initialize(PluginMessages.class);


        info("加载物品配置...");
        this.itemsManager = new ItemsManager();
        this.itemsManager.initialize();

        info("注册指令...");
        registerCommand("ScriptItems".toLowerCase(), new MainCommand(this));

        info("注册监听器...");
        registerListener(new ItemListener());
        registerListener(new ProtectListener(this));

        if (PluginConfig.METRICS.getNotNull()) {
            info("启用统计数据...");
            new Metrics(this, 14615);
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull() && !isFolia()) {
            log("开始检查更新...");
            getScheduler().runAsync(GHUpdateChecker.runner(this));
        } else {
            log("已禁用检查更新，跳过。");
        }

        return true;
    }

    @Override
    protected void shutdown() {

        log("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public static Main getInstance() {
        return instance;
    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

    public ConfigurationHolder<?> getConfigProvider() {
        return configProvider;
    }

    public ConfigurationHolder<?> getMessageProvider() {
        return messageProvider;
    }

    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
