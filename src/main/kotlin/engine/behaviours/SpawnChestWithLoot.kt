package engine.behaviours

import annotations.GameProperty
import engine.GameBehaviour
import org.bukkit.Material

class SpawnChestWithLoot : GameBehaviour() {
    override var name = "ChestSpawner"
    override var description = "spawns a chest with some random loot in it"
    override var icon: Material = Material.CHEST

    @field:GameProperty
    val rarityLevel : Int = 2
    @field:GameProperty
    val chestType : String = "legendary"

    override fun onStart() {
        // on start logic
    }

    // override fun onUpdate()
    // override fun onInteraction()
}