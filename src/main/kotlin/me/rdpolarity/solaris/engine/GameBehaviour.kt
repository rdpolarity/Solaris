package me.rdpolarity.solaris.engine

import me.rdpolarity.solaris.extentions.asComponent
import me.rdpolarity.solaris.GUI.SolarisGUI
import me.rdpolarity.solaris.annotations.GameProperty
import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import de.slikey.effectlib.EffectManager
import de.slikey.effectlib.effect.CubeEffect
import de.slikey.effectlib.effect.HelixEffect
import dev.triumphteam.gui.builder.item.ItemBuilder
import me.rdpolarity.solaris.Solaris
import me.rdpolarity.solaris.managers.GizmoManager
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player

abstract class GameBehaviour() {
    val type = this::class.simpleName!!
    abstract var name: String
    abstract var description: String
    abstract var icon: Material
    protected val gizmo : Gizmo = Gizmo()

    inner class PropertiesGUI : SolarisGUI("$name Properties" ) {
        override fun onOpen(player: Player) {
            gui.clearPageItems()
            val behaviour = this@GameBehaviour
            behaviour.javaClass.declaredFields.forEach { field ->
                if (field.isAnnotationPresent(GameProperty::class.java)) {
                    field.isAccessible = true
                    var propType = ""
                    var value : Any? = null
                    var material = Material.STONE
                    propType = when (field.type) {
                        String::class.java -> {
                            material = Material.STRING
                            value = field.get(behaviour).toString()
                            "String"
                        }
                        Int::class.java -> {
                            material = Material.COMPASS
                            value = field.getInt(behaviour)
                            "Int"
                        }
                        Float::class.java -> {
                            material = Material.OAK_BOAT
                            value = field.getFloat(behaviour)
                            "Float"
                        }
                        else -> "Unknown"
                    }
                    val lore = listOf(
                        "$propType with value: $value".asComponent(),
                        "Left Click to Edit".asComponent()
                    )
                    gui.addItem(
                        ItemBuilder.from(material)
                            .name(field.name.asComponent())
                            .lore(lore)
                            .asGuiItem()
                    )
                }
            }
        }
    }

    abstract fun onStart()

    open fun onGizmo(location: Location) {
    }

    fun onStopGizmo() {
        gizmo.stop()
    }

    /**
     * Manages the gizmo's for each behaviour
     */
    protected inner class Gizmo {
        var effectManager: EffectManager = EffectManager(Solaris.getPlugin())
        var holograms: MutableList<Hologram> = mutableListOf()

        fun cuboid(location: Location, particle : Particle = Particle.FLAME) {
            val gizmoLoc = location.clone().apply { y += .5; x += .5; z += .5 } // Center Upper Align
            val fx = CubeEffect(GizmoManager.effectManager)
            fx.iterations = -1
            fx.particle = particle
            fx.enableRotation = false
            fx.location = gizmoLoc
            effectManager.start(fx)
        }

        fun helix(location: Location, radius : Float = 2f, particle : Particle = Particle.FLAME) {
            val gizmoLoc = location.clone().apply { y += .5; x += .5; z += .5 } // Center Upper Align
            val fx = HelixEffect(GizmoManager.effectManager)
            fx.iterations = -1
            fx.radius = radius
            fx.strands = 10
            fx.particles = 20
            fx.curve = 1f
            fx.particle = particle
            fx.location = gizmoLoc
            effectManager.start(fx)
        }

        fun title(location: Location, text: String) {
            val gizmoLoc = location.clone().apply { y += 1; x += .5; z += .5 } // Center Upper Align
            val hologram = HologramsAPI.createHologram(Solaris.getPlugin(), gizmoLoc)
            hologram.appendTextLine(text)
            holograms.add(hologram)
        }

        fun stop() {
            effectManager.cancel(true)
            holograms.forEach { it.delete() }
        }
    }
}