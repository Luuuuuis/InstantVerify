/*
 * Developed by Luuuuuis on 09.04.19 15:00.
 * Last modified 09.04.19 14:56.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Spigot.Discord;

import de.luuuuuis.Spigot.Commands.VerifyCommand;
import de.luuuuuis.Spigot.InstantVerify;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

class Events extends ListenerAdapter {

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
                e.getChannel().sendMessage("Hast du schon mal versucht /verify <Discord ID> auf dem Commands Server auszuführen? Falls du deine Discord ID nicht weißt, mach ein Rechtsklick auf dein Namen und kopier sie.. " +
                        "Falls du sonst noch Probleme hast, kannst du gerne ein Teammitglied anschreiben.")
                        .queue();
            });
        }


    }

}
