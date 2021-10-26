import co.aikar.commands.BaseCommand
import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import dev.triumphteam.gui.builder.item.ItemBuilder
import engine.GameObject
import engine.objects.LootChest
import engine.objects.SpeedBoost
import extentions.asComponent
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.info
import hazae41.minecraft.kutils.bukkit.listen
import hazae41.minecraft.kutils.bukkit.msg
import managers.MapManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import java.awt.TextComponent

@Plugin(name="Solaris", version = "1.0")
@ApiVersion(ApiVersion.Target.v1_16)
@Description("Decoupled minigame engine")
@Author("RDPolarity")
class Solaris : BukkitPlugin() {
    var test = ""

    override fun onEnable() {
        info("Engine Enabled!")

        MapManager.registerEvents(this)
        MapManager.addPrefab(LootChest(),SpeedBoost())

        val commandManager = BukkitCommandManager(this).apply {
            registerCommand(Commands())
            registerCommand(MapManager.Commands())
        }

        // Debug Stick Logic
        listen<PlayerInteractEvent> {
            if (it.hand != EquipmentSlot.HAND) return@listen
            if (it.player.inventory.itemInMainHand.type != Material.DEBUG_STICK) return@listen
            val nbtData = NBTBlock(it.clickedBlock).data.toString()
            it.player.msg(nbtData)
        }
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
        @Subcommand("debug")
        private fun onTest(player : Player) {
            val debugStick = ItemBuilder.from(Material.DEBUG_STICK).asGuiItem().itemStack
            player.inventory.addItem(debugStick)
        }
    }
}