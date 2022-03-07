package doom

interface NetConsts {
    companion object {
        const val NCMD_EXIT = -0x80000000
        const val NCMD_RETRANSMIT = 0x40000000
        const val NCMD_SETUP = 0x20000000
        const val NCMD_KILL = 0x10000000
        const val NCMD_CHECKSUM = 0x0fffffff
        const val DOOMCOM_ID = 0x12345678
        const val CMD_SEND: Short = 1
        const val CMD_GET: Short = 2
    }
}