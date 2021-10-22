import co.aikar.commands.BukkitCommandManager
import hazae41.minecraft.kutils.bukkit.*
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author

@Plugin(name="Solaris", version = "1.0")
@ApiVersion(ApiVersion.Target.v1_17)
@Description("Decoupled minigame engine")
@Author("RDPolarity")
object Solaris : BukkitPlugin() {
    override fun onEnable() {
        info("Engine Enabled!")

        listen<PlayerJoinEvent>{
            it.player.msg("Welcome ${it.player.name} from Solaris")
        }

        val commandManager = BukkitCommandManager(this)
        commandManager.registerCommand(Commands())

        command("hello"){ sender, args ->
            if(args.isEmpty())
                sender.msg("&cWrong arguments, usage: $usage")
            else sender.msg("&bHello!")
        }
    }
}