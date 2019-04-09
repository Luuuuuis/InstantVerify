/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Discord;

import de.luuuuuis.InstantVerify.InstantVerify;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Discord {

    private JDA jda;
    private HashMap<String, ProxiedPlayer> verifying = new HashMap<>();
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

    public HashMap<String, ProxiedPlayer> getVerifying() {
        return verifying;
    }

    public String getDiscordRole() {
        return discordRole;
    }

    public void setDiscordRole(String discordRole) {
        this.discordRole = discordRole;
    }
}
