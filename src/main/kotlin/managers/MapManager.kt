package managers

import GUI.SolarisGUI
import extentions.asComponent
import extentions.isGameObject
import engine.GameObject
import Solaris
import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import data.Constants
import de.slikey.effectlib.util.MathUtils
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTItem
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.PaginatedGui
import extentions.broadcast
import hazae41.minecraft.kutils.bukkit.listen
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.util.Vector


object MapManager {

    private val prefabs : MutableList<GameObject> = mutableListOf()

    class PrefabGUI : SolarisGUI("Prefab Selector") {
        override fun onOpen(player: Player) {
            prefabs.forEach {
                gui.addItem(it.asItem())
            }
        }
    }

    class ObjectsGUI : SolarisGUI("Active Objects") {
        override fun onOpen(player: Player) {
            GlobalDataManager.getData(player).locations.forEach { loc ->
                getObjectAt(Location(player.world, loc.x, loc.y, loc.z))?.let {
                    gui.addItem(it.asItem())
                }
            }
        }
    }

    private fun placePrefab(event: BlockPlaceEvent) {
        val itemNBT = NBTItem(event.itemInHand)
        val solarisData = itemNBT.getObject(Constants.NBT.SOLARIS_KEY, GameObject.NBTData::class.java)
        prefabs.findLast { it.type == solarisData.type }?.placeObject(event)
    }

    private fun getObjectAt(location: Location): GameObject? {
        val block = location.block
        if (block.type != Material.PLAYER_HEAD) return null
        if (!block.isGameObject()) return null
        val itemNBT = NBTBlock(block)
        val solarisData = itemNBT.data.getObject(Constants.NBT.SOLARIS_KEY, GameObject.NBTData::class.java)
        return prefabs.findLast { it.type == solarisData.type }
    }

    private fun editPrefab(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.hand == EquipmentSlot.HAND) {
            event.clickedBlock?.let {
                getObjectAt(it.location)?.editObject(event)
            }
        }
    }

    fun addPrefab(vararg objects : GameObject) {
        objects.forEach {
            prefabs.add(it)
        }
    }

    fun registerEvents(solaris: Solaris) {
        solaris.listen<BlockBreakEvent> { if (it.block.isGameObject()) it.isCancelled = true }
        solaris.listen<PlayerInteractEvent> { editPrefab(it) }
        solaris.listen<BlockPlaceEvent> { if (it.itemInHand.isGameObject()) placePrefab(it) }
    }

    @CommandAlias("map")
    @Command(
        name = "Map",
        desc = "Map related command suite",
        aliases = ["map"],
        usage = "/<command>"
    )
    class Commands : BaseCommand() {
        @Default
        fun onMap(player : Player) {

        }

        @Subcommand("prefabs")
        fun onPrefabs(player: Player) { PrefabGUI().open(player) }

        @Subcommand("object")
        fun onObjects(player: Player) { ObjectsGUI().open(player) }

        @Subcommand("data")
        fun onData(player: Player) {
            GlobalDataManager.getData(player).toString().broadcast()
        }

        @Subcommand("dataAdd")
        fun onDataAdd(player: Player) {
            GlobalDataManager.addLocation(player, Vector(1,1,1))
//            GlobalDataManager.addLocation(player, Vector(MathUtils.random(0,10),MathUtils.random(0,10),MathUtils.random(0,10)))
        }

        @Subcommand("dataClear")
        fun onDataClear(player: Player) {
            GlobalDataManager.resetData(player)
        }
    }
}