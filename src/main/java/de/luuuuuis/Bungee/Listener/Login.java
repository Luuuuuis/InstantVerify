/*
 * Developed by Luuuuuis on 07.04.19 11:43.
 * Last modified 07.04.19 11:41.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.Listener;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.Bungee.Events.VerifyEvent;
import de.luuuuuis.Bungee.InstantVerify;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Login implements Listener {

    private Executor executor = Executors.newSingleThreadExecutor();

    @EventHandler
    public void onJoin(LoginEvent e) {
        e.registerIntent(InstantVerify.getInstance());

        TS3ApiAsync apiAsync = TeamSpeak.getApi();

        apiAsync.getClients().onSuccess(clientList ->
                executor.execute(() -> {
                    Client client = clientList.stream().filter(clients -> clients.getIp().equals(e.getConnection().getAddress().getHostString())).findAny().orElse(null);
                    if (client == null) return;

                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(client.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(TeamSpeak.getServerGroup())) {
                        VerifyEvent verifyEvent = new VerifyEvent();
                        ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                        if (!verifyEvent.isCancelled()) {
                            apiAsync.addClientToServerGroup(TeamSpeak.getServerGroup(), client.getDatabaseId());
                            apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", e.getConnection().getName())
                                            .replace("%UUID", e.getConnection().getUniqueId().toString())
                            ));
                        }
                    }
                })).onFailure(ex -> System.err.println("InstantVerify >> Could not get players! \n" + ex.getMessage()));

        e.completeIntent(InstantVerify.getInstance());

    }
}
