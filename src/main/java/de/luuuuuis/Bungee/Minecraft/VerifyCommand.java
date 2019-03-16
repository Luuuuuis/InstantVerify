/*
 * Developed by Luuuuuis on 16.03.19 19:40.
 * Last modified 16.03.19 19:39.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.Minecraft;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import de.luuuuuis.Bungee.Discord.Discord;
import de.luuuuuis.Bungee.Events.VerifyEvent;
import de.luuuuuis.Bungee.InstantVerify;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class VerifyCommand extends Command {

    public static HashMap<String, String> verifying = new HashMap<>();

    public VerifyCommand(String name) {
        super(name, "", "InstantVerify", "IV");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("InstantVerify >> Command only usable as player");
            return;
        }

        TS3ApiAsync apiAsync = TeamSpeak.getApi();

        ProxiedPlayer p = (ProxiedPlayer) sender;

        if (args.length != 1) {
            String usage = InstantVerify.prefix + "/verify <";
            if (TeamSpeak.getApi() != null) {
                usage += "TeamSpeak Name/TeamSpeak Unique ID";
            }
            if (Discord.getJda() != null) {
                usage += "/Discord ID";
            }
            usage += "/Status>";
            p.sendMessage(usage);
        } else {
            if (args[0].equalsIgnoreCase("author") || args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("status")) {
                p.sendMessage("");
                p.sendMessage(InstantVerify.prefix + "For further questions please contact the author of the plugin.\n");
                if (TeamSpeak.getApi() != null) {
                    p.sendMessage("§aCurrently connected to TeamSpeak");
                } else {
                    p.sendMessage("§4Currently not connected to TeamSpeak");
                }
                if (Discord.getJda() != null) {
                    p.sendMessage("§aCurrently connected to Discord\n");
                } else {
                    p.sendMessage("§4Currently not connected to Discord\n");
                }
                p.sendMessage("§cAuthor: Luuuuuis\nTwitter: @realluuuuuis");
                p.sendMessage("§cGitHub: https://github.com/Luuuuuis/InstantVerify");
                p.sendMessage("§cGitHub Issue: https://github.com/Luuuuuis/InstantVerify/issues");
                p.sendMessage("§cSupport Discord: https://discord.gg/2aSSGcz");
                p.sendMessage("§cVersion: " + InstantVerify.version);
                p.sendMessage("");
                return;
            }
            if (args[0].length() == 18) {
                if (Discord.getJda() == null) {
                    p.sendMessage(InstantVerify.prefix + "We couldn't verify you because there is no connection to Discord");
                    return;
                }
                User user = Discord.getJda().getUserById(args[0]);
                if (user == null) {
                    p.sendMessage(InstantVerify.prefix + "Please join our Discord and check your Discord ID");
                    return;
                }
                verifying.put(user.getName(), p.getName());
                user.openPrivateChannel().queue(channel -> channel.sendMessage("Hi " + user.getAsMention() + ",\nPlease send me your Minecraft name to verify that you own the Minecraft account." +
                        "\nIf it isn't your account just close the chat.").queue());
                p.sendMessage(InstantVerify.prefix + "Our Discord Bot has written to you. Please answer with your Minecraft name.");
                return;
            }
            if (apiAsync == null) {
                p.sendMessage(InstantVerify.prefix + "We couldn't verify you because there is no connection to TeamSpeak");
                return;
            }
            if (args[0].endsWith("=") && args[0].length() == 28) {
                apiAsync.getClientByUId(args[0]).onSuccess(clientInfo -> {
                    if (clientInfo == null) {
                        p.sendMessage(InstantVerify.prefix + "Please connect to our TeamSpeak and check your Unique ID");
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(clientInfo.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(TeamSpeak.getServerGroup())) {
                        if (clientInfo.getIp().equals(p.getAddress().getHostString())) {
                            VerifyEvent verifyEvent = new VerifyEvent();
                            ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                            if (!verifyEvent.isCancelled()) {
                                apiAsync.addClientToServerGroup(TeamSpeak.getServerGroup(), clientInfo.getDatabaseId());
                                apiAsync.editDatabaseClient(clientInfo.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                        InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                                .replace("%Name", p.getName())
                                                .replace("%UUID", p.getUniqueId().toString())
                                ));
                                p.sendMessage(InstantVerify.prefix + "Thank you for verifying");
                            }
                        } else {
                            p.sendMessage(InstantVerify.prefix + "We couldn't verify you because your IP on the Minecraft server is different from the one on TeamSpeak. Are you sure you entered the correct ID?");
                        }
                    } else {
                        p.sendMessage(InstantVerify.prefix + "You are already verified");
                    }
                }).onFailure(ex -> {
                    p.sendMessage(InstantVerify.prefix + "Please connect to our TeamSpeak and check your Unique ID");
                });
            } else {
                apiAsync.getClientByNameExact(args[0], false).onSuccess(client -> {
                    if (client == null) {
                        p.sendMessage(InstantVerify.prefix + "Please connect to our TeamSpeak and check your Unique ID");
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(client.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(TeamSpeak.getServerGroup())) {
                        if (client.getIp().equals(p.getAddress().getHostString())) {
                            VerifyEvent verifyEvent = new VerifyEvent();
                            ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                            if (!verifyEvent.isCancelled()) {
                                apiAsync.addClientToServerGroup(TeamSpeak.getServerGroup(), client.getDatabaseId());
                                apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                        InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                                .replace("%Name", p.getName())
                                                .replace("%UUID", p.getUniqueId().toString())
                                ));
                                p.sendMessage(InstantVerify.prefix + "Thank you for verifying");
                            }
                        } else {
                            p.sendMessage(InstantVerify.prefix + "We couldn't verify you because your IP on the Minecraft server is different from the one on TeamSpeak. Are you sure you entered the correct ID?");
                        }
                    } else {
                        p.sendMessage(InstantVerify.prefix + "You are already verified");
                    }
                });
            }
        }

    }

}