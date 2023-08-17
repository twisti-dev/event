package dev.slne.event.eventminingcobble.decorations

import dev.slne.event.eventminingcobble.MiningCobbleEvent
import dev.slne.event.eventminingcobble.manager.MiningManager
import dev.slne.event.eventminingcobble.messages.MessageManager
import dev.slne.event.eventminingcobble.player.MiningPlayer
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.CollectionSidebarAnimation
import net.megavex.scoreboardlibrary.api.sidebar.component.animation.SidebarAnimation
import org.bukkit.Bukkit
import org.bukkit.entity.Player


class ScoreBoard(
    plugin: MiningCobbleEvent,
    private val sidebar: Sidebar = plugin.scoreboardLibrary.createSidebar()
) {
    init {
        val titleAnimation = Component.text("Mining Cobble Event").createGradientAnimation("#1488CC", "#2B32B2")
        val title = SidebarComponent.animatedLine(titleAnimation)
        val spaceLine = Component.text("--------------------")
            .createGradientAnimation(MessageManager.WHITE.asHexString(), MessageManager.SPACER.asHexString())

        val lines = SidebarComponent.builder()
            .addBlankLine()
            .addDynamicLine { Component.text("Verbleibende Zeit: ${remainingTime.invoke()}", MessageManager.INFO) }
            .addBlankLine()
            .addAnimatedLine(spaceLine)
            .addBlankLine()
            .addDynamicLine(getPlaceComponentSupplier(1))
            .addDynamicLine(getPlaceComponentSupplier(2))
            .addDynamicLine(getPlaceComponentSupplier(3))
            .addDynamicLine(getPlaceComponentSupplier(4))
            .addDynamicLine(getPlaceComponentSupplier(5))
            .build()

        val sidebarLayout = ComponentSidebarLayout(
            title,
            lines
        )

        sidebar.addPlayers(Bukkit.getOnlinePlayers())

        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            titleAnimation.nextFrame()
            spaceLine.nextFrame()
            sidebarLayout.apply(sidebar)
            sidebar.addPlayers(Bukkit.getOnlinePlayers())
        }, 0, 5)

    }

    companion object {
        private val remainingTime: () -> String =
            { (MiningManager.EVENT_TIME_IN_SECONDS - MiningManager.timeElapsed).secondsToString() }
    }

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

    fun addPlayer(player: Player) {
        sidebar.addPlayer(player)
    }
}

private fun Component.createGradientAnimation(firstHex: String, secondHex: String): SidebarAnimation<Component> {
    val step = 1f / 20f
    val textPlaceholder = Placeholder.component("text", this)
    val frames = mutableListOf<Component>()

    // Animation from left to right
    var phase = -1f
    while (phase < 1) {
        frames.add(
            MiniMessage.miniMessage().deserialize("<gradient:${firstHex}:${secondHex}:$phase><text>", textPlaceholder)
        )
        phase += step
    }

    // Animation from right to left
    frames.addAll(frames.reversed())

    return CollectionSidebarAnimation(frames)
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