package ktdoom

import mochadoom.Engine
import java.lang.management.ManagementFactory


/*
* KtDoom Entry Point
* */
fun main() {
    val runtimeMxBean = ManagementFactory.getRuntimeMXBean()
    val args = runtimeMxBean.inputArguments
    var engine = Engine(*args.toTypedArray())
    engine.DOOM.setupLoop()
}