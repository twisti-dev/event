package dev.slne.event.eventminingcobble

import dev.slne.event.eventminingcobble.bossbar.GlobalCobbleCountBossbar
import dev.slne.event.eventminingcobble.command.EventStartCommand
import dev.slne.event.eventminingcobble.listener.MiningListener
import dev.slne.event.eventminingcobble.manager.MiningManager
import dev.slne.event.eventminingcobble.player.MiningPlayer
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

class MiningCobbleEvent : JavaPlugin() {
    override fun onLoad() {
        instance = this

        ConfigurationSerialization.registerClass(MiningPlayer::class.java)

    }

    override fun onEnable() {
        MiningPlayerManager
        EventStartCommand

        saveDefaultConfig()
        if (config.getBoolean("isRunning", false)) {
            continueFromConfig()
        }
    }

    override fun onDisable() {
        saveToConfig()
    }

    fun saveToConfig() {
        config.set("isRunning", MiningManager.running)
        config.set("timeElapsed", MiningManager.timeElapsed)
        config.set("globalCobbleMined", MiningPlayerManager.globalCobbleMined.get())
        config.set("players", MiningPlayerManager.miningPlayerCache.asMap().values.toMutableList())
        saveConfig()
    }

    @Suppress("UNCHECKED_CAST")
    private fun continueFromConfig() {
        MiningManager.running = config.getBoolean("isRunning", false)
        MiningManager.timeElapsed = config.getInt("timeElapsed", -1)
        MiningPlayerManager.globalCobbleMined.set(config.getInt("globalCobbleMined", 0))
        MiningPlayerManager.miningPlayerCache.putAll((config.getList("players") as List<MiningPlayer>).associateBy { it.getUUID() })

        server.pluginManager.registerEvents(MiningListener, this)
        MiningManager.bossbar = GlobalCobbleCountBossbar(this)
        MiningManager.startTask()
    }

    companion object {
        @JvmStatic
        lateinit var instance: MiningCobbleEvent
            private set
    }
}