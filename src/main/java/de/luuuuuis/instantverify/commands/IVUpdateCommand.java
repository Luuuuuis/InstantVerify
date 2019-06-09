/*
 * Developed by Luuuuuis on 04.05.19 23:14.
 * Last modified 04.05.19 22:16.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.commands;

import de.luuuuuis.instantverify.InstantVerify;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class IVUpdateCommand extends Command {

    private InstantVerify instantVerify;

    public IVUpdateCommand(String name, InstantVerify instantVerify) {
        super(name, "instantverify.admin", "IVUpdate");
        this.instantVerify = instantVerify;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {

        if (instantVerify.getUpdate().searchForUpdate()) {
            sender.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("update.restart"));

            if (sender instanceof ProxiedPlayer) {
                TextComponent msg = new TextComponent(instantVerify + "Restart now! §6§l[Click here]");
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Click to stop the bungeecord!").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/end"));

                sender.sendMessage(msg);
            }

        } else {
            sender.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("update.latest"));
        }
    }
}
