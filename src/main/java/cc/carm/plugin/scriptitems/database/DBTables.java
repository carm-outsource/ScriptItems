package cc.carm.plugin.scriptitems.database;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;

public class DBTables {

    /**
     * 物品发放记录表
     * 用于记录每个物品的发放情况，包含发放时间，发放人，发放数量以及发放给了谁。
     */
    public static class GiveTable {

        protected static final ConfigValue<String> TABLE_NAME = new ConfigValue<>(
                "log-storage.database.tables.give", String.class,
                "log_item_give"
        );

        protected static final String[] TABLE_COLUMNS = new String[]{
                "`id` INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE KEY",
                "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY", // ItemUUID
                "`settings` VARCHAR(36) NOT NULL", // 该物品配置对应的Identifier
                "`operator` VARCHAR(36)", "`operator_name` VARCHAR(32)", // 发放人的相关信息
                "`receiver` VARCHAR(36)", "`receiver_name` VARCHAR(32)", // 接受者的相关信息
                "`amount` INT UNSIGNED NOT NULL DEFAULT 1", // 同uuid物品的发放数量
                "`time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" // 发放时间
        };

    }

    /**
     * 物品拿取记录表
     * 改表用于记录物品的使用情况，即谁在什么时候使用了哪个物品，以及领取时任务的执行情况。
     * 请注意：只有在发生物品拿取(即 take action )事件时才会记录！
     */
    public static class TakeTable {

        protected static final ConfigValue<String> TABLE_NAME = new ConfigValue<>(
                "log-storage.database.tables.received", String.class,
                "log_item_received"
        );

        protected static final String[] TABLE_COLUMNS = new String[]{
                "`id` INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY",
                "`uuid` VARCHAR(36) NOT NULL", // ItemUUID
                "`receiver` VARCHAR(36)", "`receiver_name` VARCHAR(32)", // 接受者的相关信息
                "`result` TINYINT(2) NOT NULL DEFAULT 0",// 领取结果
                "`time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP",
                "INDEX `item`(`uuid`)"
        };

    }


}
