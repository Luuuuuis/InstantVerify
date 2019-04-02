/*
 * Developed by Luuuuuis on 02.04.19 19:39.
 * Last modified 02.04.19 19:39.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Spigot.Commands;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.luuuuuis.Spigot.Discord.Discord;
import de.luuuuuis.Spigot.InstantVerify;
import de.luuuuuis.Spigot.TeamSpeak.TeamSpeak;
import net.dv8tion.jda.core.entities.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VerifyCommand implements CommandExecutor {

    public static HashMap<String, String> verifying = new HashMap<>();
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("InstantVerify >> Command only usable as player!");
            return false;
        }

        TS3ApiAsync apiAsync = TeamSpeak.getApi();

        Player p = (Player) sender;

        if (args.length != 1) {
            String usage = InstantVerify.prefix + "/verify <";
            if (TeamSpeak.getApi() != null) {
                usage += "TeamSpeak Name|TeamSpeak Unique ID|IP";
            }
            if (Discord.getJda() != null) {
                usage += "|Discord ID";
            }
            usage += "|Status>";
            p.sendMessage(usage);
        } else {
            if (args[0].equalsIgnoreCase("author") || args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("status")) {
                p.sendMessage("");
                p.sendMessage(InstantVerify.prefix + "Bei Fragen schreibe bitte ein Teammitglied an.\n");

                p.sendMessage("§7Derzeit ist der Bot" + (TeamSpeak.getApi() != null ? "" : " §4nicht§7") + " mit dem TeamSpeak verbunden.");
//                p.sendMessage("§7Derzeit ist der Bot" + (Discord.getJda() != null ? "" : " §4nicht§7") + " mit Discord verbunden.");

                p.sendMessage("");
                p.sendMessage("§cAuthor: Luuuuuis\nTwitter: @realluuuuuis");
                p.sendMessage("§cGitHub: https://github.com/Luuuuuis/InstantVerify");
                p.sendMessage("§cGitHub Issue: https://github.com/Luuuuuis/InstantVerify/issues");
                p.sendMessage("§cSupport Discord: https://discord.gg/2aSSGcz");
                p.sendMessage("§cVersion: " + InstantVerify.version);
                p.sendMessage("");
                return false;
            } else if (args[0].equalsIgnoreCase("IP")) {

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
                                p.sendMessage(InstantVerify.prefix + "§aDu hast dich erfolgreich verifiziert!");
                            }
                        })).onFailure(ex -> System.err.println("InstantVerify >> Could not get players! \n" + ex.getMessage()));

            }
            if (args[0].length() == 18) {
                if (Discord.getJda() == null) {
                    p.sendMessage(InstantVerify.prefix + "§4Wir konnten dich leider nicht verifizieren, da keine Verbindung zu Discord besteht! :(");
                    return false;
                }
                User user = Discord.getJda().getUserById(args[0]);
                if (user == null) {
                    p.sendMessage(InstantVerify.prefix + "Bitte betrete unseren Discord und überprüfe deine Discord ID.");
                    return false;
                }
                verifying.put(user.getName(), p.getName());
                user.openPrivateChannel().queue(channel -> channel.sendMessage("Hi " + user.getAsMention() + ",\nBitte sende mir dein Commands Namen, damit wir dich überprüfen können." +
                        "\nFalls dies nicht dein Account ist, schließe diesen Chat einfach.").queue());
                p.sendMessage(InstantVerify.prefix + "Der Discord Bot hat dir eine Nachricht gesendet.");
                return false;
            }
            if (apiAsync == null) {
                p.sendMessage(InstantVerify.prefix + "§4Wir konnten dich leider nicht verifizieren, da keine Verbindung zu TeamSpeak besteht! :(");
                return false;
            }
            if (args[0].endsWith("=") && args[0].length() == 28) {
                apiAsync.getClientByUId(args[0]).onSuccess(clientInfo -> {
                    if (clientInfo == null) {
                        p.sendMessage(InstantVerify.prefix + "Bitte betrete unseren TeamSpeak und überprüfe dein Namen.");
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(clientInfo.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(TeamSpeak.getServerGroup())) {
                        if (clientInfo.getIp().equals(p.getAddress().getHostString())) {
                            apiAsync.addClientToServerGroup(TeamSpeak.getServerGroup(), clientInfo.getDatabaseId());
                            apiAsync.editDatabaseClient(clientInfo.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", p.getName())
                                            .replace("%UUID", p.getUniqueId().toString())
                            ));
                            p.sendMessage(InstantVerify.prefix + "§aDu hast dich erfolgreich verifiziert!");
                        } else {
                            p.sendMessage(InstantVerify.prefix + "§4Wir konnten dich leider nicht verifizieren, da deine IP auf dem Commands Server eine andere als auf dem TeamSpeak ist!");
                        }
                    } else {
                        p.sendMessage(InstantVerify.prefix + "§cDu hast dich bereits verifiziert.");
                    }
                }).onFailure(ex -> p.sendMessage(InstantVerify.prefix + "Bitte betrete unseren TeamSpeak und überprüfe dein Namen."));
            } else {
                apiAsync.getClientByNameExact(args[0], false).onSuccess(client -> {
                    if (client == null) {
                        p.sendMessage(InstantVerify.prefix + "Bitte betrete unseren TeamSpeak und überprüfe dein Namen.");
                        return;
                    }
                    List<Integer> groups = new ArrayList<>();
                    Arrays.stream(client.getServerGroups()).forEach(groups::add);

                    if (!groups.contains(TeamSpeak.getServerGroup())) {
                        if (client.getIp().equals(p.getAddress().getHostString())) {
                            apiAsync.addClientToServerGroup(TeamSpeak.getServerGroup(), client.getDatabaseId());
                            apiAsync.editDatabaseClient(client.getDatabaseId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
                                    InstantVerify.serverConfig.getTeamSpeakCredentials().get("Description").toString()
                                            .replace("%Name", p.getName())
                                            .replace("%UUID", p.getUniqueId().toString())
                            ));
                            p.sendMessage(InstantVerify.prefix + "§aDu hast dich erfolgreich verifiziert!");
                        } else {
                            p.sendMessage(InstantVerify.prefix + "§4Wir konnten dich leider nicht verifizieren, da deine IP auf dem Commands Server eine andere als auf dem TeamSpeak ist!");
                        }
                    } else {
                        p.sendMessage(InstantVerify.prefix + "§cDu hast dich bereits verifiziert.");
                    }
                });
            }
        }
        return false;
    }
}
