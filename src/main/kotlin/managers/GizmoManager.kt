package managers

import de.slikey.effectlib.EffectManager

object GizmoManager {
    private var effectManager: EffectManager = EffectManager(Solaris.getPlugin())
}