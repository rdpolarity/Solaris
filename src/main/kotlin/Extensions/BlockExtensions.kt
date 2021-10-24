package Extensions

import data.Constants
import de.tr7zw.nbtapi.NBTBlock
import org.bukkit.block.Block

fun Block.isGameObject() : Boolean {
    val nbt = NBTBlock(this)
    nbt.data.keys.toString().broadcast()
    return nbt.data.hasKey(Constants.NBT.SOLARIS_KEY)
}