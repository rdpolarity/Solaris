package engine

import extentions.asComponent
import GUI.SolarisGUI
import annotations.GameProperty
import dev.triumphteam.gui.builder.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

abstract class GameBehaviour {
    val type = this::class.simpleName!!
    abstract var name: String
    abstract var description: String
    abstract var icon: Material

    open fun onGizmo() {

    }

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
}