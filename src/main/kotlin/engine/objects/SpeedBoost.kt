package engine.objects

import annotations.GamePrefab
import data.Constants
import engine.GameBehaviour
import engine.GameObject

@GamePrefab
class SpeedBoost : GameObject("SpeedBooster") {
    override var description: String = "Right clicking this will give you a short speed boost"
    override var icon: String = Constants.Skulls.SPEED
    override var behaviours: MutableList<GameBehaviour> = mutableListOf()
}