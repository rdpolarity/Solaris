import co.aikar.commands.BaseCommand
import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import engine.objects.LootChest
import engine.objects.SpeedBoost
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.info
import managers.MapManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author

@Plugin(name="Solaris", version = "1.0")
@ApiVersion(ApiVersion.Target.v1_16)
@Description("Decoupled minigame engine")
@Author("RDPolarity")
class Solaris : BukkitPlugin() {
    var test = ""

    override fun onEnable() {
        info("Engine Enabled!")

        MapManager.registerEvents(this)
        MapManager.addPrefab(
            LootChest(),
            SpeedBoost()
        )

        val commandManager = BukkitCommandManager(this)
        commandManager.registerCommand(Commands())
        commandManager.registerCommand(MapManager.Commands())
    }

    companion object {
        fun getPlugin(): org.bukkit.plugin.Plugin? {
            return Bukkit.getPluginManager().getPlugin(Solaris::class.simpleName!!)
        }
    }

    @CommandAlias("solaris|se|sol")
    @Command(
        name = "Solaris",
        desc = "Core command for solaris plugin",
        aliases = ["solaris", "se", "sol"],
        usage = "/<command>"
    )
    class Commands : BaseCommand() {

        @Default
        private fun onSolaris(player : Player) {
        }

        @Default
        @Subcommand("test")
        private fun onTest(player : Player) {
        }
    }
}