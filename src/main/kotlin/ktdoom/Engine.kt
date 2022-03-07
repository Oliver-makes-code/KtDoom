package ktdoom

import awt.DoomWindow
import awt.DoomWindowController
import awt.EventBase.KeyStateInterest
import awt.EventBase.KeyStateSatisfaction
import awt.EventHandler
import awt.EventObserver
import doom.CVarManager
import doom.CommandVariable
import doom.ConfigManager
import doom.DoomMain
import g.Signals.ScanCode
import i.Strings
import java.lang.management.ManagementFactory

class Engine {
    companion object {
        lateinit var instance: Engine

        fun getEngine() = instance
        fun getCVM() = instance.cvm
        fun getConfig() = instance.cm

        fun updateFrame() {
            instance.windowController.updateFrame()
        }
    }
    val cvm: CVarManager
    val cm: ConfigManager
    val windowController: DoomWindowController<*, EventHandler>
    val DOOM: DoomMain<Any, Any>

    init {
        instance = this
        val runtimeMxBean = ManagementFactory.getRuntimeMXBean()
        val args = runtimeMxBean.inputArguments
        cvm = CVarManager(args)
        cm = ConfigManager()
        DOOM = DoomMain()
        windowController = DoomWindow.createCanvasWindowController(
            DOOM.graphicSystem::getScreenImage,
            DOOM::PostEvent,
            DOOM.graphicSystem.screenWidth,
            DOOM.graphicSystem.screenHeight
        )
        windowController.observer.addInterest(
            KeyStateInterest({ obs: EventObserver<EventHandler?>? ->
                EventHandler.fullscreenChanges(windowController.observer, windowController.switchFullscreen())
                KeyStateSatisfaction.WANTS_MORE_ATE
            }, ScanCode.SC_LALT, ScanCode.SC_ENTER)
        ).addInterest(
            KeyStateInterest({ obs: EventObserver<EventHandler?>? ->
                if (!windowController.isFullscreen) {
                    if (DOOM.menuactive || DOOM.paused || DOOM.demoplayback) {
                        EventHandler.menuCaptureChanges(obs, !DOOM.mousecaptured.also { DOOM.mousecaptured = it })
                    } else { // can also work when not DOOM.mousecaptured
                        EventHandler.menuCaptureChanges(obs, true.also { DOOM.mousecaptured = it })
                    }
                }
                KeyStateSatisfaction.WANTS_MORE_PASS
            }, ScanCode.SC_LALT)
        ).addInterest(
            KeyStateInterest({ obs: EventObserver<EventHandler?>? ->
                if (!windowController.isFullscreen && !DOOM.mousecaptured && DOOM.menuactive) {
                    EventHandler.menuCaptureChanges(obs, true.also { DOOM.mousecaptured = it })
                }
                KeyStateSatisfaction.WANTS_MORE_PASS
            }, ScanCode.SC_ESCAPE)
        ).addInterest(
            KeyStateInterest({ obs: EventObserver<EventHandler?>? ->
                if (!windowController.isFullscreen && !DOOM.mousecaptured && DOOM.paused) {
                    EventHandler.menuCaptureChanges(obs, true.also { DOOM.mousecaptured = it })
                }
                KeyStateSatisfaction.WANTS_MORE_PASS
            }, ScanCode.SC_PAUSE)
        )
    }

    fun getWindowTitle(frames: Double): String {
        if (cvm.bool(CommandVariable.SHOWFPS))
            return String.format("%s - %s FPS: %.2f", Strings.KT_DOOM_TITLE, DOOM.bppMode, frames)
        return String.format("%s - %s", Strings.KT_DOOM_TITLE, DOOM.bppMode)
    }
}