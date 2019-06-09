/*
 * Developed by Luuuuuis on 23.04.19 16:47.
 * Last modified 23.04.19 16:46.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.database.sqlite;

import de.luuuuuis.instantverify.InstantVerify;

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

        String url = "jdbc:sqlite:" + instantVerify.getDataFolder().getAbsolutePath() + "/" + instantVerify.getServerConfig().getSQLiteCredentials().get("database").toString() + ".sqlite";
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(url);

            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
                if (instantVerify.getServerConfig().isDebugMode())
                    System.out.println("InstantVerify SQLite >> Connected to " + metaData.getDatabaseProductName());

                instantVerify.getDbManager().setConnection(connection);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
