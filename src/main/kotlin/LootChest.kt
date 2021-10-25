import data.Constants

class LootChest : GameObject() {
    init {
        name = "LootChest"
        description = "A chest with random loot inside"
        icon = Constants.Skulls.CHEST
    }

    @field:GameProperty var rarity = 0
    var test = "test :O"
    var privateThing = 0
}