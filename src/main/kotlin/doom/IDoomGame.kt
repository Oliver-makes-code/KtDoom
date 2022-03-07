package doom

import defines.skill_t

interface IDoomGame {
    fun ExitLevel()
    fun WorldDone()
    fun CheckDemoStatus(): Boolean
    fun DeferedInitNew(skill: skill_t?, episode: Int, map: Int)
    fun LoadGame(name: String?)
    fun SaveGame(slot: Int, description: String?)
    fun ScreenShot()
    fun StartTitle()
    var gameAction: gameaction_t?
    fun DeathMatchSpawnPlayer(playernum: Int)
}