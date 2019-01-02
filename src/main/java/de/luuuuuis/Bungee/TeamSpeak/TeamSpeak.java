package de.luuuuuis.Bungee.TeamSpeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.TeamSpeak
 * Date: 02.01.2019
 * Time 14:32
 */
public class TeamSpeak {

    private static TS3Api api;
    private static Integer serverGroup;

    public TeamSpeak(String host, String user, String password, String id, String nickname, String serverGroupSt) {

        TS3Config config = new TS3Config();
        TS3Query query = new TS3Query(config);
        api = query.getApi();
        serverGroup = Integer.parseInt(serverGroupSt);

        config.setHost(host);
        query.connect();
        api.login(user, password);
        api.selectVirtualServerById(Integer.parseInt(id));
        api.setNickname(nickname);

        new Events(api, serverGroup);
    }

    public static TS3Api getApi() {
        return api;
    }

    public static Integer getServerGroup() {
        return serverGroup;
    }
}
