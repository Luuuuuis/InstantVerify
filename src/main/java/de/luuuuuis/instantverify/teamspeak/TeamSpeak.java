/*
 * Developed by Luuuuuis on 23.04.19 16:47.
 * Last modified 23.04.19 16:46.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import de.luuuuuis.instantverify.InstantVerify;

public class TeamSpeak {

    private TS3ApiAsync api;
    private TS3Query query;
    private Integer serverGroup;

    public TeamSpeak(InstantVerify instantVerify) {

        try {
            Thread thread = new Thread(() -> {
                TS3Config config = new TS3Config();
                query = new TS3Query(config);
                api = query.getAsyncApi();
                serverGroup = Integer.parseInt(instantVerify.getServerConfig().getTeamSpeakCredentials().get("ServerGroup").toString());

                config.setHost(instantVerify.getServerConfig().getTeamSpeakCredentials().get("Host").toString());
                query.connect();
                api.login(instantVerify.getServerConfig().getTeamSpeakCredentials().get("username").toString(), instantVerify.getServerConfig().getTeamSpeakCredentials().get("password").toString());
                api.selectVirtualServerById(Integer.parseInt(instantVerify.getServerConfig().getTeamSpeakCredentials().get("VirtualServerId").toString()));
                api.setNickname(instantVerify.getServerConfig().getTeamSpeakCredentials().get("Nickname").toString());

                System.out.println("instantverify >> Successfully connected to teamspeak");

                if (instantVerify.getServerConfig().getTeamSpeakCredentials().get("Instant").equals(true)) {
                    assert api != null;
                    new Events(instantVerify);
                }
            });
            thread.start();
        } catch (Exception ex) {
            System.err.println("instantverify >> Cannot connect to the teamspeak server! Check your config to make sure you are using the correct credentials.");
            query.exit();
            api.logout();
            api = null;
        }


    }

    public TS3ApiAsync getApi() {
        return api;
    }

    public TS3Query getQuery() {
        return query;
    }

    public Integer getServerGroup() {
        return serverGroup;
    }
}
