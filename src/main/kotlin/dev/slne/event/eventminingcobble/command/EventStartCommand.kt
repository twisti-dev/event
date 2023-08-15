package dev.slne.event.eventminingcobble.command

import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.slne.event.eventminingcobble.manager.MiningManager

object EventStartCommand {
    init {
        commandTree("cobbleEvent") {
            withPermission("cobbleEvent.start")

            literalArgument("start") {
                executes(CommandExecutor { sender, _ ->
                    MiningManager.start()
                    sender.sendMessage("Event started!")
                })
            }

            literalArgument("stop") {
                executes(CommandExecutor { sender, _ ->
                    MiningManager.stop()
                    sender.sendMessage("Event stopped!")
                })
            }
        }
    }
}