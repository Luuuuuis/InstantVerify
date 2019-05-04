/*
 * Developed by Luuuuuis on 04.05.19 23:14.
 * Last modified 04.05.19 22:33.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.discord;

import de.luuuuuis.instantverify.InstantVerify;
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
                        .setGame(Game.listening(instantVerify.getLangConfig().getMessages().get("discord.listening").toString()))
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
