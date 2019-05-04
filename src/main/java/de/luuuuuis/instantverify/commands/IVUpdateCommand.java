/*
 * Developed by Luuuuuis on 04.05.19 23:14.
 * Last modified 04.05.19 22:16.
 * Copyright (c) 2019.
 */

package de.luuuuuis.instantverify.commands;

import de.luuuuuis.instantverify.InstantVerify;
import net.md_5.bungee.api.CommandSender;
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
        } else {
            sender.sendMessage(instantVerify.getPrefix() + instantVerify.getLangConfig().getMessages().get("update.latest"));
        }
    }
}
