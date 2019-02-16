package de.luuuuuis.Bungee.Discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.Discord
 * Date: 02.01.2019
 * Time 19:01
 */
public class Discord {

    private static JDA jda;

    public Discord(String token) {

        Thread thread = new Thread(() -> {
            try {
                jda = new JDABuilder(AccountType.BOT)
                        .setToken(token)
                        .setStatus(OnlineStatus.ONLINE)
                        .addEventListener(new Events())
                        .setGame(Game.listening("Write me"))
                        .buildAsync();
                jda.setAutoReconnect(true);
            } catch (LoginException e) {
                e.printStackTrace();
                jda = null;
            }
        });
        thread.start();

    }

    public static JDA getJda() {
        return jda;
    }
}
