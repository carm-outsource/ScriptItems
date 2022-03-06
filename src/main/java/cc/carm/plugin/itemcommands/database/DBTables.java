package cc.carm.plugin.itemcommands.database;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;

public class DBTables {

    public static class LogTable {

        protected static final ConfigValue<String> TABLE_NAME = new ConfigValue<>(
                "log-storage.database.table-name", String.class,
                "log_item_commands"
        );

        protected static final String[] TABLE_COLUMNS = new String[]{
                "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY COMMENT '用户UUID'", // 用户的UUID
                "`time` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户在线秒数'",// 用户在线时间(秒)
                "`update` DATETIME NOT NULL " +
                        "DEFAULT CURRENT_TIMESTAMP " +
                        "ON UPDATE CURRENT_TIMESTAMP " +
                        " COMMENT '最后更新时间'"
        };

    }


}
