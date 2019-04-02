/*
 * Developed by Luuuuuis on 02.04.19 19:39.
 * Last modified 02.04.19 19:36.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Spigot;

import de.luuuuuis.Spigot.Commands.VerifyCommand;
import de.luuuuuis.Spigot.Discord.Discord;
import de.luuuuuis.Spigot.Listener.Join;
import de.luuuuuis.Spigot.TeamSpeak.TeamSpeak;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class InstantVerify extends JavaPlugin {


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
         * Starts the Discord Bot (Further Updates)
         */
        String botToken = serverConfig.getDiscordCredentials().get("Token").toString();
        if (!botToken.equals("BOT-TOKEN")) {
            new Discord(botToken);
            discordRole = serverConfig.getDiscordCredentials().get("ServerGroup").toString();
        }

        /*
         * Commands Commands
         */

        PluginManager pm = getServer().getPluginManager();
        getCommand("verify").setExecutor(new VerifyCommand());
        if (serverConfig.getTeamSpeakCredentials().get("Instant").equals(true)) {
            pm.registerEvents(new Join(), this);
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
//        Discord.getJda().shutdownNow();
        System.out.println("InstantVerify >> Did you like it? Yes/No? Drop me a line and let me know what's on your mind! \n\n" +
                "Discord for Support: https://discord.gg/2aSSGcz\n" +
                "GitHub: https://github.com/Luuuuuis/InstantVerify\n" +
                "GitHub Issue: https://github.com/Luuuuuis/InstantVerify/issue\n");
    }

}
