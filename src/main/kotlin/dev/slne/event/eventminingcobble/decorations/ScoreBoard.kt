package dev.slne.event.eventminingcobble.decorations

import dev.slne.event.eventminingcobble.MiningCobbleEvent
import dev.slne.event.eventminingcobble.manager.MiningManager
import dev.slne.event.eventminingcobble.messages.MessageManager
import dev.slne.event.eventminingcobble.player.MiningPlayer
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent
import org.bukkit.Bukkit

class ScoreBoard(
    plugin: MiningCobbleEvent,
    private val sidebar: Sidebar = plugin.scoreboardLibrary.createSidebar()
) {
    init {
        sidebar.title(Component.text("Mining Cobble Event", MessageManager.PRIMARY))

        val lines = SidebarComponent.builder()
            .addDynamicLine { Component.text("Zeit: ${remainingTime.invoke()}", MessageManager.INFO) }
            .addBlankLine()
            .addDynamicLine(getPlaceComponentSupplier(1))
            .addDynamicLine(getPlaceComponentSupplier(2))
            .addDynamicLine(getPlaceComponentSupplier(3))
            .addDynamicLine(getPlaceComponentSupplier(4))
            .addDynamicLine(getPlaceComponentSupplier(5))
            .build()

        val sidebarLayout = ComponentSidebarLayout(
            SidebarComponent.dynamicLine { Component.text("Mining Cobble Event", MessageManager.PRIMARY) },
            lines
        )

        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            sidebarLayout.apply(sidebar)
            sidebar.addPlayers(Bukkit.getOnlinePlayers())
        }, 0, 20)

    }

    private val remainingTime: () -> String = { (MiningManager.EVENT_TIME_IN_SECONDS - MiningManager.timeElapsed).secondsToString() }

    private fun getPlayerAtPlace(place: Int): MiningPlayer? {
        return MiningPlayerManager.getSortedMiningPlayers().getOrNull(place - 1)
    }

    private fun getPlaceComponentSupplier(place: Int): () -> TextComponent {
        return {
            val playerAtPlace = getPlayerAtPlace(place)

            playerAtPlace?.getName()?.let {
                Component.text(it, MessageManager.VARIABLE_KEY)
                    .append(Component.text(" (${playerAtPlace.getCobbleMined()})", MessageManager.VARIABLE_VALUE))
            } ?: Component.empty()
        }
    }
}

fun Int.secondsToString(): String {
    if (this < 60) return "${this}s"

    val minutes = this / 60
    if (minutes < 60) return "${minutes}m ${this % 60}s"

    val hours = minutes / 60
    if (hours < 24) return "${hours}h ${minutes % 60}m ${this % 60}s"

    val days = hours / 24
    return "${days}d ${hours % 24}h ${minutes % 60}m ${this % 60}s"
}