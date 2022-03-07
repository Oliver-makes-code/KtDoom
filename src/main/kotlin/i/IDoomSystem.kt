package i

import doom.ticcmd_t

interface IDoomSystem {
    fun AllocLow(length: Int)
    fun BeginRead()
    fun EndRead()
    fun WaitVBL(count: Int)
    fun ZoneBase(size: Int): ByteArray?
    fun GetHeapSize(): Int
    fun Tactile(on: Int, off: Int, total: Int)
    fun Quit()
    fun BaseTiccmd(): ticcmd_t?
    fun Error(error: String?, vararg args: Any?)
    fun Error(error: String?)
    fun Init()
    fun GenerateAlert(title: String?, cause: String?): Boolean
}