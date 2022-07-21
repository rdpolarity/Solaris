package me.rdpolarity.solaris.engine.objects

import me.rdpolarity.solaris.annotations.GamePrefab
import me.rdpolarity.solaris.data.Constants
import me.rdpolarity.solaris.engine.GameBehaviour
import me.rdpolarity.solaris.engine.GameObject

@GamePrefab
class SpeedBoost : GameObject("SpeedBooster") {
    override var description: String = "Right clicking this will give you a short speed boost"
    override var icon: String = Constants.Skulls.SPEED
    override var behaviours: MutableList<GameBehaviour> = mutableListOf()
}