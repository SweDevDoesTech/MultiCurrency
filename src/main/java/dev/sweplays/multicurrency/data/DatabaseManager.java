package dev.sweplays.multicurrency.data;

import dev.sweplays.multicurrency.MultiCurrency;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class DatabaseManager {

    private final MultiCurrency plugin;
    private String database;
    private String host;
    private String username;
    private String password;
    private String filename;
    private int port;
    private Connection connection;
    private Statement statement;
    private boolean sqlite;

    public DatabaseManager(MultiCurrency plugin, String filename) {
        this.plugin = plugin;

        try {
            if (filename.endsWith(".db")) {
                this.filename = filename;
            } else {
                this.filename = filename + ".db";
            }

            this.sqlite = true;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public void initializeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed() && this.connection.isValid(this.connection.getNetworkTimeout())) {
                return;
            }

            synchronized (this) {
                if (this.sqlite) {
                    File dataFolder = new File(this.plugin.getDataFolder(), this.filename);
                    if (!dataFolder.exists()) {
                        try {
                            dataFolder.createNewFile();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }

                    Class.forName("org.sqlite.JDBC");
                    this.connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
                    this.statement = this.connection.createStatement();
                }
            }

        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            if (this.connection == null || this.connection.isClosed() || !this.connection.isValid(this.connection.getNetworkTimeout())) {
                this.initializeConnection();
            }

            return this.statement.executeQuery(query);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void execute(String query) {
        try {
            if (this.connection == null || this.connection.isClosed() || !this.connection.isValid(this.connection.getNetworkTimeout())) {
                this.initializeConnection();
            }

            this.statement.execute(query);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public PreparedStatement getPreparedStatement(String sql) {
        try {
            if (this.connection == null || this.connection.isClosed() || !this.connection.isValid(this.connection.getNetworkTimeout())) {
                this.initializeConnection();
            }

            return this.connection.prepareStatement(sql);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }
}
