package dev.slne.event.eventminingcobble.player

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

class MiningPlayer(
    private val uuid: UUID,
    private val name: String,
    private var cobbleMined: AtomicInteger = AtomicInteger(0)
) : Comparable<MiningPlayer> { // TODO: Save every 5 minutes

    val bukkitPlayer: Player?
        get() = Bukkit.getPlayer(uuid)

    fun getUUID(): UUID {
        return uuid
    }

    fun getName(): String {
        return name
    }

    fun getCobbleMined(): Int {
        return cobbleMined.get()
    }

    fun addCobbleMined(): Int {
        MiningPlayerManager.addGlobalCobbleMined()
        return cobbleMined.incrementAndGet()
    }

    /**
     * Compares this object with the specified object for order. Returns zero if this object is equal
     * to the specified [other] object, a negative number if it's less than [other], or a positive number
     * if it's greater than [other].
     */
    override fun compareTo(other: MiningPlayer): Int {
        val compared = this.getCobbleMined().compareTo(other.getCobbleMined())

        if (compared == 0) {
            return this.getName().compareTo(other.getName())
        }
        return compared
    }
}