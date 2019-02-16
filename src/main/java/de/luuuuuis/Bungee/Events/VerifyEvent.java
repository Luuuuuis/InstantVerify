package de.luuuuuis.Bungee.Events;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

/**
 * Author: Luuuuuis
 * Project: InstantVerify
 * Package: de.luuuuuis.Bungee.Events
 * Date: 16.02.2019
 * Time 20:23
 */
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
