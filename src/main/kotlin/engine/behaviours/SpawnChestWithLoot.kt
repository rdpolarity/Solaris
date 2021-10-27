package engine.behaviours

import annotations.GameProperty
import de.slikey.effectlib.effect.CubeEffect
import engine.GameBehaviour
import extentions.broadcast
import managers.GizmoManager
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
        location.apply { y += .5; x += .5; z += .5 } // Center Upper Align
        var fx = CubeEffect(GizmoManager.effectManager)
        fx.enableRotation = false
        fx.location = location
        GizmoManager.effectManager.start(fx)
    }

    override fun onStart() {
        // on start logic
    }

    // override fun onUpdate()
    // override fun onInteraction()
}