package managers

import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTCompound
import extentions.broadcast
import extentions.debugLog
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * Stateless manager to keep track of the locations for each object on a
 * Map using NBT data of a block at 0,0,0
 */
object GlobalDataManager {
    /**
     * Manages data sent to the global data block
     */
    data class GlobalData(val locations: MutableList<Vector>)

    /**
     * Retrieves NBT block data at 0,0,0 in specified world
     */
    private fun getBlockNBT(world: World) : NBTCompound? {
        val block = world.getBlockAt(0,0,0)
        return NBTBlock(block).data
    }

    /**
     * Saves global data object to the worlds 0,0,0
     */
    private fun saveData(world: World, globalData: GlobalData) {
        getBlockNBT(world)?.setObject(Constants.NBT.SOLARIS_GLOBAL_KEY, globalData)
    }

    /**
     * Clears all data stored at 0,0,0 in a set world
     */
    fun resetData(world: World) {
        getBlockNBT(world)?.setObject(Constants.NBT.SOLARIS_GLOBAL_KEY, GlobalData(mutableListOf()))
    }

    /**
     * Gets existing data in world otherwise creates it
     */
    fun getData(world: World) : GlobalData {
        val blockNBT = getBlockNBT(world)
        val blockGlobalData : GlobalData? = blockNBT?.getObject(Constants.NBT.SOLARIS_GLOBAL_KEY, GlobalData::class.java)
        return if (blockGlobalData != null) {
            blockGlobalData
        } else {
            resetData(world)
            GlobalData(mutableListOf())
        }
    }

    fun addLocation(location: Location) {
        val currentData = getData(location.world!!)
        if (currentData.locations.all { it != location.toVector() }) {
            currentData.locations.add(location.toVector())
            saveData(location.world!!, currentData)
        } else {
//            player.msg("Object location already exists")
        }
    }

    fun removeLocation(location: Location) {
        val currentData = getData(location.world!!)
        currentData.locations.removeIf { it == location.toVector() }
        saveData(location.world!!, currentData)
    }
}