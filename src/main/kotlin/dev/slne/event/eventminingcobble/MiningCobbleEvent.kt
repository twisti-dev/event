package dev.slne.event.eventminingcobble

import com.github.retrooper.packetevents.PacketEvents
import dev.slne.event.eventminingcobble.decorations.GlobalCobbleCountBossbar
import dev.slne.event.eventminingcobble.command.EventStartCommand
import dev.slne.event.eventminingcobble.decorations.ScoreBoard
import dev.slne.event.eventminingcobble.listener.MiningListener
import dev.slne.event.eventminingcobble.manager.MiningManager
import dev.slne.event.eventminingcobble.player.MiningPlayer
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin

class MiningCobbleEvent : JavaPlugin() {
    lateinit var scoreboardLibrary: ScoreboardLibrary
    lateinit var scoreBoard: ScoreBoard
        private set

    override fun onLoad() {
        instance = this

        ConfigurationSerialization.registerClass(MiningPlayer::class.java)

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this))
        PacketEvents.getAPI().load()


    }

    override fun onEnable() {
        PacketEvents.getAPI().init()

        MiningPlayerManager
        EventStartCommand

        saveDefaultConfig()
        if (config.getBoolean("isRunning", false)) {
            continueFromConfig()
        }

        Bukkit.getScheduler().runTask(this, Runnable {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this)
            scoreBoard = ScoreBoard(this)
        })
    }

    override fun onDisable() {
        PacketEvents.getAPI().terminate()

        saveToConfig()

        scoreboardLibrary.close()
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