package dev.mehmet27.rokbot.storage;

import java.sql.Connection;

public class SQLiteCore implements DBCore {
    private Connection connection;

    /*public SQLiteCore() {
        initialize();
    }

    private void initialize() {
        String pluginName = MRank.getInstance().getDescription().getName();
        getSource().setPoolName("mRank-Hikari");
        getSource().setDriverClassName("org.sqlite.JDBC");
        getSource().setJdbcUrl(String.format("jdbc:sqlite:./plugins/%s/%s.db", pluginName, pluginName));
    }*/
}