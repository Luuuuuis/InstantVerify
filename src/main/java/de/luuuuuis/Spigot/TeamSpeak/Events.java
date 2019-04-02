/*
 * Developed by Luuuuuis on 02.04.19 19:39.
 * Last modified 02.04.19 19:39.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Spigot.TeamSpeak;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.event.*;
import de.luuuuuis.Spigot.InstantVerify;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Events {

    Events(TS3ApiAsync apiAsync, Integer serverGroup) {

        apiAsync.registerAllEvents();
        apiAsync.addTS3Listeners(new TS3Listener() {
            @Override
            public void onTextMessage(TextMessageEvent textMessageEvent) {

            }

            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {
                apiAsync.getClientByUId(clientJoinEvent.getUniqueClientIdentifier()).onSuccess(clientInfo -> {

                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(clientInfo.getServerGroups()).forEach(groups::add);
                    if (!groups.contains(serverGroup)) {
                        Executor executor = Executors.newSingleThreadExecutor();

                        Collection<Player> playerList = (Collection<Player>) Bukkit.getOnlinePlayers();

                        executor.execute(() -> {
                            Player player = playerList.stream().filter(players -> players.getAddress().getHostString().equals(clientInfo.getIp())).findAny().orElse(null);
                            if (player == null) return;
                            apiAsync.addClientToServerGroup(serverGroup, clientInfo.getDatabaseId());
                            apiAsync.editDatabaseClient(clientInfo.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", player.getName())
                                            .replace("%UUID", player.getUniqueId().toString())
                            ));

                        });

                    }

                });
            }

            @Override
            public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

            }

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {

            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

            }

            @Override
            public void onClientMoved(ClientMovedEvent clientMovedEvent) {

            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

            }
        });

    }
}
