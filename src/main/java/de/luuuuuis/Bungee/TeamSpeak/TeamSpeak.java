package de.luuuuuis.Bungee.TeamSpeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3Exception;

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

    public TeamSpeak(String host, String user, String password, String id, String nickname, String serverGroupSt) {

        try {
            Thread thread = new Thread(() -> {
                TS3Config config = new TS3Config();
                query = new TS3Query(config);
                api = query.getApi();
                serverGroup = Integer.parseInt(serverGroupSt);

                config.setHost(host);
                query.connect();
                api.login(user, password);
                api.selectVirtualServerById(Integer.parseInt(id));
                api.setNickname(nickname);

                System.out.println("InstantVerify >> Successfully connected to TeamSpeak");

                assert api != null;
                new Events(api, serverGroup);
            });
            thread.start();
        } catch (TS3Exception ex) {
            System.err.println("InstantVerify >> Cannot connect to TeamSpeak server at " + host + "! Check your config to make sure you are using the correct credentials.");
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
