/*
 * Developed by Luuuuuis on 07.04.19 11:43.
 * Last modified 07.04.19 11:41.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee;

import de.luuuuuis.Bungee.Commands.VerifyCommand;
import de.luuuuuis.Bungee.Discord.Discord;
import de.luuuuuis.Bungee.Listener.Login;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class InstantVerify extends Plugin {

    public static String prefix;
    public static String discordRole;
    public static String version;
    public static ServerConfig serverConfig;
    private static InstantVerify instance;

    public static InstantVerify getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        System.out.println("Thanks for using\n" +
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
        serverConfig = new ServerConfig();

        /*
         * Starts the TeamSpeak Bot
         */
        if (!serverConfig.getTeamSpeakCredentials().get("password").toString().equals("yourPassword")) {
            new TeamSpeak();
        }

        /*
         * Starts the Discord Bot
         */
        String botToken = serverConfig.getDiscordCredentials().get("Token").toString();
        if (!botToken.equals("BOT-TOKEN")) {
            new Discord(botToken);
            discordRole = serverConfig.getDiscordCredentials().get("ServerGroup").toString();
        }

        /*
         * Commands Commands
         */

        PluginManager pm = getProxy().getPluginManager();
        pm.registerCommand(this, new VerifyCommand("verify"));
        if (serverConfig.getTeamSpeakCredentials().get("Instant").equals(true)) {
            pm.registerListener(this, new Login());
        }

        /*
         * Updater
         */
        version = getDescription().getVersion();
        new Update(version, getFile());
    }

    @Override
    public void onDisable() {
        super.onDisable();
        TeamSpeak.getQuery().exit();
        Discord.getJda().shutdownNow();
        System.out.println("InstantVerify >> Did you like it? Yes/No? Drop me a line and let me know what's on your mind! \n\n" +
                "Discord for Support: https://discord.gg/2aSSGcz\n" +
                "GitHub: https://github.com/Luuuuuis/InstantVerify\n" +
                "GitHub Issue: https://github.com/Luuuuuis/InstantVerify/issue\n");
    }
}
