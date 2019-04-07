/*
 * Developed by Luuuuuis on 08.04.19 20:33.
 * Last modified 08.04.19 20:30.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.Listener;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.Bungee.Events.VerifyEvent;
import de.luuuuuis.Bungee.InstantVerify;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Login implements Listener {

    private InstantVerify instantVerify;

    public Login(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    @EventHandler
    public void onLogin(LoginEvent e) {
        e.registerIntent(instantVerify);

        TS3ApiAsync apiAsync = instantVerify.getTeamSpeak().getApi();

        apiAsync.getClients().onSuccess(clientList -> {
            Client client = clientList.stream().filter(clients -> clients.getIp().equals(e.getConnection().getAddress().getHostString())).findAny().orElse(null);
            if (client == null) {
                return;
            }

            List<Integer> groups = new ArrayList<>();
            Arrays.stream(client.getServerGroups()).forEach(groups::add);

            if (!groups.contains(instantVerify.getTeamSpeak().getServerGroup())) {
                VerifyEvent verifyEvent = new VerifyEvent();
                ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                if (!verifyEvent.isCancelled()) {
                    apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), client.getDatabaseId());
                    apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                            instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                    .replace("%Name", e.getConnection().getName())
                                    .replace("%UUID", e.getConnection().getUniqueId().toString())
                    ));
                }
            }
        }).onFailure(ex -> System.err.println("InstantVerify >> Could not get players! \n" + ex.getMessage()));

        e.completeIntent(instantVerify);
    }
}
