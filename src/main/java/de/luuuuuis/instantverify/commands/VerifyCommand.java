/*
 * Developed by Luuuuuis on 04.05.19 23:14.
 * Last modified 04.05.19 23:08.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.commands;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.instantverify.InstantVerify;
import de.luuuuuis.instantverify.database.PlayerInfo;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class VerifyCommand extends Command {

    private InstantVerify instantVerify;

    public VerifyCommand(String name, InstantVerify instantVerify) {
        super(name, "", "instantverify", "IV");
        this.instantVerify = instantVerify;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("instantverify >> Command only usable as player!");
            return;
        }

        ProxiedPlayer p = (ProxiedPlayer) sender;

        TS3ApiAsync apiAsync = instantVerify.getTeamSpeak().getApi();

        if (args.length != 1) {

            if (apiAsync != null) {
                apiAsync.getClients().onSuccess(clientList -> {
                    Client client = clientList.stream().filter(clients -> clients.getIp().equals(p.getAddress().getHostString())).findFirst().orElse(null);
                    if (client != null) {

                        List<Integer> groups = new ArrayList<>();
                        Arrays.stream(client.getServerGroups()).forEach(groups::add);

                        if (!groups.contains(instantVerify.getTeamSpeak().getServerGroup())) {
                            apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), client.getDatabaseId());
                            apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", p.getName())
                                            .replace("%UUID", p.getUniqueId().toString())
                            ));
                            p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.success"));
                            instantVerify.getDbManager().getVerifyPlayer().update(p.getUniqueId(), client.getUniqueIdentifier(), null, null);
                        }
                    }
                }).onFailure(ex -> System.err.println("instantverify >> Could not get players! \n" + ex.getMessage()));
            }

            StringJoiner stringJoiner = new StringJoiner("|", "<", ">");
            if (apiAsync != null) {
                stringJoiner.add("Teamspeak Name|Teamspeak Unique ID");
            }
            if (instantVerify.getDiscord().getJda() != null) {
                stringJoiner.add("Discord ID");
            }
            stringJoiner.add("Status");
            p.sendMessage(instantVerify.getPrefix() + "/verify " + stringJoiner.toString());
        } else {
            if (args[0].equalsIgnoreCase("author") || args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("status")) {
                p.sendMessage("");

                p.sendMessage("§7TeamSpeak: " + (apiAsync == null ? "§4Offline" : "§aOnline"));
                p.sendMessage("§7Discord: " + (instantVerify.getDiscord().getJda() == null ? "§4Offline" : "§aOnline"));

                p.sendMessage("");
                p.sendMessage("§9§oAuthor: Luuuuuis");
                p.sendMessage("§9§oTwitter: @realluuuuuis");
                p.sendMessage("§9§oGitHub: https://github.com/Luuuuuis/InstantVerify");
                p.sendMessage("§9§oVersion: " + instantVerify.getUpdate().getVersion());
                p.sendMessage("");
            } else if (args[0].length() == 18) {
                if (instantVerify.getDiscord().getJda() == null) {
                    p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.noconnection"));
                    return;
                }
                User user = instantVerify.getDiscord().getJda().getUserById(args[0]);
                if (user == null) {
                    p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.discord.usernull"));
                    return;
                }

                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p.getUniqueId().toString(), instantVerify);
                if (playerInfo != null && playerInfo.getDiscordid() != null && playerInfo.getDiscordid().equals(user.getId())) {
                    p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.already"));
                    return;
                }

                instantVerify.getDiscord().getVerifying().put(user.getId(), p);
                user.openPrivateChannel().queue(channel -> channel.sendMessage(instantVerify.getLangConfig().getMessages().get("verify.bot.wrote").toString().replace("%MENTION", user.getAsMention())).queue());
            } else if (args[0].endsWith("=") && args[0].length() == 28) {
                if (apiAsync == null) {
                    p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.noconnection"));
                    return;
                }

                apiAsync.getClientByUId(args[0]).onSuccess(clientInfo -> {
                    if (clientInfo == null) {
                        p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.teamspeak.usernull"));
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(clientInfo.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(instantVerify.getTeamSpeak().getServerGroup())) {
                        if (clientInfo.getIp().equals(p.getAddress().getHostString())) {
                            apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), clientInfo.getDatabaseId());
                            apiAsync.editDatabaseClient(clientInfo.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", p.getName())
                                            .replace("%UUID", p.getUniqueId().toString())
                            ));
                            p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.success"));
                            instantVerify.getDbManager().getVerifyPlayer().update(p.getUniqueId(), clientInfo.getUniqueIdentifier(), null, null);
                        } else {
                            p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.teamspeak.otherip"));
                        }
                    } else {
                        p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.already"));
                    }
                }).onFailure(ex -> p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.teamspeak.usernull")));
            } else {
                if (apiAsync == null) {
                    p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.noconnection"));
                    return;
                }

                apiAsync.getClientByNameExact(args[0], false).onSuccess(client -> {
                    if (client == null) {
                        p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.teamspeak.usernull"));
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(client.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(instantVerify.getTeamSpeak().getServerGroup())) {
                        System.out.println(client.getIp() + " sa " + p.getAddress().getHostString());
                        if (client.getIp().equals(p.getAddress().getHostString())) {
                            apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), client.getDatabaseId());
                            apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", p.getName())
                                            .replace("%UUID", p.getUniqueId().toString())
                            ));
                            p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.success"));
                            instantVerify.getDbManager().getVerifyPlayer().update(p.getUniqueId(), client.getUniqueIdentifier(), null, null);
                        } else {
                            p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.teamspeak.otherip"));
                        }
                    } else {
                        p.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("verify.already"));
                    }
                });
            }
        }

    }

}