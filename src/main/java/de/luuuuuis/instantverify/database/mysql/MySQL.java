/*
 * Developed by Luuuuuis on 23.04.19 16:47.
 * Last modified 23.04.19 16:46.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.database.mysql;

import de.luuuuuis.instantverify.InstantVerify;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class MySQL {

    private InstantVerify instantVerify;

    public MySQL(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    public void init() {

        HashMap<String, Object> getMySQLCredentials = instantVerify.getServerConfig().getMySQLCredentials();

        String url = "jdbc:mysql://" + getMySQLCredentials.get("Host").toString() + ":" + getMySQLCredentials.get("Port").toString() + "/"
                + getMySQLCredentials.get("database").toString() + "?autoReconnect=true&useUnicode=yes";
        try {

            Connection connection = DriverManager.getConnection(url, getMySQLCredentials.get("User").toString(), getMySQLCredentials.get("Password").toString());

            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
                System.out.println("instantverify mysql >> Connected to " + metaData.getDatabaseProductName());

                instantVerify.getDbManager().setConnection(connection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
