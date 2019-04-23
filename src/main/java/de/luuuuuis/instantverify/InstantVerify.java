/*
 * Developed by Luuuuuis on 23.04.19 19:42.
 * Last modified 23.04.19 17:04.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify;

import de.luuuuuis.instantverify.commands.IVUpdateCommand;
import de.luuuuuis.instantverify.commands.VerifyCommand;
import de.luuuuuis.instantverify.database.DBManager;
import de.luuuuuis.instantverify.discord.Discord;
import de.luuuuuis.instantverify.listener.Login;
import de.luuuuuis.instantverify.misc.ServerConfig;
import de.luuuuuis.instantverify.misc.Update;
import de.luuuuuis.instantverify.teamspeak.TeamSpeak;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class InstantVerify extends Plugin {

    private String prefix;
    private ServerConfig serverConfig;
    private TeamSpeak teamSpeak;
    private Update update;
    private Discord discord;

    private DBManager dbManager;

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
                "GitHub: https://github.com/Luuuuuis/InstantVerify"
        );


        /*
         * Config
         */
        serverConfig = new ServerConfig(this);

        /*
         * Starts the teamspeak Bot
         */
        if (!serverConfig.getTeamSpeakCredentials().get("password").toString().equals("yourPassword")) {
            teamSpeak = new TeamSpeak(this);
        }

        /*
         * Starts the discord Bot
         */
        String botToken = serverConfig.getDiscordCredentials().get("Token").toString();
        discord = new Discord(this);
        if (!botToken.equals("BOT-TOKEN")) {
            discord.init(botToken);
            discord.setDiscordRole(serverConfig.getDiscordCredentials().get("ServerGroup").toString());
        }

        /*
         * commands commands
         */
        PluginManager pm = getProxy().getPluginManager();
        pm.registerCommand(this, new VerifyCommand("verify", this));
        pm.registerCommand(this, new IVUpdateCommand("InstantVerifyUpdate", this));
        if (serverConfig.getTeamSpeakCredentials().get("Instant").equals(true)) {
            pm.registerListener(this, new Login(this));
        }

        /*
         * database Manager
         */
        dbManager = new DBManager(this);
        dbManager.connect();

        /*
         * Updater
         */
        update = new Update(this);
        update.searchForUpdate();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (teamSpeak.getQuery() != null)
            teamSpeak.getQuery().exit();
        if (getDiscord().getJda() != null)
            getDiscord().getJda().shutdown();
        dbManager.close();
        System.out.println("instantverify >> Bye! See you soon.");
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

    public DBManager getDbManager() {
        return dbManager;
    }
}
