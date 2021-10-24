package commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.entity.Player
import org.bukkit.plugin.java.annotation.command.Command

@CommandAlias("solaris|se|sol")
@Command(
    name = "Solaris",
    desc = "Core command for solaris plugin",
    aliases = ["solaris", "se", "sol"],
    usage = "/<command>"
)
class SolarisCommand : BaseCommand() {

    @Default
    @Description("Debug command for Solaris")
    private fun onSolaris(player : Player) {
    }

    @Default
    @Subcommand("test")
    @Description("Debug command for Solaris")
    private fun onTest(player : Player) {
    }
}