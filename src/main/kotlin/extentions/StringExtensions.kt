package extentions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit

fun String.asComponent(): TextComponent {
    return Component.text(this)
}

fun String.broadcast() {
    Bukkit.broadcastMessage(this)
}