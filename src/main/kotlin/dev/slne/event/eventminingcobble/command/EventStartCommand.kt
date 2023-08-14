package dev.slne.event.eventminingcobble.command

import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.slne.event.eventminingcobble.manager.MiningManager

object EventStartCommand {
    init {
        commandTree("cobbleEvent") {
            withPermission("cobbleEvent.start")

            literalArgument("start") {
                executesPlayer(PlayerCommandExecutor { player, _ ->
                    MiningManager.start()
                    player.sendMessage("Event started!")
                })
            }

            literalArgument("stop") {
                executesPlayer(PlayerCommandExecutor { player, _ ->
                    MiningManager.stop()
                    player.sendMessage("Event stopped!")
                })
            }
        }
    }
}