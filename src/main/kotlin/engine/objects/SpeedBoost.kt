package engine.objects

import data.Constants
import engine.GameBehaviour
import engine.GameObject

class SpeedBoost : GameObject() {
    override var name: String = "SpeedBooster"
    override var description: String = "Right clicking this will give you a short speed boost"
    override var icon: String = Constants.Skulls.SPEED
    override var behaviours: MutableList<GameBehaviour> = mutableListOf()
}