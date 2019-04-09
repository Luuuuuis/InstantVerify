/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Commands;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.InstantVerify.Database.PlayerInfo;
import de.luuuuuis.InstantVerify.Events.VerifyEvent;
import de.luuuuuis.InstantVerify.InstantVerify;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class VerifyCommand extends Command {

    private InstantVerify instantVerify;

    public VerifyCommand(String name, InstantVerify instantVerify) {
        super(name, "", "InstantVerify", "IV");
        this.instantVerify = instantVerify;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("InstantVerify >> Command only usable as player!");
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
                            VerifyEvent verifyEvent = new VerifyEvent();
                            ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                            if (!verifyEvent.isCancelled()) {
                                apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), client.getDatabaseId());
                                apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                        instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                                .replace("%Name", p.getName())
                                                .replace("%UUID", p.getUniqueId().toString())
                                ));
                                p.sendMessage(instantVerify.getPrefix() + "§aDu hast dich erfolgreich verifiziert!");
                                instantVerify.getDbManager().getVerifyPlayer().update(p.getUniqueId(), client.getUniqueIdentifier(), null, null);
                            }
                        }
                    }
                }).onFailure(ex -> System.err.println("InstantVerify >> Could not get players! \n" + ex.getMessage()));
            }

            StringJoiner stringJoiner = new StringJoiner("|", "<", ">");
            if (apiAsync != null) {
                stringJoiner.add("TeamSpeak Name|TeamSpeak Unique ID");
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
                p.sendMessage("§9§oAutor: Luuuuuis");
                p.sendMessage("§9§oTwitter: @realluuuuuis");
                p.sendMessage("§9§oGitHub: https://github.com/Luuuuuis/InstantVerify");
                p.sendMessage("§9§oVersion: " + instantVerify.getUpdate().getVersion());
                p.sendMessage("");
            } else if (args[0].length() == 18) {
                if (instantVerify.getDiscord().getJda() == null) {
                    p.sendMessage(instantVerify.getPrefix() + "§4Wir konnten dich leider nicht verifizieren, da keine Verbindung zu Discord besteht! :(");
                    return;
                }
                User user = instantVerify.getDiscord().getJda().getUserById(args[0]);
                if (user == null) {
                    p.sendMessage(instantVerify.getPrefix() + "Bitte betrete unseren Discord und überprüfe deine Discord ID.");
                    return;
                }

                PlayerInfo playerInfo = PlayerInfo.getPlayerInfo(p.getUniqueId().toString(), instantVerify);
                if (playerInfo != null && playerInfo.getDiscordid() != null && playerInfo.getDiscordid().equals(user.getId())) {
                    p.sendMessage(instantVerify.getPrefix() + "Du hast dich bereits verifiziert!");
                    return;
                }

                instantVerify.getDiscord().getVerifying().put(user.getId(), p);
                user.openPrivateChannel().queue(channel -> channel.sendMessage("Hi " + user.getAsMention() + ",\nBitte sende mir dein Minecraft Namen, damit wir dich überprüfen können." +
                        "\nFalls dies nicht dein Account ist, schließe diesen Chat einfach.").queue());
                p.sendMessage(instantVerify.getPrefix() + "Der Discord Bot hat dir eine Nachricht gesendet.");
            } else if (args[0].endsWith("=") && args[0].length() == 28) {
                if (apiAsync == null) {
                    p.sendMessage(instantVerify.getPrefix() + "§4Wir konnten dich leider nicht verifizieren, da keine Verbindung zu TeamSpeak besteht! :(");
                    return;
                }

                apiAsync.getClientByUId(args[0]).onSuccess(clientInfo -> {
                    if (clientInfo == null) {
                        p.sendMessage(instantVerify.getPrefix() + "Bitte betrete unseren TeamSpeak und überprüfe dein Namen.");
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(clientInfo.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(instantVerify.getTeamSpeak().getServerGroup())) {
                        if (clientInfo.getIp().equals(p.getAddress().getHostString())) {
                            VerifyEvent verifyEvent = new VerifyEvent();
                            ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                            if (!verifyEvent.isCancelled()) {
                                apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), clientInfo.getDatabaseId());
                                apiAsync.editDatabaseClient(clientInfo.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                        instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                                .replace("%Name", p.getName())
                                                .replace("%UUID", p.getUniqueId().toString())
                                ));
                                p.sendMessage(instantVerify.getPrefix() + "§aDu hast dich erfolgreich verifiziert!");
                                instantVerify.getDbManager().getVerifyPlayer().update(p.getUniqueId(), clientInfo.getUniqueIdentifier(), null, null);
                            }
                        } else {
                            p.sendMessage(instantVerify.getPrefix() + "§4Wir konnten dich leider nicht verifizieren, da deine IP auf dem Commands Server eine andere als auf dem TeamSpeak ist!");
                        }
                    } else {
                        p.sendMessage(instantVerify.getPrefix() + "§cDu hast dich bereits verifiziert.");
                    }
                }).onFailure(ex -> p.sendMessage(instantVerify.getPrefix() + "Bitte betrete unseren TeamSpeak und überprüfe dein Namen."));
            } else {
                if (apiAsync == null) {
                    p.sendMessage(instantVerify.getPrefix() + "§4Wir konnten dich leider nicht verifizieren, da keine Verbindung zu TeamSpeak besteht! :(");
                    return;
                }

                apiAsync.getClientByNameExact(args[0], false).onSuccess(client -> {
                    if (client == null) {
                        p.sendMessage(instantVerify.getPrefix() + "Bitte betrete unseren TeamSpeak und überprüfe dein Namen.");
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(client.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(instantVerify.getTeamSpeak().getServerGroup())) {
                        if (client.getIp().equals(p.getAddress().getHostString())) {
                            VerifyEvent verifyEvent = new VerifyEvent();
                            ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                            if (!verifyEvent.isCancelled()) {
                                apiAsync.addClientToServerGroup(instantVerify.getTeamSpeak().getServerGroup(), client.getDatabaseId());
                                apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                        instantVerify.getServerConfig().getTeamSpeakCredentials().get("Description").toString()
                                                .replace("%Name", p.getName())
                                                .replace("%UUID", p.getUniqueId().toString())
                                ));
                                p.sendMessage(instantVerify.getPrefix() + "§aDu hast dich erfolgreich verifiziert!");
                                instantVerify.getDbManager().getVerifyPlayer().update(p.getUniqueId(), client.getUniqueIdentifier(), null, null);
                            }
                        } else {
                            p.sendMessage(instantVerify.getPrefix() + "§4Wir konnten dich leider nicht verifizieren, da deine IP auf dem Commands Server eine andere als auf dem TeamSpeak ist!");
                        }
                    } else {
                        p.sendMessage(instantVerify.getPrefix() + "§cDu hast dich bereits verifiziert.");
                    }
                });
            }
        }

    }

}