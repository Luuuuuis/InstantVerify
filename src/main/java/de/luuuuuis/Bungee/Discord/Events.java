/*
 * Developed by Luuuuuis on 09.04.19 15:00.
 * Last modified 09.04.19 14:42.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.Discord;

import de.luuuuuis.Bungee.Events.VerifyEvent;
import de.luuuuuis.Bungee.InstantVerify;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;

public class Events extends ListenerAdapter {

    private InstantVerify instantVerify;

    public Events(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        String message = e.getMessage().getContentDisplay();

        if (instantVerify.getDiscord().getVerifying().containsKey(e.getAuthor().getName())) {
            if (message.equalsIgnoreCase(instantVerify.getDiscord().getVerifying().get(e.getAuthor().getName()))) {
                VerifyEvent verifyEvent = new VerifyEvent();
                ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                if (!verifyEvent.isCancelled()) {
                    e.getJDA().getGuilds().forEach(guilds -> guilds.getController().addRolesToMember(guilds.getMember(e.getAuthor()), e.getJDA().getRolesByName(instantVerify.getDiscord().getDiscordRole(), true)).complete());
                    e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage("Du hast dich erfolgreich verifiziert! Wir sehn uns. ;)").queue());
                    instantVerify.getDiscord().getVerifying().remove(e.getAuthor().getName());
                }
            } else {
                e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage("Uppps... Versuchs nochmal!").queue());
            }
        } else {
            e.getAuthor().openPrivateChannel().queue(channel -> {
                e.getChannel().sendMessage("Hast du schon mal versucht /verify <Discord ID> auf dem Commands Server auszuführen? Deine ID ist " + e.getAuthor().getId() + "\n" +
                        "Falls du sonst noch Probleme hast, kannst du gerne ein Teammitglied anschreiben.")
                        .queue();
            });
        }


    }

}
