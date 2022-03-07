package automap

import doom.SourceCode.AM_Map
import doom.event_t

interface IAutoMap<T, V> {
    @AM_Map.C(AM_Map.AM_Responder)
    fun Responder(ev: event_t?): Boolean

    fun Ticker()

    fun Drawer()

    fun Repalette()

    @AM_Map.C(AM_Map.AM_Stop)
    fun Stop()
    fun Start()

    companion object {
        const val AM_MSGHEADER = ('a'.code shl 24) + ('m'.code shl 16)
        const val AM_MSGENTERED = AM_MSGHEADER or ('e'.code shl 8)
        const val AM_MSGEXITED = AM_MSGHEADER or ('x'.code shl 8)
        const val REDRANGE = 16
        const val BLUERANGE = 8
        const val GREENRANGE = 16
        const val GRAYSRANGE = 16
        const val BROWNRANGE = 16
        const val YELLOWRANGE = 1
        const val YOURRANGE = 0
        const val WALLRANGE = REDRANGE
        const val TSWALLRANGE = GRAYSRANGE
        const val FDWALLRANGE = BROWNRANGE
        const val CDWALLRANGE = YELLOWRANGE
        const val THINGRANGE = GREENRANGE
        const val SECRETWALLRANGE = WALLRANGE
        const val GRIDRANGE = 0
    }
}