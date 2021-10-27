package managers

import de.slikey.effectlib.EffectManager

object GizmoManager {
    var effectManager: EffectManager = EffectManager(Solaris.getPlugin())
}