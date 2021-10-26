package managers

import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTCompound
import extentions.broadcast
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * Keeps track of the locations of each object on a
 * Map using NBT data of a block at 0,0,0
 */
object GlobalDataManager {
    /**
     * This manages data sent to the global data block
     */
    data class GlobalData(val locations: MutableList<Vector>)

    private fun getBlockNBT(player: Player) : NBTCompound? {
        val block = player.world.getBlockAt(0,0,0)
        return NBTBlock(block).data
    }

    private fun saveData(player: Player, globalData: GlobalData) {
        getBlockNBT(player)?.setObject(Constants.NBT.SOLARIS_GLOBAL_KEY, globalData)
            ?: player.msg("Block NBT not found?")
    }

    fun resetData(player: Player) {
        getBlockNBT(player)?.setObject(Constants.NBT.SOLARIS_GLOBAL_KEY, GlobalData(mutableListOf()))
            ?: player.msg("Block NBT not found?")
    }

    /**
     * Gets existing data in world otherwise creates it
     */
    fun getData(player: Player) : GlobalData {
        val blockNBT = getBlockNBT(player)
        val blockGlobalData : GlobalData? = blockNBT?.getObject(Constants.NBT.SOLARIS_GLOBAL_KEY, GlobalData::class.java)
        return if (blockGlobalData != null) {
            player.msg("Global Data Found!")
            blockGlobalData
        } else {
            player.msg("Global Data Not Found: Creating new Global Data")
            resetData(player)
            GlobalData(mutableListOf())
        }
    }

    fun addLocation(player: Player, vector: Vector) {
        val currentData = getData(player)
        if (currentData.locations.all { it != vector }) {
            currentData.locations.add(vector)
            saveData(player, currentData)
        } else {
            player.msg("Object location already exists")
        }
    }
    fun removeLocation(player: Player, vector: Vector) {
        val currentData = getData(player)
        currentData.locations.removeIf { it == vector }
        saveData(player, currentData)
    }
}