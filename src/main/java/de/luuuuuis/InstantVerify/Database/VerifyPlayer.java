/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Database;

import de.luuuuuis.InstantVerify.InstantVerify;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class VerifyPlayer {

    private InstantVerify instantVerify;

    VerifyPlayer(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    public void update(UUID uuid, String TSID, String DISCORDID, String EMAIL) {
        if (!instantVerify.getDbManager().isConnected()) return;

        PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(uuid.toString(), instantVerify);

        if (playerInfo != null) {

            try (PreparedStatement preparedStatement = instantVerify.getDbManager().getConnection().prepareStatement("UPDATE verify SET TSID=?, DISCORDID=?, EMAIL=? WHERE UUID=?")) {

                preparedStatement.setString(1, (TSID == null ? playerInfo.getTsid() : TSID));
                preparedStatement.setString(2, (DISCORDID == null ? playerInfo.getDiscordid() : DISCORDID));
                preparedStatement.setString(3, (EMAIL == null ? playerInfo.getEmail() : EMAIL));
                preparedStatement.setString(4, uuid.toString());

                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            //Create new Player

            try (PreparedStatement preparedStatement = instantVerify.getDbManager().getConnection().prepareStatement("INSERT INTO verify (UUID, TSID, DISCORDID, EMAIL) VALUES (?, ?, ?, ?)")) {

                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, TSID);
                preparedStatement.setString(3, DISCORDID);
                preparedStatement.setString(4, EMAIL);

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
