/*
 * Developed by Luuuuuis on 04.05.19 23:14.
 * Last modified 04.05.19 23:08.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.discord;

import de.luuuuuis.instantverify.InstantVerify;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
                e.getJDA().getGuilds().forEach(guilds -> guilds.getController().addRolesToMember(guilds.getMember(e.getAuthor()), e.getJDA().getRolesByName(instantVerify.getDiscord().getDiscordRole(), true)).complete());
                e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage(instantVerify.getLangConfig().getMessages().get("discord.success").toString()).queue());
                instantVerify.getDbManager().getVerifyPlayer().update(instantVerify.getDiscord().getVerifying().get(e.getAuthor().getId()).getUniqueId(), null, e.getAuthor().getId(), null);
                instantVerify.getDiscord().getVerifying().remove(e.getAuthor().getId());
            } else {
                e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage(instantVerify.getLangConfig().getMessages().get("discord.tryagain").toString()).queue());
            }
        } else {
            e.getAuthor().openPrivateChannel().queue(channel -> {
                e.getChannel().sendMessage(instantVerify.getLangConfig().getMessages().get("discord.privatemessage").toString().replace("%ID", e.getAuthor().getId())).queue();
            });
        }


    }

}
