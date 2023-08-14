package dev.slne.event.eventminingcobble

import dev.slne.event.eventminingcobble.command.EventStartCommand
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import org.bukkit.plugin.java.JavaPlugin

class MiningCobbleEvent : JavaPlugin() {
    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        MiningPlayerManager
        EventStartCommand
    }

    override fun onDisable() {

    }

    companion object {
        lateinit var plugin: MiningCobbleEvent
            private set
    }
}