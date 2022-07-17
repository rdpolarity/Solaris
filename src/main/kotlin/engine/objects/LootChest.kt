package engine.objects

import annotations.GamePrefab
import data.Constants
import engine.GameBehaviour
import engine.GameObject
import engine.behaviours.ChanceToExplodeOnClick
import engine.behaviours.SpawnChestWithLoot

@GamePrefab
class LootChest : GameObject("LootChest (With a surprise)") {
    override var description = "A chest with random loot inside... maybe"
    override var icon = Constants.Skulls.CHEST
    override var behaviours: MutableList<GameBehaviour> = mutableListOf(
        SpawnChestWithLoot(),
        ChanceToExplodeOnClick()
    )
}