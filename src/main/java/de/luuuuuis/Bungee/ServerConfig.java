/*
 * Developed by Luuuuuis on 16.03.19 19:32.
 * Last modified 16.03.19 19:30.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee;

import net.md_5.bungee.api.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ServerConfig {

    private HashMap<String, Object> TeamSpeakCredentials = new HashMap<>();
    private HashMap<String, Object> DiscordCredentials = new HashMap<>();

    public ServerConfig() {

        Thread thread = new Thread(() -> {

            File file = new File(InstantVerify.getInstance().getDataFolder().getPath(), "config.json");
            if (!file.exists()) {
                if (!InstantVerify.getInstance().getDataFolder().exists())
                    InstantVerify.getInstance().getDataFolder().mkdir();
                try {
                    file.createNewFile();

                    URL downloadURL = new URL("https://raw.githubusercontent.com/Luuuuuis/InstantVerify/master/config.json");

                    HttpURLConnection urlConnection = (HttpURLConnection) downloadURL.openConnection();
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

                    InputStream inputStream = urlConnection.getInputStream();

                    Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    inputStream.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }


            }

            try {
                Object object = new JSONParser().parse(new FileReader(InstantVerify.getInstance().getDataFolder().getPath() + "/config.json"));
                JSONObject jsonObject = (JSONObject) object;

                InstantVerify.prefix = ChatColor.translateAlternateColorCodes('&', jsonObject.get("Prefix").toString().replace("§", "&") + "&7 ");

                Map TeamSpeakJSON = (Map) jsonObject.get("TeamSpeak");
                for (Object o : TeamSpeakJSON.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    TeamSpeakCredentials.put(pair.getKey().toString(), pair.getValue());
                }

                Map DiscordJSON = (Map) jsonObject.get("Discord");
                for (Object o : DiscordJSON.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    DiscordCredentials.put(pair.getKey().toString(), pair.getValue());
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getTeamSpeakCredentials() {
        return TeamSpeakCredentials;
    }

    HashMap<String, Object> getDiscordCredentials() {
        return DiscordCredentials;
    }
}
