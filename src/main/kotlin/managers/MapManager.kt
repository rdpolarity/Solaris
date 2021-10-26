package managers

import extentions.asComponent
import extentions.isGameObject
import engine.GameObject
import Solaris
import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTItem
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.PaginatedGui
import hazae41.minecraft.kutils.bukkit.listen
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.annotation.command.Command


/*

 */
object MapManager {

    private val prefabs : MutableList<GameObject> = mutableListOf()
    private val prefabGUI : PaginatedGui = Gui.paginated()
        .title(Component.text("Prefab Selector"))
        .rows(6)
        .create()

    init {
        prefabGUI.setItem(6,3,ItemBuilder
            .from(Material.PAPER)
            .name("Previous".asComponent())
            .asGuiItem() {
            it.isCancelled = true
            prefabGUI.previous()
        } )

        prefabGUI.setItem(6,7,ItemBuilder
            .from(Material.PAPER)
            .name("Next".asComponent())
            .asGuiItem() {
            it.isCancelled = true
            prefabGUI.next()
        })
    }

    private fun placePrefab(event: BlockPlaceEvent) {
        val itemNBT = NBTItem(event.itemInHand)
        val solarisData = itemNBT.getObject(Constants.NBT.SOLARIS_KEY, GameObject.NBTData::class.java)
        prefabs.findLast { it.type == solarisData.type }?.placeObject(event)
    }

    private fun editPrefab(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.hand == EquipmentSlot.HAND) {
            val block = event.clickedBlock
            if (block?.type != Material.PLAYER_HEAD) return
            if (!block.isGameObject()) return
            val itemNBT = NBTBlock(block)
            val solarisData = itemNBT.data.getObject(Constants.NBT.SOLARIS_KEY, GameObject.NBTData::class.java)
            prefabs.findLast { it.type == solarisData.type }?.editObject(event)
        }
    }

    fun addPrefab(vararg objects : GameObject) {
        objects.forEach {
            prefabs.add(it)
            prefabGUI.addItem(it.asItem())
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

        @Subcommand("object")
        inner class Objects : BaseCommand() {
            @Default
            @Subcommand("get")
            fun onGet(player: Player) { prefabGUI.open(player) }
        }
    }
}