/*
 * Developed by Luuuuuis on 23.04.19 16:47.
 * Last modified 23.04.19 16:46.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.database;

import de.luuuuuis.instantverify.InstantVerify;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfo {

    private String tsid, discordid, email;

    private PlayerInfo(ResultSet rs) throws SQLException {
        tsid = rs.getString("TSID");
        discordid = rs.getString("DISCORDID");
        email = rs.getString("EMAIL");
    }

    public static PlayerInfo getPlayerInfo(String uuid, InstantVerify instantVerify) {

        try (ResultSet rs = instantVerify.getDbManager().getResult("SELECT * FROM verify WHERE UUID='" + uuid + "'")) {
            if (rs.next()) {
                return new PlayerInfo(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    String getTsid() {
        return tsid;
    }

    public String getDiscordid() {
        return discordid;
    }

    String getEmail() {
        return email;
    }
}
