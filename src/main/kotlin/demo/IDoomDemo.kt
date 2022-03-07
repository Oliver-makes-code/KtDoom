package demo

import defines.skill_t
import w.IWritableDoomObject

interface IDoomDemo : IWritableDoomObject {
    /** Get next demo command, in its raw format. Use
     * its own adapters if you need it converted to a
     * standard ticcmd_t.
     *
     * @return
     */
    val nextTic: IDemoTicCmd?

    /** Record a demo command in the IDoomDemo's native format.
     * Use the IDemoTicCmd's objects adaptors to convert it.
     *
     * @param tic
     */
    fun putTic(tic: IDemoTicCmd?)
    var version: Int
    var skill: skill_t?
    var episode: Int
    var map: Int
    var isDeathmatch: Boolean
    var isRespawnparm: Boolean
    var isFastparm: Boolean
    var isNomonsters: Boolean
    var consoleplayer: Int
    var playeringame: BooleanArray?

    fun resetDemo()

    companion object {
        /** Vanilla end demo marker, to append at the end of recorded demos  */
        const val DEMOMARKER = 0x80
    }
}