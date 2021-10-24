package managers

import Extensions.asComponent
import Extensions.isGameObject
import GameObject
import Solaris
import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.PaginatedGui
import hazae41.minecraft.kutils.bukkit.listen
import hazae41.minecraft.kutils.bukkit.msg
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.annotation.command.Command


/*

 */
object MapManager {

    private val prefabs : List<GameObject> = mutableListOf()
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
        event.player.msg("You placed a game objects")
    }

    fun addPrefab(vararg objects : GameObject) {
        objects.forEach {
            prefabs.toMutableList().add(it)
            prefabGUI.addItem(it.asItem())
        }
    }

    fun registerEvents(solaris: Solaris) {
        solaris.listen<BlockPlaceEvent> { if (it.block.isGameObject()) placePrefab(it) }
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