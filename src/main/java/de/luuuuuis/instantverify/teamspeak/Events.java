/*
 * Developed by Luuuuuis on 04.05.19 23:14.
 * Last modified 04.05.19 23:09.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.event.*;
import de.luuuuuis.instantverify.InstantVerify;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class Events {

    Events(InstantVerify instantVerify) {

        TS3ApiAsync apiAsync = instantVerify.getTeamSpeak().getApi();

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
                    if (!groups.contains(instantVerify.getTeamSpeak().getServerGroup())) {
                        Executor executor = Executors.newSingleThreadExecutor();

                        Collection<ProxiedPlayer> playerList = ProxyServer.getInstance().getPlayers();

                        executor.execute(() -> {
                            ProxiedPlayer player = playerList.stream().filter(players -> players.getAddress().getHostString().equals(clientInfo.getIp())).findFirst().orElse(null);
                            if (player == null) return;

                            apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), clientInfo.getDatabaseId());
                            apiAsync.editDatabaseClient(clientInfo.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", player.getName())
                                            .replace("%UUID", player.getUniqueId().toString())
                            ));
                            instantVerify.getDbManager().getVerifyPlayer().update(player.getUniqueId(), clientInfo.getUniqueIdentifier(), null, null);

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
