import GameObject
import data.Constants

class LootChest : GameObject() {
    init {
        name = "LootChest"
        description = "A chest with random loot inside"
        icon = Constants.Skulls.CHEST
    }
}