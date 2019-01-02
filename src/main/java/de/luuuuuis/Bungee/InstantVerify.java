package de.luuuuuis.Bungee;

import net.md_5.bungee.api.plugin.Plugin;

/**
 * 
 */
public class InstantVerify extends Plugin {

    public static InstantVerify instance;


    @Override
    public void onEnable() {
        super.onEnable();

    }

    public static InstantVerify getInstance() {
        return instance;
    }
}
