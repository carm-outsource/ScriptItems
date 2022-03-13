package cc.carm.plugin.scriptitems.database;

import cc.carm.lib.easyplugin.configuration.values.ConfigValue;

public class DBConfiguration {

    protected static final ConfigValue<String> DRIVER_NAME = new ConfigValue<>(
            "log-storage.database.driver", String.class,
            "com.mysql.cj.jdbc.Driver"
    );

    protected static final ConfigValue<String> HOST = new ConfigValue<>(
            "log-storage.database.host", String.class,
            "127.0.0.1"
    );

    protected static final ConfigValue<Integer> PORT = new ConfigValue<>(
            "log-storage.database.port", Integer.class,
            3306
    );

    protected static final ConfigValue<String> DATABASE = new ConfigValue<>(
            "log-storage.database.database", String.class,
            "minecraft"
    );

    protected static final ConfigValue<String> USERNAME = new ConfigValue<>(
            "log-storage.database.username", String.class,
            "root"
    );

    protected static final ConfigValue<String> PASSWORD = new ConfigValue<>(
            "log-storage.database.password", String.class,
            "password"
    );

    protected static final ConfigValue<String> EXTRA_SETTINGS = new ConfigValue<>(
            "log-storage.database.extra", String.class,
            "?useSSL=false"
    );

    protected static String buildJDBC() {
        return String.format("jdbc:mysql://%s:%s/%s%s",
                HOST.get(), PORT.get(), DATABASE.get(), EXTRA_SETTINGS.get()
        );
    }


}
