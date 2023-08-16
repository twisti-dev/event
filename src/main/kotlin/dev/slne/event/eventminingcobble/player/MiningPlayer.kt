package dev.slne.event.eventminingcobble.player

import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.configuration.serialization.SerializableAs
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@SerializableAs("MiningPlayer")
class MiningPlayer(
    private val uuid: UUID,
    private val name: String,
    private var cobbleMined: AtomicInteger = AtomicInteger(0)
) : Comparable<MiningPlayer>, ConfigurationSerializable{

    @Suppress("unused")
    val bukkitPlayer: Player?
        get() = Bukkit.getPlayer(uuid)

    fun reset() {
        cobbleMined.set(0)
    }

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
        return -compared
    }

    /**
     * Creates a Map representation of this class.
     *
     *
     * This class must provide a method to restore this class, as defined in
     * the [ConfigurationSerializable] interface javadocs.
     *
     * @return Map containing the current state of this class
     */
    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "uuid" to uuid.toString(),
            "name" to name,
            "cobbleMined" to cobbleMined.get()
        )
    }

    companion object {
        @Suppress("unused") // Used by Bukkit
        @JvmStatic
        @JvmName("deserialize")
        fun deserialize(map: Map<String, Any>): MiningPlayer {
            return MiningPlayer(
                UUID.fromString(map["uuid"] as String),
                map["name"] as String,
                AtomicInteger(map["cobbleMined"] as Int)
            )
        }
    }
}