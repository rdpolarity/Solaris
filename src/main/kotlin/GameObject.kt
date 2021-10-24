import Extensions.asComponent
import data.Constants
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.GuiItem
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType

open class GameObject {
    var name: String = "GameObject"
        protected set
    var description: String = "No description available"
        protected set
    protected var icon: String = ""
    protected var components: List<GameBehaviour> = listOf()



    fun asItem(): GuiItem {
        val skull = ItemBuilder.skull().texture(Constants.Skulls.CHEST)
            ?: ItemBuilder.from(Material.STONE)
        return skull
            .name(name.asComponent())
            .lore(description.asComponent())
            .asGuiItem() {
                it.isCancelled = true
                if (it.click != ClickType.LEFT) return@asGuiItem;
                it.cursor = it.currentItem
            }
    }
}