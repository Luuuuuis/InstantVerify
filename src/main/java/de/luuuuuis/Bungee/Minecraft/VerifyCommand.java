package de.luuuuuis.Bungee.Minecraft;

import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.Bungee.Discord.Discord;
import de.luuuuuis.Bungee.InstantVerify;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

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

    public static HashMap<String, String> verifying = new HashMap<>();

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("InstantVerify >> Command only usable as player");
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (args.length != 1) {
            p.sendMessage(InstantVerify.prefix + "/verify <TeamSpeak Name/TeamSpeak Unique ID/Discord ID>");
        } else {
            if (args[0].length() == 18) {
                User user = Discord.getJda().getUserById(args[0]);
                if (user == null) {
                    p.sendMessage(InstantVerify.prefix + "Please connect to our Discord and check your Discord ID");
                    return;
                }
                user.openPrivateChannel().queue(channel -> {
                    channel.sendMessage("Hi " + user.getAsMention() + ",\nPlease send me your Minecraft name to verify that you own the Minecraft account." +
                            "\nIf it isn't your account just close the chat.").queue();
                    verifying.put(user.getName(), p.getName());
                });
                return;
            }
            Client client;
            if (args[0].endsWith("=") && args[0].length() == 28) {
                client = TeamSpeak.getApi().getClientByUId(args[0]);
            } else {
                client = TeamSpeak.getApi().getClientByNameExact(args[0], false);
            }
            if (client == null) {
                p.sendMessage(InstantVerify.prefix + "Please connect to our TeamSpeak and check your Unique ID");
                return;
            }
            List<Integer> groups = new ArrayList<>();
            Arrays.stream(client.getServerGroups()).forEach(groups::add);
            if (!groups.contains(TeamSpeak.getServerGroup())) {
                TeamSpeak.getApi().addClientToServerGroup(TeamSpeak.getServerGroup(), client.getDatabaseId());
                TeamSpeak.getApi().editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION, "Minecraft Name: " + p.getName()));
                p.sendMessage(InstantVerify.prefix + "Thank you for verifying");
            } else {
                p.sendMessage(InstantVerify.prefix + "You are already verified");
            }
        }

    }
}

//269857055563710465 268086491438252033 261824524973113344 530081371217985537
