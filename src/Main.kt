package fr.bescouvois

fun main() {
    val inputs = listOf(Melt, Vaporize, Condense, Freeze, Melt)
    println(Simulator.simulateWaterStates(inputs).run(Water(Solid)))
}