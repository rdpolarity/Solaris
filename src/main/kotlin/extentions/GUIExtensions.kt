package extentions

import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.ScrollingGui
import org.bukkit.Material

fun ScrollingGui.setBasicScrollTemplate() {
    this.setPageSize(14)
    val blankPane = ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem()
    filler.fillBorder(blankPane)
    filler.fillBetweenPoints(5,0, 5, 9, blankPane)
    filler.fillBetweenPoints(4,0, 4, 9, blankPane)
    setItem(5, 3, ItemBuilder.from(Material.PAPER).name("<--- Scroll Left".asComponent()).asGuiItem { previous() })
    setItem(5, 7, ItemBuilder.from(Material.PAPER).name("Scroll Right --->".asComponent()).asGuiItem { next() })
}