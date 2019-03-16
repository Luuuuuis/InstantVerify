/*
 * Developed by Luuuuuis on 16.03.19 19:32.
 * Last modified 16.03.19 19:31.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.Events;

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
