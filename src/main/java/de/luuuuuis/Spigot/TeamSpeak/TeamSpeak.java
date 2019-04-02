/*
 * Developed by Luuuuuis on 02.04.19 19:39.
 * Last modified 02.04.19 19:29.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Spigot.TeamSpeak;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3Exception;
import de.luuuuuis.Spigot.InstantVerify;

public class TeamSpeak {

    private static TS3ApiAsync api;
    private static TS3Query query;
    private static Integer serverGroup;

    public TeamSpeak() {

        try {
            Thread thread = new Thread(() -> {
                TS3Config config = new TS3Config();
                query = new TS3Query(config);
                api = query.getAsyncApi();
                serverGroup = Integer.parseInt(InstantVerify.serverConfig.getTeamSpeakCredentials().get("ServerGroup").toString());

                config.setHost(InstantVerify.serverConfig.getTeamSpeakCredentials().get("Host").toString());
                query.connect();
                api.login(InstantVerify.serverConfig.getTeamSpeakCredentials().get("username").toString(), InstantVerify.serverConfig.getTeamSpeakCredentials().get("password").toString());
                api.selectVirtualServerById(Integer.parseInt(InstantVerify.serverConfig.getTeamSpeakCredentials().get("VirtualServerId").toString()));
                api.setNickname(InstantVerify.serverConfig.getTeamSpeakCredentials().get("Nickname").toString());

                System.out.println("InstantVerify >> Successfully connected to TeamSpeak");

                if (InstantVerify.serverConfig.getTeamSpeakCredentials().get("Instant").equals(true)) {
                    assert api != null;
                    new Events(api, serverGroup);
                }
            });
            thread.start();
        } catch (TS3Exception ex) {
            System.err.println("InstantVerify >> Cannot connect to the TeamSpeak server! Check your config to make sure you are using the correct credentials.");
            query.exit();
            api.logout();
            api = null;
        }


    }

    public static TS3ApiAsync getApi() {
        return api;
    }

    public static TS3Query getQuery() {
        return query;
    }

    public static Integer getServerGroup() {
        return serverGroup;
    }
}
