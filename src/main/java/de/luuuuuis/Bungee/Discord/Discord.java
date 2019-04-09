/*
 * Developed by Luuuuuis on 09.04.19 15:00.
 * Last modified 09.04.19 14:37.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.Discord;

import de.luuuuuis.Bungee.InstantVerify;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Discord {

    private JDA jda;
    private HashMap<String, String> verifying = new HashMap<>();
    private InstantVerify instantVerify;
    private String discordRole;

    public Discord(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    public void init(String token) {
        Thread thread = new Thread(() -> {
            try {
                jda = new JDABuilder(AccountType.BOT)
                        .setToken(token)
                        .setStatus(OnlineStatus.ONLINE)
                        .addEventListener(new Events(instantVerify))
                        .setGame(Game.listening("Schreib mir"))
                        .buildAsync();
                jda.setAutoReconnect(true);
            } catch (LoginException e) {
                e.printStackTrace();
                jda = null;
            }
        });
        thread.start();
    }

    public JDA getJda() {
        return jda;
    }

    public HashMap<String, String> getVerifying() {
        return verifying;
    }

    public String getDiscordRole() {
        return discordRole;
    }

    public void setDiscordRole(String discordRole) {
        this.discordRole = discordRole;
    }
}
