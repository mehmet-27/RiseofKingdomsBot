package dev.mehmet27.rokbot.storage;

import java.sql.Connection;

public class MySQLCore implements DBCore {

    private Connection connection;
    private final String host, database, username, password;
    private final int port;

    public MySQLCore(String host, String database, int port, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
        initialize();
    }

    private void initialize() {
        getLogger().info("Loading storage provider: MySQL");
        getSource().setPoolName("Bot-Hikari");
        getSource().setDriverClassName("com.mysql.cj.jdbc.Driver");
        getSource().setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&characterEncoding=utf-8");
        getSource().setUsername(username);
        getSource().setPassword(password);
    }
}
