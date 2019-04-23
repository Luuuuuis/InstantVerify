/*
 * Developed by Luuuuuis on 23.04.19 16:47.
 * Last modified 23.04.19 16:47.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.discord;

import de.luuuuuis.instantverify.InstantVerify;
import de.luuuuuis.instantverify.events.VerifyEvent;
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

        if (instantVerify.getDiscord().getVerifying().containsKey(e.getAuthor().getId())) {
            if (message.equalsIgnoreCase(instantVerify.getDiscord().getVerifying().get(e.getAuthor().getId()).getName())) {
                VerifyEvent verifyEvent = new VerifyEvent();
                ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                if (!verifyEvent.isCancelled()) {
                    e.getJDA().getGuilds().forEach(guilds -> guilds.getController().addRolesToMember(guilds.getMember(e.getAuthor()), e.getJDA().getRolesByName(instantVerify.getDiscord().getDiscordRole(), true)).complete());
                    e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage("Du hast dich erfolgreich verifiziert! Wir sehen uns. ;)").queue());
                    instantVerify.getDbManager().getVerifyPlayer().update(instantVerify.getDiscord().getVerifying().get(e.getAuthor().getId()).getUniqueId(), null, e.getAuthor().getId(), null);
                    instantVerify.getDiscord().getVerifying().remove(e.getAuthor().getId());
                }
            } else {
                e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage("Uppps... Versuchs nochmal!").queue());
            }
        } else {
            e.getAuthor().openPrivateChannel().queue(channel -> {
                e.getChannel().sendMessage("Hast du schon mal versucht /verify <discord ID> auf dem commands Server auszuführen? Deine ID ist " + e.getAuthor().getId() + "\n" +
                        "Falls du sonst noch Probleme hast, kannst du gerne ein Teammitglied anschreiben.")
                        .queue();
            });
        }


    }

}
