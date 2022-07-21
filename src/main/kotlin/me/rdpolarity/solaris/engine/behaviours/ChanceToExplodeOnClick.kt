package me.rdpolarity.solaris.engine.behaviours

import me.rdpolarity.solaris.annotations.GameProperty
import me.rdpolarity.solaris.engine.GameBehaviour
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