package i

import data.sfxinfo_t

interface DoomSoundInterface {
  fun I_InitSound()
  fun I_UpdateSound()
  fun I_SubmitSound()
  fun I_ShutdownSound()
  fun I_SetChannels()
  fun I_GetSfxLumpNum(sfxinfo: sfxinfo_t?): Int
  fun I_StartSound(id: Int, vol: Int, sep: Int, pitch: Int, priority: Int): Int
  fun I_StopSound(handle: Int)
  fun I_SoundIsPlaying(handle: Int): Boolean
  fun I_UpdateSoundParams(handle: Int, vol: Int, sep: Int, pitch: Int)
  fun I_InitMusic()
  fun I_ShutdownMusic()
  fun I_SetMusicVolume(volume: Int)
  fun I_PauseSong(handle: Int)
  fun I_ResumeSong(handle: Int)
  fun I_RegisterSong(data: ByteArray?): Int
  fun I_PlaySong(handle: Int, looping: Int)
  fun I_StopSong(handle: Int)
  fun I_UnRegisterSong(handle: Int)
}