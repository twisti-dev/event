package dev.slne.event.eventminingcobble.command

import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.offlinePlayerArgument
import dev.slne.event.eventminingcobble.manager.MiningManager
import dev.slne.event.eventminingcobble.messages.MessageManager
import dev.slne.event.eventminingcobble.player.MiningPlayerManager
import org.bukkit.OfflinePlayer

object EventStartCommand {
    init {
        commandTree("cobbleEvent") {
            withPermission("cobbleEvent.start")

            literalArgument("count") {
                executes(CommandExecutor { sender, _ ->
                    sender.sendMessage(MessageManager.showGlobalCobbleMined())
                })

                offlinePlayerArgument("offlinePlayer") {
                    executes(CommandExecutor { sender, args ->
                        val player = args["offlinePlayer"] as OfflinePlayer
                        val miningPlayer = MiningPlayerManager.getMiningPlayerIfPresent(player.uniqueId)

                        miningPlayer?.let {
                            sender.sendMessage(MessageManager.showPlayerCobbleMined(it))
                        }?: sender.sendMessage(MessageManager.noMiningPlayerFound(player))
                    })
                }
            }

            literalArgument("start") {
                withPermission("cobbleEvent.start")
                executes(CommandExecutor { sender, _ ->
                    MiningManager.start()
                    sender.sendMessage("Event started!")
                })
            }

            literalArgument("stop") {
                withPermission("cobbleEvent.stop")
                executes(CommandExecutor { sender, _ ->
                    MiningManager.stop()
                    sender.sendMessage("Event stopped!")
                })
            }
        }
    }
}