/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Commands;

import de.luuuuuis.InstantVerify.InstantVerify;
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
            sender.sendMessage(instantVerify.getPrefix() + "§6Ein Update wurde heruntergeladen. Bitte starte den Proxy neu!");
        } else {
            sender.sendMessage(instantVerify.getPrefix() + "§aDu hast bereits die neuste Version!");
        }
    }
}
