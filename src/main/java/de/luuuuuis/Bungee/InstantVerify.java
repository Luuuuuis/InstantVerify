package de.luuuuuis.Bungee;

import de.luuuuuis.Bungee.Minecraft.VerifyCommand;
import de.luuuuuis.Bungee.TeamSpeak.TeamSpeak;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee
 * Date: 02.01.2019
 * Time 14:29
 */
public class InstantVerify extends Plugin {

    public static InstantVerify instance;
    public static String prefix;
    public static TeamSpeak ts;

    public static InstantVerify getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        System.out.println("\n" +
                " __     __   __     ______     ______   ______     __   __     ______   __   __   ______     ______     __     ______   __  __    \n" +
                "/\\ \\   /\\ \"-.\\ \\   /\\  ___\\   /\\__  _\\ /\\  __ \\   /\\ \"-.\\ \\   /\\__  _\\ /\\ \\ / /  /\\  ___\\   /\\  == \\   /\\ \\   /\\  ___\\ /\\ \\_\\ \\   \n" +
                "\\ \\ \\  \\ \\ \\-.  \\  \\ \\___  \\  \\/_/\\ \\/ \\ \\  __ \\  \\ \\ \\-.  \\  \\/_/\\ \\/ \\ \\ \\'/   \\ \\  __\\   \\ \\  __<   \\ \\ \\  \\ \\  __\\ \\ \\____ \\  \n" +
                " \\ \\_\\  \\ \\_\\\\\"\\_\\  \\/\\_____\\    \\ \\_\\  \\ \\_\\ \\_\\  \\ \\_\\\\\"\\_\\    \\ \\_\\  \\ \\__|    \\ \\_____\\  \\ \\_\\ \\_\\  \\ \\_\\  \\ \\_\\    \\/\\_____\\ \n" +
                "  \\/_/   \\/_/ \\/_/   \\/_____/     \\/_/   \\/_/\\/_/   \\/_/ \\/_/     \\/_/   \\/_/      \\/_____/   \\/_/ /_/   \\/_/   \\/_/     \\/_____/ \n" +
                "\n\n" +
                "Version: " + getDescription().getVersion() + "\n" +
                "Support: https://discord.gg/2aSSGcz \n" +
                "GitHub: https://github.com/Luuuuuis/InstantVerify\n"
        );

        /*
         * Updater
         */
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL("https://raw.githubusercontent.com/Luuuuuis/InstantVerify/master/version");
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        assert connection != null;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String[] versionCode = in.readLine().split("&&");

            if (!(versionCode[0].equalsIgnoreCase(getDescription().getVersion()))) {
                Thread th = new Thread(() -> {

                    URL dURL = null;
                    try {
                        dURL = new URL(versionCode[1]);
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }

                    assert dURL != null;
                    try (InputStream input = dURL.openStream();
                         FileOutputStream output = new FileOutputStream(getFile())) {
                        byte[] buffer = new byte[4096];
                        int n;
                        while (-1 != (n = input.read(buffer))) {
                            output.write(buffer, 0, n);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    System.out.println("InstantVerify >> A new update is available(" + versionCode[0] + " please restart your server soon.");
                    System.out.println("InstantVerify >> Changelog can be viewed at GitHub: https://github.com/Luuuuuis/InstantVerify/releases");

                });
                th.start();
            }
            connection.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Config config = new Config();


        /*
         * Starts the TeamSpeak Bot
         */
        ts = new TeamSpeak(config.getTeamSpeakCredentials().get("Host").toString(), config.getTeamSpeakCredentials().get("username").toString(), config.getTeamSpeakCredentials().get("password").toString(),
                config.getTeamSpeakCredentials().get("VirtualServerId").toString(), config.getTeamSpeakCredentials().get("Nickname").toString(), config.getTeamSpeakCredentials().get("ServerGroup").toString());

        /*
         * Minecraft Commands
         */

        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerCommand(this, new VerifyCommand("verify"));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
