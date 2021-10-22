import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import hazae41.minecraft.kutils.bukkit.msg
import org.bukkit.entity.Player

@CommandAlias("solaris|se|sol")
class Commands : BaseCommand() {

    @Default
    @Description("Debug command for Solaris")
    private fun onSolaris(player : Player) {
        player.msg("bruh")
    }
}