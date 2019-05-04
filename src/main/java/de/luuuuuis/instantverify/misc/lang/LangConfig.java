/*
 * Developed by Luuuuuis on 04.05.19 23:14.
 * Last modified 04.05.19 23:07.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.misc.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.luuuuuis.instantverify.InstantVerify;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LangConfig {

    private InstantVerify instantVerify;
    private Map<String, Object> messages = new HashMap<>();

    public LangConfig(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
        query();
    }

    private void query() {
        Thread thread = new Thread(() -> {

            File file = new File(instantVerify.getDataFolder().getPath(), "messages.json");
            if (!file.exists()) {
                if (!instantVerify.getDataFolder().exists())
                    instantVerify.getDataFolder().mkdir();

                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try (FileWriter fileWriter = new FileWriter(instantVerify.getDataFolder().getPath() + "/messages.json")) {

                    JSONObject jsonObject = new JSONObject();

                    JSONObject messages = new JSONObject();
                    messages.put("update.restart", "§6An update was downloaded. Please restart the proxy.");
                    messages.put("update.latest", "§aYou are already running the latest version!");
                    messages.put("verify.success", "§aYou are now verified!");
                    messages.put("verify.noconnection", "§4Unfortunately we could not verify you, because there is no connection! :(");
                    messages.put("verify.discord.usernull", "Please join our Discord server and check your Discord ID.");
                    messages.put("verify.already", "You are already verified!");
                    messages.put("verify.bot.wrote", "Hi %MENTION,\nPlease send me your Minecraft name so I can verify you.\nIf this is not your account, please close this chat.");
                    messages.put("verify.teamspeak.usernull", "Please join our TeamSpeak server and check your name.");
                    messages.put("verify.teamspeak.otherip", "§4Unfortunately we could not verify you, because your IP on the server is a different one than on TeamSpeak!");
                    messages.put("discord.listening", "write me!");
                    messages.put("discord.success", "You are now verified! See you on our Discord.");
                    messages.put("discord.tryagain", "Misspelled? Try again!");
                    messages.put("discord.privatemessage", "Try /verify %ID on our Minecraft server to get verified.");
                    jsonObject.put("messages", messages);


                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    fileWriter.write(gson.toJson(jsonObject));
                    fileWriter.flush();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            }

            try {
                Object object = new JSONParser().parse(new FileReader(instantVerify.getDataFolder().getPath() + "/messages.json"));
                JSONObject jsonObject = (JSONObject) object;

                Map messagesJSON = (Map) jsonObject.get("messages");
                for (Object o : messagesJSON.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    messages.put(pair.getKey().toString(), pair.getValue());
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

    public Map<String, Object> getMessages() {
        return messages;
    }
}