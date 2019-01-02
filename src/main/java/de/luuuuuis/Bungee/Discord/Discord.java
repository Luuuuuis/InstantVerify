package de.luuuuuis.Bungee.Discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;

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

    @SuppressWarnings("deprecation")
    public Discord(String token) {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListener(new Events())
                    .buildBlocking();
            jda.setAutoReconnect(true);
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JDA getJda() {
        return jda;
    }
}
