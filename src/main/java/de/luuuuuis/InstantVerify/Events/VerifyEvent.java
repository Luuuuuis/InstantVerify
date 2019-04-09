/*
 * Developed by Luuuuuis on 09.04.19 19:55.
 * Last modified 09.04.19 19:50.
 * Copyright (c) 2019.
 */

package de.luuuuuis.InstantVerify.Events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class VerifyEvent extends Event implements Cancellable {

    private boolean isCancelled;

    public VerifyEvent() {
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
