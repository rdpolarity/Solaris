package engine

import extentions.asComponent
import GUI.SolarisGUI
import Solaris
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTItem
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.GuiItem
import extentions.debugLog
import hazae41.minecraft.kutils.bukkit.msg
import managers.GlobalDataManager
import managers.MapManager
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.reflect.full.createInstance

abstract class GameObject {
    val type = this::class.simpleName!!
    abstract var name: String
        protected set
    abstract var description: String
        protected set
    protected abstract var icon: String
    protected abstract var behaviours: MutableList<GameBehaviour>
    var location : Location? = null
    var activeBehaviours : MutableList<GameBehaviour> = mutableListOf()

    inner class ObjectGUI() : SolarisGUI("$type Object") {
        override fun onOpen(player: Player) {
            gui.clearPageItems()
            val removeItem = ItemBuilder
                .from(Material.TNT)
                .name("Destroy Object".asComponent().color(TextColor.color(255, 0 ,0)))
                .asGuiItem {
                    location.toString().debugLog()
                    location?.let {
                        val block = it.block
                        block.type = Material.AIR
                        destroyObject()
                        back(player)
                    }
                }
            gui.setItem(7, 0, removeItem)
            behaviours.forEach { component ->
                gui.addItem(
                    ItemBuilder.from(component.icon)
                        .name("${component.name} [Behaviour]".asComponent())
                        .lore(component.description.asComponent())
                        .asGuiItem() { component.PropertiesGUI().openWithDelegate(player, this) }
                )
            }
        }
    }

    fun destroyObject() {
        location?.let {
            stopGizmos()
            GlobalDataManager.removeLocation(it)
            MapManager.activeObjects.remove(this)
        }
    }

    fun instantiate(gameObject : GameObject, location: Location) : GameObject {
        val instantiatedObject = gameObject::class.createInstance()
        if (MapManager.activeObjects.all { it.name.dropLast(2) == instantiatedObject.name }) {
            val amount = MapManager.activeObjects.count { it.type == instantiatedObject.type }
            instantiatedObject.name = instantiatedObject.name + " " + amount.toString()
        }
        behaviours.forEach {
            val instantiatedBehaviour = it::class.createInstance()
            instantiatedObject.activeBehaviours.add(instantiatedBehaviour)
        }
        MapManager.activeObjects.add(instantiatedObject)
        GlobalDataManager.addLocation(location)
        instantiatedObject.location = location
        instantiatedObject.runGizmos()
        return instantiatedObject
    }

    fun placeObject(event: BlockPlaceEvent) {
        instantiate(this, event.block.location)
        event.player.msg("You placed a engine.GameObject")
        val blockNBT = NBTBlock(event.block)
        val itemNBT = NBTItem(event.itemInHand)
        blockNBT.data.setObject(Constants.NBT.SOLARIS_KEY, itemNBT.getObject(Constants.NBT.SOLARIS_KEY, NBTData::class.java))
        event.player.msg("Data: ${blockNBT.data}")
    }

    fun editObject(event: PlayerInteractEvent) {
        ObjectGUI().open(event.player)
    }

    inner class NBTData(
        val type : String = this@GameObject.type,
        val name : String = this@GameObject.name,
        val overrides: String = "null"
    )

    fun runGizmos() {
        activeBehaviours.forEach { it.onGizmo(location ?: return) }
    }

    fun stopGizmos() {
        activeBehaviours.forEach { it.onStopGizmo()  }
    }


    fun asItem(): GuiItem {
        val skull = ItemBuilder.skull().texture(icon)
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

    fun asItemWithLink(solarisGUI: SolarisGUI): GuiItem {
        val skull = ItemBuilder.skull().texture(icon)
            ?: ItemBuilder.from(Material.STONE)
        val skullNBT = NBTItem(skull.build())
        skullNBT.setObject(Constants.NBT.SOLARIS_KEY, NBTData())
        return ItemBuilder
            .from(skullNBT.item)
            .name(name.asComponent())
            .lore(
                description.asComponent(),
                location?.x.toString().asComponent(),
                location?.y.toString().asComponent(),
                location?.z.toString().asComponent()
            )
            .asGuiItem {
                it.isCancelled = true
                if (it.click != ClickType.LEFT) return@asGuiItem;
                this@GameObject.ObjectGUI().openWithDelegate(it.whoClicked as Player, solarisGUI)
            }
    }
}