package de.luuuuuis.Bungee.TeamSpeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3Exception;
import de.luuuuuis.Bungee.InstantVerify;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.TeamSpeak
 * Date: 02.01.2019
 * Time 14:32
 */
public class TeamSpeak {

    private static TS3Api api;
    private static TS3Query query;
    private static Integer serverGroup;

    public TeamSpeak() {

        try {
            Thread thread = new Thread(() -> {
                TS3Config config = new TS3Config();
                query = new TS3Query(config);
                api = query.getApi();
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

    public static TS3Api getApi() {
        return api;
    }

    public static TS3Query getQuery() {
        return query;
    }

    public static Integer getServerGroup() {
        return serverGroup;
    }
}
