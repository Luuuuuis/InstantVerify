package de.luuuuuis.Bungee.Minecraft;

import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.Bungee.InstantVerify;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.Minecraft
 * Date: 02.01.2019
 * Time 18:24
 */
public class VerifyCommand extends Command {

    public VerifyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("InstantVerify >> Command only usable as player");
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (args.length != 1) {
            p.sendMessage(InstantVerify.prefix + "/verify <TeamSpeak Name/Unique ID>");
        } else {
            Client client;
            if (args[0].endsWith("=") && args[0].length() == 28) {
                client = TeamSpeak.getApi().getClientByUId(args[0]);
            } else {
                client = TeamSpeak.getApi().getClientByNameExact(args[0], true);
            }
            if (client == null) {
                p.sendMessage(InstantVerify.prefix + "You have to be connected to the TeamSpeak");
                return;
            }
            List<Integer> groups = new ArrayList<>();
            Arrays.stream(client.getServerGroups()).forEach(groups::add);
            if (!groups.contains(TeamSpeak.getServerGroup())) {
                TeamSpeak.getApi().addClientToServerGroup(TeamSpeak.getServerGroup(), client.getDatabaseId());
                TeamSpeak.getApi().editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION, "Minecraft Name: " + p.getName()));
            }
        }

    }
}
