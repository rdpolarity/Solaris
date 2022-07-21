package me.rdpolarity.solaris.extentions

import me.rdpolarity.solaris.data.Constants
import de.tr7zw.nbtapi.NBTBlock
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

fun Block.isGameObject() : Boolean {
    val nbt = NBTBlock(this)
    return nbt.data.hasKey(Constants.NBT.SOLARIS_KEY)
}

fun ItemStack.isGameObject() : Boolean {
    val nbt = NBTItem(this)
    return nbt.hasKey(Constants.NBT.SOLARIS_KEY)
}