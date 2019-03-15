package de.luuuuuis.Bungee.TeamSpeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.Bungee.Events.VerifyEvent;
import de.luuuuuis.Bungee.InstantVerify;
import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.TeamSpeak
 * Date: 02.01.2019
 * Time 14:48
 */
class Events {

    Events(TS3Api api, Integer serverGroup) {

        api.registerAllEvents();
        api.addTS3Listeners(new TS3Listener() {
            @Override
            public void onTextMessage(TextMessageEvent textMessageEvent) {

            }

            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {
                Client client = api.getClientByUId(clientJoinEvent.getUniqueClientIdentifier());
                List<Integer> groups = new ArrayList<>();
                Arrays.stream(client.getServerGroups()).forEach(groups::add);
                if (!groups.contains(serverGroup)) {
                    ProxyServer.getInstance().getPlayers().forEach(players -> {
                        if (api.getClientInfo(clientJoinEvent.getClientId()).getIp().equals(players.getAddress().getHostString())) {
                            VerifyEvent verifyEvent = new VerifyEvent();
                            ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                            if (!verifyEvent.isCancelled()) {
                                api.addClientToServerGroup(serverGroup, client.getDatabaseId());
                                api.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION, InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                        .replace("%Name", players.getName())
                                        .replace("%UUID", players.getUniqueId().toString())));
                            }
                        }
                    });
                }
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
