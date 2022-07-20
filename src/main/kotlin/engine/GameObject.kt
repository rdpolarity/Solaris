package engine

import GUI.SolarisGUI
import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTItem
import dev.triumphteam.gui.builder.item.ItemBuilder
import dev.triumphteam.gui.guis.GuiItem
import extentions.asComponent
import extentions.broadcast
import extentions.debugLog
import hazae41.minecraft.kutils.bukkit.msg
import managers.GlobalDataManager
import managers.MapManager
import managers.StateManager
import mobx.core.autorun
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Skull
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*
import kotlin.reflect.full.createInstance

/**
 * Core GameObject which handles:
 * - Creating prefab items for GUI
 * - Removing skulls when in play mode
 * @param name Name of the gameobject, additional objects will append a number
 */
abstract class GameObject(var name: String) {
    val type = this::class.simpleName!!
    abstract var description: String
        protected set
    protected abstract var icon: String
    protected abstract var behaviours: MutableList<GameBehaviour>
    var location : Location? = null
    var activeBehaviours : MutableList<GameBehaviour> = mutableListOf()

    private fun onPlay() {
        "&8[${name}] &a▶ is now in play mode".broadcast()
        stopGizmos()
        removeHead()
    }

    private fun onEdit() {
        "&8[${name}] &6⏸ is now in edit mode".broadcast()
        runGizmos()
        addHead()
    }

    private fun onDebug() {
        "&8[${name}} &4⏹ is now in debug mode".broadcast()
        runGizmos()
        removeHead()
    }

    private fun removeHead() {
        location?.let {
            it.block.type = Material.AIR
        }
    }

    private fun addHead() {
        location?.let {
            val block = it.block
            block.type = Material.PLAYER_HEAD
//            val state = block.state as Skull
//            state.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(UUID.fromString(icon)))
        }
    }

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

    protected fun init() {
        "&8[${name}] &fhas been instantiated".broadcast()
        autorun {
            when(StateManager.state) {
                StateManager.STATE.PLAY -> onPlay()
                StateManager.STATE.EDIT -> onEdit()
                StateManager.STATE.DEBUG -> onDebug()
                else -> "${name} is now in unknown mode".broadcast()
            }
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
        instantiatedObject.init()
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
        "running gizmo at ${location}".broadcast()
        activeBehaviours.forEach { it.onGizmo(location ?: return) }
    }

    fun stopGizmos() {
        activeBehaviours.forEach { it.onStopGizmo()  }
    }

    /**
     * Gets this game objects as an object that can be duplicated and placed
     */
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

    /**
     * Gets game objects with the intent to be linked to an existing instance or prefab
     */
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