package dev.slne.event.eventminingcobble.manager

import dev.slne.event.eventminingcobble.MiningCobbleEvent
import dev.slne.event.eventminingcobble.bossbar.GlobalCobbleCountBossbar
import dev.slne.event.eventminingcobble.listener.MiningListener
import dev.slne.event.eventminingcobble.messages.MessageManager
import org.bukkit.Bukkit
import java.time.Duration

object MiningManager {
    private var bossbar: GlobalCobbleCountBossbar? = null

    private var running = false
    private var taskID = -1

    fun start() {
        if (running) return

        Bukkit.broadcast(MessageManager.startMessage())

        running = true
        val plugin = MiningCobbleEvent.plugin

        taskID = plugin.server.scheduler.runTaskLater(
            plugin,
            Runnable { stop() },
            Duration.ofHours(1).toSeconds() * 20
        ).taskId

        plugin.server.pluginManager.registerEvents(MiningListener, plugin)
        bossbar = GlobalCobbleCountBossbar(plugin)
    }

    fun stop() {
        if (!running) return

        running = false
        val plugin = MiningCobbleEvent.plugin

        plugin.server.scheduler.cancelTask(taskID)
        plugin.server.pluginManager.registerEvents(MiningListener, plugin)
        bossbar?.stop()

        Bukkit.broadcast(MessageManager.stopMessage())
    }
}