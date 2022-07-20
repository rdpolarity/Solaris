package engine.behaviours

import annotations.GameProperty
import de.slikey.effectlib.effect.CubeEffect
import de.slikey.effectlib.util.MathUtils
import engine.GameBehaviour
import extentions.broadcast
import managers.GizmoManager
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle

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