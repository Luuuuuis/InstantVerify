/*
 * Developed by Luuuuuis on 09.04.19 15:00.
 * Last modified 09.04.19 14:38.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee;

import de.luuuuuis.Bungee.Commands.IVUpdateCommand;
import de.luuuuuis.Bungee.Commands.VerifyCommand;
import de.luuuuuis.Bungee.Discord.Discord;
import de.luuuuuis.Bungee.Listener.Login;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import de.luuuuuis.Bungee.misc.ServerConfig;
import de.luuuuuis.Bungee.misc.Update;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class InstantVerify extends Plugin {

    private String prefix;
    private ServerConfig serverConfig;
    private TeamSpeak teamSpeak;
    private Update update;
    private Discord discord;

    @Override
    public void onEnable() {
        super.onEnable();

        System.out.println("You are using\n" +
                " __     __   __     ______     ______   ______     __   __     ______   __   __   ______     ______     __     ______   __  __    \n" +
                "/\\ \\   /\\ \"-.\\ \\   /\\  ___\\   /\\__  _\\ /\\  __ \\   /\\ \"-.\\ \\   /\\__  _\\ /\\ \\ / /  /\\  ___\\   /\\  == \\   /\\ \\   /\\  ___\\ /\\ \\_\\ \\   \n" +
                "\\ \\ \\  \\ \\ \\-.  \\  \\ \\___  \\  \\/_/\\ \\/ \\ \\  __ \\  \\ \\ \\-.  \\  \\/_/\\ \\/ \\ \\ \\'/   \\ \\  __\\   \\ \\  __<   \\ \\ \\  \\ \\  __\\ \\ \\____ \\  \n" +
                " \\ \\_\\  \\ \\_\\\\\"\\_\\  \\/\\_____\\    \\ \\_\\  \\ \\_\\ \\_\\  \\ \\_\\\\\"\\_\\    \\ \\_\\  \\ \\__|    \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_\\  \\ \\_\\    \\/\\_____\\ \n" +
                "  \\/_/   \\/_/ \\/_/   \\/_____/     \\/_/   \\/_/\\/_/   \\/_/ \\/_/     \\/_/   \\/_/      \\/_____/   \\/_/ /_/   \\/_/   \\/_/     \\/_____/ \n" +
                "\n\n" +
                "Version: " + getDescription().getVersion() + "\n" +
                "Support: https://discord.gg/2aSSGcz\n" +
                "GitHub: https://github.com/Luuuuuis/InstantVerify\n"
        );


        /*
         * ServerConfig
         */
        serverConfig = new ServerConfig(this);

        /*
         * Starts the TeamSpeak Bot
         */
        if (!serverConfig.getTeamSpeakCredentials().get("password").toString().equals("yourPassword")) {
            teamSpeak = new TeamSpeak(this);
        }

        /*
         * Starts the Discord Bot
         */
        String botToken = serverConfig.getDiscordCredentials().get("Token").toString();
        discord = new Discord(this);
        if (!botToken.equals("BOT-TOKEN")) {
            discord.init(botToken);
            discord.setDiscordRole(serverConfig.getDiscordCredentials().get("ServerGroup").toString());
        }

        /*
         * Commands Commands
         */

        PluginManager pm = getProxy().getPluginManager();
        pm.registerCommand(this, new VerifyCommand("verify", this));
        pm.registerCommand(this, new IVUpdateCommand("InstantVerifyUpdate", this));
        if (serverConfig.getTeamSpeakCredentials().get("Instant").equals(true)) {
            pm.registerListener(this, new Login(this));
        }

        /*
         * Updater
         */
        update = new Update(this);
        update.searchForUpdate();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        teamSpeak.getQuery().exit();
        getDiscord().getJda().shutdownNow();
        System.out.println("InstantVerify >> Did you like it? Yes/No? Drop me a line and let me know what's on your mind! \n\n" +
                "Discord for Support: https://discord.gg/2aSSGcz\n" +
                "GitHub: https://github.com/Luuuuuis/InstantVerify\n" +
                "GitHub Issue: https://github.com/Luuuuuis/InstantVerify/issue\n");
    }

    public TeamSpeak getTeamSpeak() {
        return teamSpeak;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public Update getUpdate() {
        return update;
    }

    public Discord getDiscord() {
        return discord;
    }
}
