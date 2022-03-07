package doom

import java.io.IOException

interface IDoom {
    fun PostEvent(ev: event_t?)
    fun PageTicker()
    fun PageDrawer()
    fun AdvanceDemo()
    fun StartTitle()

    @Throws(IOException::class)
    fun QuitNetGame()
}