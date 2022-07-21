package me.rdpolarity.solaris

import me.rdpolarity.solaris.annotations.GamePrefab
import co.aikar.commands.BaseCommand
import co.aikar.commands.BukkitCommandManager
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import de.tr7zw.nbtapi.NBTBlock
import dev.triumphteam.gui.builder.item.ItemBuilder
import me.rdpolarity.solaris.engine.GameObject
import hazae41.minecraft.kutils.bukkit.BukkitPlugin
import hazae41.minecraft.kutils.bukkit.info
import hazae41.minecraft.kutils.bukkit.listen
import hazae41.minecraft.kutils.bukkit.msg
import io.github.classgraph.ClassGraph
import me.rdpolarity.solaris.managers.MapManager
import me.rdpolarity.solaris.managers.StateManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.dependency.SoftDependency
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Description
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
//
//@Plugin(name="Solaris", version = "1.0")
//@ApiVersion(ApiVersion.Target.v1_19)
//@SoftDependency("HolographicDisplays")
//@Description("Decoupled minigame engine")
//@Author("RDPolarity")
class Solaris : BukkitPlugin() {
    var test = ""

    override fun onEnable() {
        info("Engine Enabled!!!!")
        val prefabs = getAllAnnotatedWith(GamePrefab::class).map { it.createInstance() as GameObject }
        MapManager.addPrefab(prefabs)
        MapManager.onEnable(this)

        val commandManager = BukkitCommandManager(this).apply {
            registerCommand(Commands())
            registerCommand(MapManager.Commands())
            registerCommand(StateManager.Commands())
        }

        // Debug Stick Logic
        listen<PlayerInteractEvent> {
            if (it.hand != EquipmentSlot.HAND) return@listen
            if (it.player.inventory.itemInMainHand.type != Material.STICK) return@listen
            val nbtData = NBTBlock(it.clickedBlock).data.toString()
            it.player.msg(nbtData)
        }
    }

    @Throws(Exception::class)
    fun getAllAnnotatedWith(annotation: KClass<out Annotation>): List<KClass<*>> {
        val annotationName = annotation.java.canonicalName

        return ClassGraph()
            .enableAllInfo()
            .scan().use { scanResult ->
                scanResult.getClassesWithAnnotation(annotationName).map {
                    it.loadClass().kotlin
                }
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
            player.msg("Solaris is cool!!!!")
        }

        @Default
        @Subcommand("debug")
        private fun onTest(player : Player) {
            val debugStick = ItemBuilder.from(Material.STICK).asGuiItem().itemStack
            player.inventory.addItem(debugStick)
        }
    }
}