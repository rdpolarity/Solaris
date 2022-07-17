package managers

import GUI.SolarisGUI
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
import engine.objects.LootChest
import extentions.broadcast
import extentions.debugLog
import hazae41.minecraft.kutils.bukkit.listen
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.annotation.command.Command
import kotlin.reflect.KClass

/**
 * The Map Manager handles keeping track of all objects in various worlds
 */
object MapManager {
    private val prefabs : MutableList<GameObject> = mutableListOf()
    val activeObjects : MutableList<GameObject> = mutableListOf()

    /**
     * Displays all the prefabs added to the prefabs list
     */
    class PrefabGUI : SolarisGUI("Prefab Selector") {
        override fun onOpen(player: Player) {
            gui.clearPageItems()
            prefabs.forEach {
                gui.addItem(it.asItem())
            }
        }
    }

    /**
     * Displays all active objects in the current world
     */
    class ObjectsGUI : SolarisGUI("Active Objects") {
        override fun onOpen(player: Player) {
            gui.clearPageItems()
            activeObjects.forEach { obj ->
                if (obj.location?.world == player.world) {
                    gui.addItem(obj.asItemWithLink(this))
                }
            }
        }
    }

    /**
     * Finds all objects for each world and stores them in active objects
     */
    private fun initActiveObjects() {
        Bukkit.getWorlds().forEach { world ->
            "searching ${world.name}:".broadcast()
            GlobalDataManager.getData(world).locations.forEach { loc ->
                "searching ${loc}".debugLog()
                val obj = getPrefabObjectAt(loc.toLocation(world))
                "result: ${loc.toLocation(world).block.type}".debugLog()
                obj?.let {
                    "found ${it.name}".debugLog()
                    "creating object at ${loc.toLocation(world)}".debugLog()
                    it.instantiate(it, loc.toLocation(world))
                }
            }
        }
    }

    private fun placePrefab(event: BlockPlaceEvent) {
        val itemNBT = NBTItem(event.itemInHand)
        val solarisData = itemNBT.getObject(Constants.NBT.SOLARIS_KEY, GameObject.NBTData::class.java)
        prefabs.findLast { it.type == solarisData.type }?.placeObject(event)
    }

//    fun getObjects(player: Player) : List<GameObject> {
//        val objects : MutableList<GameObject> = mutableListOf()
//        GlobalDataManager.getData(player.world).locations.forEach { loc ->
//            val fulLoc = Location(player.world, loc.x, loc.y, loc.z)
//            getObjectAt(fulLoc)?.let { objects.add(it) }
//        }
//        return objects
//    }

    /**
     * Finds the prefab type used at a location
     */
    private fun getPrefabObjectAt(location: Location): GameObject? {
        val block = location.block
        if (block.type !in listOf(Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD)) return null
        if (!block.isGameObject()) return null
        val itemNBT = NBTBlock(block)
        val solarisData = itemNBT.data.getObject(Constants.NBT.SOLARIS_KEY, GameObject.NBTData::class.java)
        return prefabs.find { it.type == solarisData.type }
    }

    /**
     * Finds active objects at a location
     */
    private fun getActiveObjectAt(location: Location): GameObject? {
        return activeObjects.find { it.location == location }
    }

    /**
     * Edits an active object using player interaction event details
     */
    private fun editObject(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.hand == EquipmentSlot.HAND) {
            event.clickedBlock?.let {
                getActiveObjectAt(it.location)?.editObject(event)
            }
        }
    }

    /**
     * Add any amount of GameObjects to the prefabs list
     * Which can be accessed in-game with `/map prefabs`
     */
    fun addPrefab(vararg objects: GameObject) {
        objects.forEach {
            prefabs.add(it)
        }
    }

    fun addPrefab(objects: List<GameObject>) {
        objects.forEach {
            prefabs.add(it)
        }
    }

    fun onEnable(solaris: Solaris) {
        initActiveObjects()
        registerEvents(solaris)
    }

    private fun registerEvents(solaris: Solaris) {
        // Prevents game object heads from being broken, instead they can be destroyed through GUI
        solaris.listen<BlockBreakEvent> { if (it.block.isGameObject()) it.isCancelled = true }
        // Brings up the edit GUI on right click
        solaris.listen<PlayerInteractEvent> { editObject(it) }
        // Calls the necessary function when placing a blocks that's a game object
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

        @Subcommand("objects")
        fun onObjects(player: Player) { ObjectsGUI().open(player) }

        @Subcommand("data")
        fun onData(player: Player) {
            GlobalDataManager.getData(player.world).toString().broadcast()
        }

        @Subcommand("dataAdd")
        fun onDataAdd(player: Player) {
            GlobalDataManager.addLocation(player.location)
//            GlobalDataManager.addLocation(player, Vector(MathUtils.random(0,10),MathUtils.random(0,10),MathUtils.random(0,10)))
        }

        @Subcommand("dataClear")
        fun onDataClear(player: Player) {
            GlobalDataManager.resetData(player.world)
        }
    }
}