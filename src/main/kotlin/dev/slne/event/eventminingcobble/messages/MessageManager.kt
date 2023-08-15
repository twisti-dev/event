package dev.slne.event.eventminingcobble.messages

import dev.slne.event.eventminingcobble.player.MiningPlayer
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.OfflinePlayer


object MessageManager {
    val PRIMARY = TextColor.fromHexString("#3b92d1")
    val SECONDARY = TextColor.fromHexString("#5b5b5b")
    val TERTIARY = TextColor.fromHexString("#f2c94c")
    val VARIABLE_KEY = TextColor.fromHexString("#3b92d1")
    val VARIABLE_VALUE = TextColor.fromHexString("#f9c353")
    val SPACER: TextColor = NamedTextColor.GRAY
    val DARK_SPACER: TextColor = NamedTextColor.DARK_GRAY

    val ERROR = TextColor.fromHexString("#ee3d51")
    val SUCCESS = TextColor.fromHexString("#65ff64")
    val WARNING = TextColor.fromHexString("#f9c353")
    val INFO = TextColor.fromHexString("#40d1db")
    val DEBUG = TextColor.fromHexString("#a6c7e6")

    val WHITE = TextColor.fromHexString("#f4f4f4")
    val BLACK = TextColor.fromHexString("#000000")
    val DARK_GRAY: TextColor = NamedTextColor.DARK_GRAY
    val GRAY: TextColor = NamedTextColor.GRAY
    val DARK_RED: TextColor = NamedTextColor.DARK_RED
    val RED: TextColor = NamedTextColor.RED
    val DARK_GREEN: TextColor = NamedTextColor.DARK_GREEN
    val GREEN: TextColor = NamedTextColor.GREEN
    val DARK_AQUA: TextColor = NamedTextColor.DARK_AQUA
    val AQUA: TextColor = NamedTextColor.AQUA
    val DARK_BLUE: TextColor = NamedTextColor.DARK_BLUE
    val BLUE: TextColor = NamedTextColor.BLUE
    val DARK_PURPLE: TextColor = NamedTextColor.DARK_PURPLE
    val LIGHT_PURPLE: TextColor = NamedTextColor.LIGHT_PURPLE
    val GOLD: TextColor = NamedTextColor.GOLD
    val YELLOW: TextColor = NamedTextColor.YELLOW

    val prefix: Component = Component.text(">> ", DARK_SPACER)
        .append(Component.text("Cobble", PRIMARY))
        .append(Component.text(" | ", DARK_SPACER))

    val line: Component = Component.newline().append(prefix)

    fun startMessage(): Component {
        return line
            .append(line)
            .append(line)
            .append(
                Component.text("Herzlich Willkommen zum ", VARIABLE_VALUE)
                    .append(Component.text("Cobble-Mining-Event", VARIABLE_KEY))
                    .append(line)
                    .append(line)
                    .append(line)
                    .append(line)
                    .append(
                        Component.text("Wichtig: ", ERROR)
                            .append(line)
                            .append(Component.text("Jeder muss die Nachricht im Discord gelesen haben!", WARNING))
                    )
            )
            .append(line)
            .append(line)
            .append(Component.text("Das Event startet jetzt!", SUCCESS))
    }

    fun stopMessage(): Component {
        val miningPlayer = MiningPlayerManager.getSortedMiningPlayers()
        val first: MiningPlayer? = miningPlayer.getOrNull(0)
        val second: MiningPlayer? = miningPlayer.getOrNull(1)
        val third: MiningPlayer? = miningPlayer.getOrNull(2)

        return line
            .append(line)
            .append(line)
            .append(
                Component.text("Das Event ist vorbei!", ERROR)
                    .append(line)
                    .append(line)
                    .append(line)
                    .append(line)
                    .append(
                        Component.text("Platz 1: ", SECONDARY)
                            .append(Component.text(first?.let { "${it.getName()} (${it.getCobbleMined()})" }
                                ?: "__EMPTY__", VARIABLE_VALUE))
                            .append(line)
                            .append(Component.text("Platz 2: ", SECONDARY))
                            .append(Component.text(second?.let { "${it.getName()} (${it.getCobbleMined()})" }
                                ?: "__EMPTY__", VARIABLE_VALUE))
                            .append(line)
                            .append(Component.text("Platz 3: ", SECONDARY))
                            .append(Component.text(third?.let { "${it.getName()} (${it.getCobbleMined()})" }
                                ?: "__EMPTY__", VARIABLE_VALUE))
                    )
            )
            .append(line)
            .append(line)
            .append(Component.text("Vielen Dank fürs Mitmachen!", SUCCESS))
            .append(line)
            .append(
                Component.text("Es wurden ", INFO)
                    .append(Component.text("${MiningPlayerManager.getGlobalCobbleMined()} / 500.000", VARIABLE_VALUE))
                    .append(Component.text(" Cobble abgebaut!", INFO))
            )
    }

    fun showGlobalCobbleMined(): Component {
        return prefix
            .append(
                Component.text("Es wurden ", INFO)
                    .append(Component.text("${MiningPlayerManager.getGlobalCobbleMined()} / 500.000", VARIABLE_VALUE))
                    .append(Component.text(" Cobble abgebaut!", INFO))
            )
    }

    fun noMiningPlayerFound(offlinePlayer: OfflinePlayer) : Component{
        val builder = Component.text()

        builder.append(prefix)
        builder.append(Component.text("Der Spieler ", ERROR))
        offlinePlayer.name?.let { builder.append(Component.text(it, VARIABLE_VALUE)) }
        builder.append(Component.text(" wurde nicht gefunden!", ERROR))

        return builder.build()
    }

    fun showPlayerCobbleMined(player: MiningPlayer): Component {
        return prefix
            .append(Component.text(player.getName(), VARIABLE_KEY))
            .append(Component.text(" hat ", INFO))
            .append(Component.text("${player.getCobbleMined()} / 500.000", VARIABLE_VALUE))
            .append(Component.text(" Cobble abgebaut!", INFO))
    }
}