/*
 * Developed by Luuuuuis on 23.04.19 16:47.
 * Last modified 23.04.19 16:46.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.database;

import de.luuuuuis.instantverify.InstantVerify;

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

                preparedStatement.setString(1, (TSID == null ? playerInfo.getTsID() : TSID));
                preparedStatement.setString(2, (DISCORDID == null ? playerInfo.getDiscordID() : DISCORDID));
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
