/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Database;

import de.luuuuuis.InstantVerify.Database.MySQL.MySQL;
import de.luuuuuis.InstantVerify.Database.SQLite.SQLite;
import de.luuuuuis.InstantVerify.InstantVerify;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private InstantVerify instantVerify;
    private Connection connection;
    private VerifyPlayer verifyPlayer;

    public DBManager(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    public void connect() {
        if (isConnected()) return;

        if (instantVerify.getServerConfig().getSQLiteCredentials().get("active").equals(true)) {

            new SQLite(instantVerify).init();

        } else if (instantVerify.getServerConfig().getMySQLCredentials().get("active").equals(true)) {

            new MySQL(instantVerify).init();

        } else {
            throw new NullPointerException("No active DB");
        }

        // Create Tables
        try {
            connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS verify(UUID VARCHAR(36), TSID VARCHAR(28), DISCORDID VARCHAR(18), EMAIL VARCHAR(100))");
            System.out.println("InstantVerify SQL >> Successfully created all tables");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        verifyPlayer = new VerifyPlayer(instantVerify);
    }


    public void close() {
        if (!isConnected()) return;
        try {
            connection.close();
            System.out.println("InstantVerify SQL >> Successfully closed the connection");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult(String query) {
        if (!isConnected()) return null;

        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public VerifyPlayer getVerifyPlayer() {
        return verifyPlayer;
    }
}
