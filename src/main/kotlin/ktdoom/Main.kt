package ktdoom

import java.lang.management.ManagementFactory
import ktdoom.Engine

/*
* KtDoom Entry Point
* */
fun main() {
    var engine = Engine()
    engine.DOOM.setupLoop()
}