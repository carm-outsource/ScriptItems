package cc.carm.plugin.scriptitems.database;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.plugin.scriptitems.Main;

import java.sql.SQLException;

public class DataManager {
    private SQLManager sqlManager;

    public boolean initialize() {
        try {
            Main.info("	尝试连接到数据库...");
            this.sqlManager = EasySQL.createManager(
                    DatabaseConfig.DRIVER_NAME.getNotNull(), DatabaseConfig.buildJDBC(),
                    DatabaseConfig.USERNAME.getNotNull(), DatabaseConfig.PASSWORD.get()
            );
            this.sqlManager.setDebugMode(() -> Main.getInstance().isDebugging());
        } catch (Exception exception) {
            Main.severe("无法连接到数据库，请检查配置文件。");
            exception.printStackTrace();
            return false;
        }

        try {
            Main.info("	创建插件记录所需表...");
            for (DatabaseTables value : DatabaseTables.values()) {
                value.create(this.sqlManager);
            }
        } catch (SQLException exception) {
            Main.severe("无法创建插件所需的表，请检查数据库权限。");
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public void shutdown() {
        Main.info("	关闭数据库连接...");
        EasySQL.shutdownManager(getSQLManager());
        this.sqlManager = null;
    }

    public SQLManager getSQLManager() {
        return sqlManager;
    }


}

