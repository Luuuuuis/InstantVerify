/*
 * Developed by Luuuuuis on 08.04.19 21:14.
 * Last modified 08.04.19 21:14.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.Commands;

import de.luuuuuis.Bungee.InstantVerify;
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
