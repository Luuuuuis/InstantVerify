/*
 * Developed by Luuuuuis on 02.04.19 19:39.
 * Last modified 02.04.19 19:39.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Spigot.Listener;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.Spigot.InstantVerify;
import de.luuuuuis.Spigot.TeamSpeak.TeamSpeak;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Join implements Listener {

    Executor executor = Executors.newSingleThreadExecutor();

    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        TS3ApiAsync apiAsync = TeamSpeak.getApi();

        apiAsync.getClients().onSuccess(clientList ->
                executor.execute(() -> {
                    Client client = clientList.stream().filter(clients -> clients.getIp().equals(p.getAddress().getHostString())).findAny().orElse(null);
                    if (client == null) return;

                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(client.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(TeamSpeak.getServerGroup())) {
                        apiAsync.addClientToServerGroup(TeamSpeak.getServerGroup(), client.getDatabaseId());
                        apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                        .replace("%Name", p.getName())
                                        .replace("%UUID", p.getUniqueId().toString())
                        ));
                    }
                })).onFailure(ex -> System.err.println("InstantVerify >> Could not get players! \n" + ex.getMessage()));

    }
}
