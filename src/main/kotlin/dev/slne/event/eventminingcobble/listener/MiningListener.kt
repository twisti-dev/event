package dev.slne.event.eventminingcobble.listener

import dev.slne.event.eventminingcobble.manager.MiningManager
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerJoinEvent

object MiningListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        MiningPlayerManager.getMiningPlayer(player.uniqueId)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val type = event.block.type
        if (type == Material.STONE || type == Material.COBBLESTONE) {
            val player = event.player
            val miningPlayer = MiningPlayerManager.getMiningPlayer(player.uniqueId)

            miningPlayer.addCobbleMined()

            if (MiningPlayerManager.getGlobalCobbleMined() >= 500_000) {
                MiningManager.stop()
                return
            }

            player.sendActionBar(Component.text("Cobble Mined: ${miningPlayer.getCobbleMined()}", NamedTextColor.GREEN))
        }
    }
}