package de.luuuuuis.Bungee.Discord;

import com.google.common.io.ByteStreams;
import de.luuuuuis.Bungee.Events.VerifyEvent;
import de.luuuuuis.Bungee.InstantVerify;
import de.luuuuuis.Bungee.Minecraft.VerifyCommand;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;

import java.io.IOException;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.Discord
 * Date: 02.01.2019
 * Time 19:13
 */
class Events extends ListenerAdapter {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        if (e.getAuthor().isBot()) return;
        String message = e.getMessage().getContentDisplay();

        if (VerifyCommand.verifying.containsKey(e.getAuthor().getName())) {
            if (message.equalsIgnoreCase(VerifyCommand.verifying.get(e.getAuthor().getName()))) {
                VerifyEvent verifyEvent = new VerifyEvent();
                ProxyServer.getInstance().getPluginManager().callEvent(verifyEvent);
                if (!verifyEvent.isCancelled()) {
                    e.getJDA().getGuilds().forEach(guilds -> guilds.getController().addRolesToMember(guilds.getMember(e.getAuthor()), e.getJDA().getRolesByName(InstantVerify.discordRole, true)).complete());
                    e.getAuthor().openPrivateChannel().queue(channel -> {
                        e.getChannel().sendMessage("Thanks for verifying. See you on the Discord!").queue();
                    });
                    VerifyCommand.verifying.remove(e.getAuthor().getName());
                }
            } else {
                e.getAuthor().openPrivateChannel().queue(channel -> {
                    e.getChannel().sendMessage("Opps... Maybe wrong Credentials? Try again!").queue();
                });
            }
        } else {
            e.getAuthor().openPrivateChannel().queue(channel -> {
                try {
                    e.getChannel().sendMessage("Have you ever tried running /verify <Discord ID> on our Minecraft server? If so, please try again. To see your Discord ID right click on your name and then copy ID. " +
                            "If that doesn't solve your problem, please contact a supporter on our discord.")
                            .addFile(ByteStreams.toByteArray(InstantVerify.class.getResourceAsStream("/Copy_ID.png")), "Copy_ID.png")
                            .queue();
                    e.getChannel().sendMessage("To find out the TeamSpeak Unique ID click [> Tools > Indentities]. Then select your identity. Most of the time this is \"Default\". Then copy the unique ID.")
                            .addFile(ByteStreams.toByteArray(InstantVerify.class.getResourceAsStream("/UniqueID.png")), "UniqueID.png")
                            .queue();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
        }




    }

}
