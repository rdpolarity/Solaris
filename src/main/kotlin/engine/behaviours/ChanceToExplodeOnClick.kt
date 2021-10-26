package engine.behaviours

import annotations.GameProperty
import engine.GameBehaviour
import org.bukkit.Material

class ChanceToExplodeOnClick : GameBehaviour() {
    override var name: String = "ExplosionChance"
    override var description: String = "Might explode when clicked"
    override var icon: Material = Material.TNT

    @field:GameProperty
    val explosionChance : Float = 0.4f

    override fun onStart() {

    }

    // onUpdate
    // onInteract
}