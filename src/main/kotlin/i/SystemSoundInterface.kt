package i

import data.sfxinfo_t

interface SystemSoundInterface {
    fun InitSound()
    fun UpdateSound()
    fun SubmitSound()

    // ... shut down and relase at program termination.
    fun ShutdownSound()
    fun SetChannels()
    fun GetSfxLumpNum(sfxinfo: sfxinfo_t?): Int
    fun StartSound(id: Int, vol: Int, sep: Int, pitch: Int, priority: Int): Int
    fun StopSound(handle: Int)
    fun SoundIsPlaying(handle: Int): Boolean
    fun UpdateSoundParams(handle: Int, vol: Int, sep: Int, pitch: Int)
    fun InitMusic()
    fun ShutdownMusic()
    fun SetMusicVolume(volume: Int)
    fun PauseSong(handle: Int)
    fun ResumeSong(handle: Int)
    fun RegisterSong(data: ByteArray?): Int
    fun PlaySong(handle: Int, looping: Int)
    fun StopSong(handle: Int)
    fun UnRegisterSong(handle: Int)
}