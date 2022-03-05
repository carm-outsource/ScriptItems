package cc.carm.plugin.itemcommands;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.plugin.itemcommands.configuration.PluginConfig;
import cc.carm.plugin.itemcommands.hooker.GHUpdateChecker;
import cc.carm.plugin.itemcommands.manager.ConfigManager;
import cc.carm.plugin.itemcommands.util.JarResourceUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.Optional;

public class Main extends EasyPlugin {

    private static Main instance;

    public Main() {
        super(new EasyPluginMessageProvider.zh_CN());
        instance = this;
    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public static void regListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getInstance());
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

    public static Main getInstance() {
        return instance;
    }

    @Override
    protected boolean initialize() {

        info("加载配置文件...");
        if (!ConfigManager.initConfig()) {
            severe("配置文件初始化失败，请检查。");
            setEnabled(false);
            return false;
        }

        if (PluginConfig.METRICS.get()) {
            info("启用统计数据...");
            new Metrics(this, 14459);
        }

        if (PluginConfig.CHECK_UPDATE.get()) {
            log("开始检查更新...");
            GHUpdateChecker checker = new GHUpdateChecker(getLogger(), "CarmJos", "ItemCommands");
            getScheduler().runAsync(() -> checker.checkUpdate(getDescription().getVersion()));
        } else {
            log("已禁用检查更新，跳过。");
        }

        return true;
    }

    @Override
    protected void shutdown() {

        info("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.get();
    }

    @Override
    public void outputInfo() {
        Optional.ofNullable(JarResourceUtils.readResource(this.getResource("PLUGIN_INFO"))).ifPresent(this::log);
    }
}
