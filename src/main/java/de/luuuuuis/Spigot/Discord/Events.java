/*
 * Developed by Luuuuuis on 02.04.19 19:39.
 * Last modified 02.04.19 19:39.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Spigot.Discord;

import com.google.common.io.ByteStreams;
import de.luuuuuis.Spigot.Commands.VerifyCommand;
import de.luuuuuis.Spigot.InstantVerify;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;

class Events extends ListenerAdapter {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        String message = e.getMessage().getContentDisplay();

        if (VerifyCommand.verifying.containsKey(e.getAuthor().getName())) {
            if (message.equalsIgnoreCase(VerifyCommand.verifying.get(e.getAuthor().getName()))) {
                e.getJDA().getGuilds().forEach(guilds -> guilds.getController().addRolesToMember(guilds.getMember(e.getAuthor()), e.getJDA().getRolesByName(InstantVerify.discordRole, true)).complete());
                e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage("Du hast dich erfolgreich verifiziert! Wir sehn uns. ;)").queue());
                VerifyCommand.verifying.remove(e.getAuthor().getName());
            } else {
                e.getAuthor().openPrivateChannel().queue(channel -> e.getChannel().sendMessage("Uppps... Versuchs nochmal!").queue());
            }
        } else {
            e.getAuthor().openPrivateChannel().queue(channel -> {
                try {
                    e.getChannel().sendMessage("Hast du schon mal versucht /verify <Discord ID> auf dem Commands Server auszuführen? Falls du deine Discord ID nicht weißt, mach ein Rechtsklick auf dein Namen und kopier sie.. " +
                            "Falls du sonst noch Probleme hast, kannst du gerne ein Teammitglied anschreiben.")
                            .addFile(ByteStreams.toByteArray(InstantVerify.class.getResourceAsStream("/Copy_ID.png")), "Copy_ID.png")
                            .queue();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        }


    }

}
