package GUI

import extentions.asComponent
import extentions.setBasicScrollTemplate
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.ScrollingGui
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType

open class SolarisGUI(val title: String = "") {
    protected val gui: ScrollingGui = Gui.scrolling()
        .title(title.asComponent())
        .rows(6)
        .create()

    private var delegate: SolarisGUI? = null

    init {
        gui.setDefaultClickAction {
            if (it.clickedInventory?.type == InventoryType.CHEST) it.isCancelled = true
        }
        gui.setBasicScrollTemplate()
        val closeText = "CLOSE".asComponent()
            .color(TextColor.color(255,0,0))
            .style {
                it.decorate(TextDecoration.BOLD)
            }
        gui.setItem(6, 5, ItemBuilder
            .from(Material.RED_WOOL)
            .name(closeText)
            .asGuiItem {
            onClose(it.whoClicked as Player)
            gui.close(it.whoClicked as Player)
        })
    }

    fun placeBackArrow() {
        delegate?.let {
            val backItem = ItemBuilder
                .from(Material.ARROW)
                .name("Back".asComponent())
                .lore("<-- ${delegate!!.title}".asComponent())
            gui.setItem(5, 5, backItem.asGuiItem {
                back(it.whoClicked as Player)
            })
        }
    }

    fun open(player: Player) {

        onOpen(player)
        placeBackArrow()
        gui.open(player)
    }

    fun openWithDelegate(player: Player, solarisGUI: SolarisGUI) {
        delegate = solarisGUI
        open(player)
    }

    fun back(player: Player) {
        delegate?.open(player)
    }

    open fun onOpen(player: Player) {
    }

    open fun onClose(player: Player) {
    }
}