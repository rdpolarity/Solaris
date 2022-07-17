package extentions

import data.Constants
import hazae41.minecraft.kutils.textOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit

fun String.asComponent(): TextComponent {
    return Component.text(this)
}

fun String.broadcast() {
    Bukkit.broadcastMessage(textOf(this).toLegacyText())
}

fun net.md_5.bungee.api.chat.TextComponent.broadcast() {
    Bukkit.broadcastMessage(this.toLegacyText())
}

fun String.debugLog() {
    if (Constants.DEBUG.LOGGING) Bukkit.broadcastMessage(this)
}