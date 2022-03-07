package doom

import java.io.IOException

interface IDoomGameNetworking {
    @Throws(IOException::class)
    fun TryRunTics()
    fun NetUpdate()
    var doomCom: doomcom_t
    var ticdup: Int
}