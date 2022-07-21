package me.rdpolarity.solaris.engine.behaviours

import me.rdpolarity.solaris.annotations.GameProperty
import me.rdpolarity.solaris.engine.GameBehaviour
import org.bukkit.Location
import org.bukkit.Material

class SpawnChestWithLoot : GameBehaviour() {
    override var name = "ChestSpawner"
    override var description = "spawns a chest with some random loot in it"
    override var icon: Material = Material.CHEST

    @field:GameProperty
    val rarityLevel : Int = 2
    @field:GameProperty
    val chestType : String = "legendary"

    override fun onGizmo(location: Location) {
        gizmo.cuboid(location)
//        gizmo.title(location, "Testing gizmo name ${rarityLevel}")
    }

    override fun onStart() {
        // on start logic
    }

    // override fun onStateChange()
    // override fun onUpdate()
    // override fun onInteraction()
}