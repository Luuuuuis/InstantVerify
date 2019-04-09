/*
 * Developed by Luuuuuis on 09.04.19 15:00.
 * Last modified 09.04.19 14:11.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.luuuuuis.Bungee.InstantVerify;
import net.md_5.bungee.api.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerConfig {

    private HashMap<String, Object> TeamSpeakCredentials = new HashMap<>();
    private HashMap<String, Object> DiscordCredentials = new HashMap<>();
    private InstantVerify instantVerify;

    public ServerConfig(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
        query();
    }

    private void query() {
        Thread thread = new Thread(() -> {

            File file = new File(instantVerify.getDataFolder().getPath(), "config.json");
            if (!file.exists()) {
                if (!instantVerify.getDataFolder().exists())
                    instantVerify.getDataFolder().mkdir();

                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try (FileWriter fileWriter = new FileWriter(instantVerify.getDataFolder().getPath() + "/config.json")) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Prefix", "&eInstantVerify &8>>");

                    JSONObject discord = new JSONObject();
                    discord.put("Token", "BOT-TOKEN");
                    discord.put("ServerGroup", "user");
                    jsonObject.put("Discord", discord);

                    JSONObject teamspeak = new JSONObject();
                    teamspeak.put("Host", "localhost");
                    teamspeak.put("username", "serveradmin");
                    teamspeak.put("password", "yourPassword");
                    teamspeak.put("VirtualServerID", 1);
                    teamspeak.put("Nickname", "InstantVerify TS Bot");
                    teamspeak.put("ServerGroup", 7);
                    teamspeak.put("Description", "Minecraft Name: %Name | UUID: %UUID");
                    teamspeak.put("Instant", true);
                    jsonObject.put("TeamSpeak", teamspeak);


                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    fileWriter.write(gson.toJson(jsonObject));
                    fileWriter.flush();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

            try {
                Object object = new JSONParser().parse(new FileReader(instantVerify.getDataFolder().getPath() + "/config.json"));
                JSONObject jsonObject = (JSONObject) object;

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

                instantVerify.setPrefix(ChatColor.translateAlternateColorCodes('&', jsonObject.get("Prefix").toString().replace("§", "&") + "&7 "));

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

    public HashMap<String, Object> getDiscordCredentials() {
        return DiscordCredentials;
    }
}
