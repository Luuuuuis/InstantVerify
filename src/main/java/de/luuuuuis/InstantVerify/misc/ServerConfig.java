/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.luuuuuis.InstantVerify.InstantVerify;
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
    private HashMap<String, Object> SQLiteCredentials = new HashMap<>();
    private HashMap<String, Object> MySQLCredentials = new HashMap<>();
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

                    JSONObject SQLite = new JSONObject();
                    SQLite.put("active", true);
                    SQLite.put("Database", "verify");
                    jsonObject.put("SQLite", SQLite);

                    JSONObject MySQL = new JSONObject();
                    MySQL.put("active", false);
                    MySQL.put("Host", "localhost");
                    MySQL.put("Port", 3306);
                    MySQL.put("Database", "verify");
                    MySQL.put("User", "root");
                    MySQL.put("Password", "yourPassword");
                    jsonObject.put("MySQL", MySQL);

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

                Map SQLiteJSON = (Map) jsonObject.get("SQLite");
                for (Object o : SQLiteJSON.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    SQLiteCredentials.put(pair.getKey().toString(), pair.getValue());
                }

                Map MySQLJSON = (Map) jsonObject.get("MySQL");
                for (Object o : MySQLJSON.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    MySQLCredentials.put(pair.getKey().toString(), pair.getValue());
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

    public HashMap<String, Object> getSQLiteCredentials() {
        return SQLiteCredentials;
    }

    public HashMap<String, Object> getMySQLCredentials() {
        return MySQLCredentials;
    }
}
