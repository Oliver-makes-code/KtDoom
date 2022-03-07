package m

import doom.SourceCode.M_Menu
import doom.event_t

interface IDoomMenu {
    @M_Menu.C(M_Menu.M_Responder)
    fun Responder(ev: event_t?): Boolean
    @M_Menu.C(M_Menu.M_Ticker)
    fun Ticker()
    @M_Menu.C(M_Menu.M_Drawer)
    fun Drawer()
    @M_Menu.C(M_Menu.M_Init)
    fun Init()
    @M_Menu.C(M_Menu.M_StartControlPanel)
    fun StartControlPanel()
    var showMessages: Boolean
    var screenBlocks: Int
    val detailLevel: Int

    fun ClearMenus()
}