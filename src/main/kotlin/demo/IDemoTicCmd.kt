package demo

import doom.ticcmd_t
import w.IWritableDoomObject

interface IDemoTicCmd : IWritableDoomObject {
    fun decode(dest: ticcmd_t?)
    fun encode(source: ticcmd_t?)
}