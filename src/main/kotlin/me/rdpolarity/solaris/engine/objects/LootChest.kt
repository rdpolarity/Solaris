package me.rdpolarity.solaris.engine.objects

import me.rdpolarity.solaris.annotations.GamePrefab
import me.rdpolarity.solaris.data.Constants
import me.rdpolarity.solaris.engine.GameBehaviour
import me.rdpolarity.solaris.engine.GameObject
import me.rdpolarity.solaris.engine.behaviours.ChanceToExplodeOnClick
import me.rdpolarity.solaris.engine.behaviours.SpawnChestWithLoot

@GamePrefab
class LootChest : GameObject("LootChest (With a surprise)") {
    override var description = "A chest with random loot inside... maybe"
    override var icon = Constants.Skulls.CHEST
    override var behaviours: MutableList<GameBehaviour> = mutableListOf(
        SpawnChestWithLoot(),
        ChanceToExplodeOnClick()
    )
}