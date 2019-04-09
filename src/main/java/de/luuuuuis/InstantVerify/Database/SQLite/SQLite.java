/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Database.SQLite;

import de.luuuuuis.InstantVerify.InstantVerify;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite {

    private InstantVerify instantVerify;

    public SQLite(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    public void init() {

        String url = "jdbc:sqlite:" + instantVerify.getDataFolder().getAbsolutePath() + "/" + instantVerify.getServerConfig().getSQLiteCredentials().get("Database").toString() + ".sqlite";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(url);

            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("InstantVerify SQLite >> Connected to " + metaData.getDatabaseProductName());

                instantVerify.getDbManager().setConnection(connection);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
