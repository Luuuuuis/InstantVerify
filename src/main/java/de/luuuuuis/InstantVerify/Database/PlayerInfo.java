/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Database;

import de.luuuuuis.InstantVerify.InstantVerify;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerInfo {

    private String uuid, tsid, discordid, email;

    private PlayerInfo(ResultSet rs) throws SQLException {
        uuid = rs.getString("UUID");
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


    public String getUuid() {
        return uuid;
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
