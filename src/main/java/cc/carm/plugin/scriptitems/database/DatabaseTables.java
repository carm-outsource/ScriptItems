package cc.carm.plugin.scriptitems.database;

import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.SQLTable;
import cc.carm.lib.easysql.api.builder.TableCreateBuilder;
import cc.carm.lib.easysql.api.enums.IndexType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.function.Consumer;

public enum DatabaseTables implements SQLTable {


    /**
     * 物品发放记录表
     * 用于记录每个物品的发放情况，包含发放时间，发放人，发放数量以及发放给了谁。
     */
    GIVE(DatabaseConfig.TABLES.GIVEN, (table) -> {
        table.setColumns("`id` INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE KEY",
                "`uuid` VARCHAR(36) NOT NULL PRIMARY KEY", // ItemUUID
                "`settings` VARCHAR(36) NOT NULL", // 该物品配置对应的Identifier
                "`operator` VARCHAR(36)", "`operator_name` VARCHAR(32)", // 发放人的相关信息
                "`receiver` VARCHAR(36)", "`receiver_name` VARCHAR(32)", // 接受者的相关信息
                "`amount` INT UNSIGNED NOT NULL DEFAULT 1", // 同uuid物品的发放数量
                "`time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP" // 发放时间
        );

        table.addAutoIncrementColumn("id", false, true);
        table.addColumn("uuid", "CHAR(36) NOT NULL PRIMARY KEY"); // 该物品的 ItemUUID
        table.addColumn("type", "VARCHAR(36) NOT NULL"); // 该物品的 类型

        // 发放人的相关信息
        table.addColumn("operator_uuid", "CHAR(36)");
        table.addColumn("operator_name", "VARCHAR(32)");

        // 领取者的相关信息
        table.addColumn("receiver_uuid", "CHAR(36)");
        table.addColumn("receiver_name", "VARCHAR(32)");

        // 发放时间
        table.addColumn("time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");
    }),

    /**
     * 物品拿取记录表
     * 改表用于记录物品的使用情况，即谁在什么时候使用了哪个物品，以及领取时任务的执行情况。
     * 请注意：只有在发生物品拿取(即 take action )事件时才会记录！
     */
    HOMES(DatabaseConfig.TABLES.TAKEN, (table) -> {
        table.addAutoIncrementColumn("id", true, true);
        table.addColumn("uuid", "CHAR(36) NOT NULL"); // ItemUUID
        table.addColumn("item", "VARCHAR(36) NOT NULL"); // Item ID

        // 领取者者的相关信息
        table.addColumn("user_uuid", "CHAR(36) NOT NULL");
        table.addColumn("user_name", "VARCHAR(32) NOT NULL");

        // 领取结果 (是否成功)
        table.addColumn("success", "BIT NOT NULL DEFAULT 0");
        table.addColumn("time", "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP");

        table.setIndex(IndexType.UNIQUE_KEY, "uk_item_receive", "uuid");
    });

    private final Consumer<TableCreateBuilder> builder;
    private final ConfiguredValue<String> name;
    private @Nullable SQLManager manager;

    DatabaseTables(ConfiguredValue<String> name,
                   Consumer<TableCreateBuilder> builder) {
        this.name = name;
        this.builder = builder;
    }

    @Override
    public @Nullable SQLManager getSQLManager() {
        return this.manager;
    }

    @Override
    public @NotNull String getTableName() {
        return this.name.getNotNull();
    }

    @Override
    public boolean create(SQLManager sqlManager) throws SQLException {
        if (this.manager == null) this.manager = sqlManager;

        TableCreateBuilder tableBuilder = sqlManager.createTable(getTableName());
        if (builder != null) builder.accept(tableBuilder);
        return tableBuilder.build().executeFunction(l -> l > 0, false);
    }

}
