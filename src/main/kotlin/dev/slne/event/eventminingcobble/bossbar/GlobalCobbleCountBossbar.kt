package dev.slne.event.eventminingcobble.bossbar

import dev.slne.event.eventminingcobble.MiningCobbleEvent
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle

class GlobalCobbleCountBossbar(
    plugin: MiningCobbleEvent
) {
    private val bossBar = Bukkit.createBossBar("Progress", BarColor.GREEN, BarStyle.SOLID).apply {
        this.progress = (MiningPlayerManager.getGlobalCobbleMined().toDouble() / 500).coerceAtMost(1.0)
        this.isVisible = true
    }

    private val timer = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
        bossBar.progress = (MiningPlayerManager.getGlobalCobbleMined().toDouble() / 500).coerceAtMost(1.0)
        Bukkit.getOnlinePlayers().forEach {
            if (!bossBar.players.contains(it)) {
                bossBar.addPlayer(it)
            }
        }
    }, 0, 20)

    fun stop() {
        timer.cancel()
        bossBar.removeAll()
    }
}