package fr.bescouvois

import fr.bescouvois.State.Companion.get
import fr.bescouvois.State.Companion.modify

sealed class WaterEvent
object Freeze : WaterEvent()
object Melt : WaterEvent()
object Vaporize : WaterEvent()
object Condense : WaterEvent()

sealed class WaterState
object Solid : WaterState() {
    override fun toString(): String = "Solid"
}
object Liquid : WaterState() {
    override fun toString(): String = "Liquid"
}
object Gas : WaterState() {
    override fun toString(): String = "Gas"
}

data class Water(val state: WaterState)

object Simulator {
    fun simulateWaterStates(inputs: List<WaterEvent>): State<Water, WaterState> =
        State.sequence(inputs.map { modify(updateState(it)) })
            .flatMap { get<Water>() }
            .map { it.state }

    private fun updateState(e: WaterEvent): (Water) -> Water = { w: Water ->
        when (e) {
            is Freeze   -> if (w.state == Liquid) Water(Solid) else w
            is Melt     -> if (w.state == Solid) Water(Liquid) else w
            is Vaporize -> if (w.state == Liquid) Water(Gas) else w
            is Condense -> if (w.state == Gas) Water(Liquid) else w
        }
    }
}