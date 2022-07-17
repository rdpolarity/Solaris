package managers

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import extentions.broadcast
import mobx.core.action
import mobx.core.observable
import org.bukkit.plugin.java.annotation.command.Command

object StateManager {
    enum class STATE {
        EDIT, // Runs Gizmos
        PLAY, // Removes Skulls, Runs Methods
        DEBUG // Removes Skulls, Runs Methods, Runs Gizmos
    }

    var state by observable(STATE.EDIT)

    fun change(state: STATE) = action {
        this.state = state
    }

    @CommandAlias("state")
    @Command(
        name = "State",
        desc = "Changes the state of the plugin",
        aliases = ["state", "st"],
        usage = "/<command>"
    )
    class Commands : BaseCommand() {
        @Default
        fun onState() {

        }

        @Subcommand("edit")
        fun onEdit() {
            change(STATE.EDIT)
            "changed to edit mode".broadcast()
        }

        @Subcommand("play")
        fun onPlay() {
            change(STATE.PLAY)
            "changed to play mode".broadcast()
        }

        @Subcommand("debug")
        fun onDebug() {
            change(STATE.DEBUG)
            "changed to debug mode".broadcast()
        }
    }
}