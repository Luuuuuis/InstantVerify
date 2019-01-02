package de.luuuuuis.Bungee;

import net.md_5.bungee.api.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee
 * Date: 02.01.2019
 * Time 18:00
 */
class Config {

    private HashMap<String, Object> TeamSpeakCredentials = new HashMap<>();
    private HashMap<String, Object> DiscordCredentials = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    Config() {
        File file = new File(InstantVerify.getInstance().getDataFolder().getPath(), "config.json");
        if (!file.exists()) {
            if (!InstantVerify.getInstance().getDataFolder().exists())
                InstantVerify.getInstance().getDataFolder().mkdir();
            try (InputStream input = new URL("https://raw.githubusercontent.com/Luuuuuis/InstantVerify/master/config.json").openStream();
                 FileOutputStream output = new FileOutputStream(file)) {
                file.createNewFile();

                byte[] buffer = new byte[4096];
                int n;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }

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
    }

    HashMap<String, Object> getTeamSpeakCredentials() {
        return TeamSpeakCredentials;
    }

    HashMap<String, Object> getDiscordCredentials() {
        return DiscordCredentials;
    }
}
