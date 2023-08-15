package dev.slne.event.eventminingcobble.player

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object MiningPlayerManager {
    val miningPlayerCache: LoadingCache<UUID, MiningPlayer> = Caffeine.newBuilder()
        .build { id -> MiningPlayer(id, Bukkit.getPlayer(id)!!.name) }

    val globalCobbleMined: AtomicInteger = AtomicInteger(0)

    fun getMiningPlayer(uuid: UUID): MiningPlayer {
        return miningPlayerCache.get(uuid)
    }

    fun getGlobalCobbleMined(): Int {
        return globalCobbleMined.get()
    }

    fun addGlobalCobbleMined(): Int {
        return globalCobbleMined.incrementAndGet()
    }

    fun getSortedMiningPlayers(): List<MiningPlayer> {
        return miningPlayerCache.asMap().values.sorted()
    }

    fun reset() {
        globalCobbleMined.set(0)
        miningPlayerCache.asMap().values.forEach { it.reset() }
    }
}