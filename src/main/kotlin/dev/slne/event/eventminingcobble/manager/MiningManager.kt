package dev.slne.event.eventminingcobble.manager

import dev.slne.event.eventminingcobble.MiningCobbleEvent
import dev.slne.event.eventminingcobble.decorations.GlobalCobbleCountBossbar
import dev.slne.event.eventminingcobble.listener.MiningListener
import dev.slne.event.eventminingcobble.messages.MessageManager
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList

object MiningManager {
    const val EVENT_TIME_IN_SECONDS: Short = 3_600
    var bossbar: GlobalCobbleCountBossbar? = null
    private val plugin = MiningCobbleEvent.instance
    var running = false

    private var taskID = -1

    var timeElapsed: Int = -1

    fun start() {
        if (running) return

        Bukkit.broadcast(MessageManager.startMessage())
        running = true
        startTask()

        plugin.server.pluginManager.registerEvents(MiningListener, plugin)
        bossbar = GlobalCobbleCountBossbar(plugin)
    }

    fun startTask() {
        taskID = plugin.server.scheduler.runTaskTimer(
            plugin,
            Runnable {
                if (timeElapsed++ >= EVENT_TIME_IN_SECONDS) {
                    stop()
                }
            },
            0, 20
        ).taskId
    }

    fun stop() {
        if (!running) return

        Bukkit.broadcast(MessageManager.stopMessage())

        running = false
        val plugin = MiningCobbleEvent.instance

        MiningPlayerManager.getSortedMiningPlayers().forEach {
            plugin.componentLogger.info(Component.text("${it.getName()} mined ${it.getCobbleMined()} cobblestone", MessageManager.INFO))
        }

        plugin.server.scheduler.cancelTask(taskID)
        HandlerList.unregisterAll(MiningListener)
        bossbar?.stop()
        bossbar = null
        timeElapsed = -1

        MiningPlayerManager.reset()
    }
}