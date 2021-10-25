import Extensions.asComponent
import Extensions.broadcast
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTItem
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.Gui
import dev.triumphteam.gui.guis.GuiItem
import dev.triumphteam.gui.guis.PaginatedGui
import hazae41.minecraft.kutils.bukkit.msg
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent

open class GameObject {
    val type = this::class.simpleName!!
    var name: String = "GameObject"
        protected set
    var description: String = "No description available"
        protected set
    protected var icon: String = ""
    protected var components: List<GameBehaviour> = listOf()

    private val objectGUI : PaginatedGui = Gui.paginated()
        .title(Component.text("$type Menu"))
        .rows(6)
        .create()

    fun placeObject(event: BlockPlaceEvent) {
        event.player.msg("You placed a GameObject")
        val blockNBT = NBTBlock(event.block)
        val itemNBT = NBTItem(event.itemInHand)
        blockNBT.data.setObject(Constants.NBT.SOLARIS_KEY, itemNBT.getObject(Constants.NBT.SOLARIS_KEY, NBTData::class.java))
        event.player.msg("Data: ${blockNBT.data}")
        displayHologram(event.block.location)
    }

    fun displayHologram(location: Location) {
        location.apply { y++; x += .5; z += .5 } // Center Upper Align
        val hologram = HologramsAPI.createHologram(Solaris.getPlugin(), location)
        hologram.appendTextLine("$name [$type]")
    }

    fun editObject(event: PlayerInteractEvent) {
        objectGUI.clearPageItems()
        this.javaClass.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(GameProperty::class.java)) {
                field.isAccessible = true
                objectGUI.addItem(
                    ItemBuilder.from(Material.STONE)
                        .name(field.name.asComponent())
                        .lore(field.get(this).toString().asComponent())
                        .asGuiItem()
                )
            }
        }
        objectGUI.open(event.player)
    }

    inner class NBTData(
        val type : String = this@GameObject.type,
        val name : String = this@GameObject.name,
        val overrides: String = "null"
    )

    fun asItem(): GuiItem {
        val skull = ItemBuilder.skull().texture(Constants.Skulls.CHEST)
            ?: ItemBuilder.from(Material.STONE)
        val skullNBT = NBTItem(skull.build())
        skullNBT.setObject(Constants.NBT.SOLARIS_KEY, NBTData())
        return ItemBuilder
            .from(skullNBT.item)
            .name(name.asComponent())
            .lore(description.asComponent())
            .asGuiItem {
                it.isCancelled = true
                if (it.click != ClickType.LEFT) return@asGuiItem;
                it.cursor = it.currentItem
            }
    }
}