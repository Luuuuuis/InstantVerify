package de.luuuuuis.Bungee;

import de.luuuuuis.Bungee.Discord.Discord;
import de.luuuuuis.Bungee.Minecraft.VerifyCommand;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee
 * Date: 02.01.2019
 * Time 14:29
 */
public class InstantVerify extends Plugin {

    private static InstantVerify instance;
    public static String prefix;
    public static String discordRole;
    public static String version;

    static InstantVerify getInstance() {
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
        ServerConfig serverConfig = new ServerConfig();

        /*
         * Starts the TeamSpeak Bot
         */
        if (!serverConfig.getTeamSpeakCredentials().get("password").toString().equals("yourPassword")) {
            new TeamSpeak(serverConfig.getTeamSpeakCredentials().get("Host").toString(), serverConfig.getTeamSpeakCredentials().get("username").toString(), serverConfig.getTeamSpeakCredentials().get("password").toString(),
                    serverConfig.getTeamSpeakCredentials().get("VirtualServerId").toString(), serverConfig.getTeamSpeakCredentials().get("Nickname").toString(), serverConfig.getTeamSpeakCredentials().get("ServerGroup").toString());
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
         * Minecraft Commands
         */

        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerCommand(this, new VerifyCommand("verify"));

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
