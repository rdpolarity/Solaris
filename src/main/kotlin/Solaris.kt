import co.aikar.commands.BukkitCommandManager
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import commands.SolarisCommand
import hazae41.minecraft.kutils.bukkit.*
import managers.MapManager
import org.bukkit.Bukkit
//import LootChest
//import kit.SpeedBoost
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
        commandManager.registerCommand(SolarisCommand())
        commandManager.registerCommand(MapManager.Commands())
    }

    companion object {
        fun getPlugin(): org.bukkit.plugin.Plugin? {
            return Bukkit.getPluginManager().getPlugin(Solaris::class.simpleName!!)
        }
    }
}