package de.luuuuuis.Bungee.Discord;

import de.luuuuuis.Bungee.InstantVerify;
import de.luuuuuis.Bungee.Minecraft.VerifyCommand;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.Discord
 * Date: 02.01.2019
 * Time 19:13
 */
class Events extends ListenerAdapter {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        JDA jda = e.getJDA();
        Guild guild = e.getGuild();

        if (e.isFromType(ChannelType.TEXT)) {
            Member member = e.getMember();
            Role group = guild.getRolesByName(InstantVerify.discordRole, true).get(0);
            if (group != null) {
                guild.getController().addSingleRoleToMember(guild.getMember(e.getAuthor()), group);
            }

        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        String message = e.getMessage().getContentRaw();

        if (message.equalsIgnoreCase(VerifyCommand.verifying.get(e.getAuthor().getName()))) {
            Role group = e.getJDA().getRolesByName(InstantVerify.discordRole, true).get(0);
            if (group != null) {
                group.getGuild().getController().addRolesToMember(group.getGuild().getMember(e.getAuthor()), group);
            }
        }


    }

}
