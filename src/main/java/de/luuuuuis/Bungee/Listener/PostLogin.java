package de.luuuuuis.Bungee.Listener;

import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import de.luuuuuis.Bungee.Events.VerifyEvent;
import de.luuuuuis.Bungee.InstantVerify;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.Listener
 * Date: 16.02.2019
 * Time 20:36
 */
public class PostLogin implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent e) {
        TeamSpeak.getApi().getClients().forEach(clients -> {
            if (clients.getIp().equals(e.getPlayer().getAddress().getHostString())) {
                List<Integer> groups = new ArrayList<>();
                Arrays.stream(clients.getServerGroups()).forEach(groups::add);
                if (!groups.contains(Integer.parseInt(InstantVerify.serverConfig.getTeamSpeakCredentials().get("ServerGroup").toString()))) {
                    VerifyEvent verifyEvent = new VerifyEvent();
                    ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                    if (!verifyEvent.isCancelled()) {
                        TeamSpeak.getApi().addClientToServerGroup(Integer.parseInt(InstantVerify.serverConfig.getTeamSpeakCredentials().get("ServerGroup").toString()), clients.getDatabaseId());
                        TeamSpeak.getApi().editDatabaseClient(clients.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION, "Minecraft Name: " + e.getPlayer().getName()));
                    }
                }
            }
        });
    }
}