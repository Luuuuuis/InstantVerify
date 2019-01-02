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

    private static TS3Config config;
    private static TS3Query query;
    private static TS3Api api;

    public TeamSpeak(String host, String user, String password, Integer id, String nickname, Integer serverGroup) {

        config = new TS3Config();
        query = new TS3Query(config);
        api = query.getApi();

        config.setHost(host);
        query.connect();
        api.login(user, password);
        api.selectVirtualServerById(id);
        api.setNickname(nickname);

        new Events(api, serverGroup);
    }
}
