package ktdoom

import awt.DoomWindow
import awt.EventBase
import awt.EventBase.ActionMode
import awt.EventBase.ActionStateHolder
import p.ActiveStates
import v.graphics.Patches
import java.awt.AWTEvent
import java.io.OutputStream
import java.util.*
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors
import java.util.stream.Stream

val DEFAULT_LEVEL = Level.WARNING
val PARENT_LOGGERS_MAP: Map<Level?, Logger?> = Stream.of(Level.FINE, Level.FINER, Level.FINEST, Level.INFO, Level.SEVERE, Level.WARNING)
    .collect(
        Collectors.toMap(
            { l: Level? -> l },
            { l: Level? -> newLoggerHandlingLevel(l) }
        )
    )
val DEFAULT_LOGGER = PARENT_LOGGERS_MAP[DEFAULT_LEVEL]
val INDIVIDUAL_CLASS_LOGGERS: HashMap<String, Logger> = HashMap()
var lastHandler: EventBase<*>? = null

object Initialize {
    init {
        INDIVIDUAL_CLASS_LOGGERS[ActiveStates::class.java.name] = PARENT_LOGGERS_MAP[Level.FINER]!!
        INDIVIDUAL_CLASS_LOGGERS[DoomWindow::class.java.name] = PARENT_LOGGERS_MAP[Level.FINE]!!
        INDIVIDUAL_CLASS_LOGGERS[Patches::class.java.name] = PARENT_LOGGERS_MAP[Level.INFO]!!
    }
}

fun getLogger(className: String?): Logger? {
    val out = Logger.getLogger(className)
    out.parent = INDIVIDUAL_CLASS_LOGGERS.getOrDefault(className!!, DEFAULT_LOGGER!!)
    return out
}

fun <EventHandler> LogEvent(
    logger: Logger,
    actionStateHolder: ActionStateHolder<EventHandler>,
    handler: EventHandler,
    event: AWTEvent?
) where EventHandler : Enum<EventHandler>?, EventHandler : EventBase<EventHandler>? {
    if (!logger.isLoggable(Level.ALL) && lastHandler === handler) {
        return
    }
    lastHandler = handler
    fun getEBArr(len: Int): Array<EventBase<EventHandler>?> = Array(len) { null }
    val arrayGenerator: (Int) -> Array<EventBase<EventHandler>?> = ::getEBArr
    val depends = actionStateHolder
        .cooperations(handler, EventBase.RelationType.DEPEND)
        .stream()
        .filter { hdl: EventHandler ->
            actionStateHolder.hasActionsEnabled(
                hdl,
                ActionMode.DEPEND
            )
        }
        .toArray(arrayGenerator)
    val adjusts = actionStateHolder
        .adjustments(handler)
    val causes = actionStateHolder
        .cooperations(handler, EventBase.RelationType.CAUSE)
        .stream()
        .filter { hdl: EventHandler ->
            actionStateHolder.hasActionsEnabled(
                hdl,
                ActionMode.DEPEND
            )
        }
        .toArray(arrayGenerator)
    val reverts = actionStateHolder
        .cooperations(handler, EventBase.RelationType.REVERT)
        .stream()
        .filter { hdl: EventHandler ->
            actionStateHolder.hasActionsEnabled(
                hdl,
                ActionMode.DEPEND
            )
        }
        .toArray(arrayGenerator)
    if (logger.isLoggable(Level.FINEST)) logger.log(Level.FINEST) {
        String.format(
            "\n\nENCOUNTERED EVENT: %s [%s] \n%s: %s \n%s \n%s: %s \n%s: %s \nOn event: %s",
            handler, ActionMode.PERFORM,
            EventBase.RelationType.DEPEND, Arrays.toString(depends),
            adjusts.entries.stream().collect({ StringBuilder() },
                { sb: StringBuilder, (key, value): Map.Entry<EventBase.RelationType, Set<EventHandler>> ->
                    sb.append(
                        key
                    ).append(' ').append(value).append('\n')
                }
            ) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) },
            EventBase.RelationType.CAUSE, Arrays.toString(causes),
            EventBase.RelationType.REVERT, Arrays.toString(reverts),
            event
        )
    } else if (logger.isLoggable(Level.FINER)) {
        logger.log(Level.FINER) {
            String.format(
                "\n\nENCOUNTERED EVENT: %s [%s] \n%s: %s \n%s \n%s: %s \n%s: %s \n",
                handler, ActionMode.PERFORM,
                EventBase.RelationType.DEPEND, Arrays.toString(depends),
                adjusts.entries.stream().collect({ StringBuilder() },
                    { sb: StringBuilder, (key, value): Map.Entry<EventBase.RelationType, Set<EventHandler>> ->
                        sb.append(
                            key
                        ).append(' ').append(value).append('\n')
                    }
                ) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) },
                EventBase.RelationType.CAUSE, Arrays.toString(causes),
                EventBase.RelationType.REVERT, Arrays.toString(reverts)
            )
        }
    } else {
        logger.log(Level.FINE) {
            String.format(
                "\nENCOUNTERED EVENT: %s [%s]",
                handler, ActionMode.PERFORM
            )
        }
    }
}

private fun newLoggerHandlingLevel(l: Level?): Logger? {
    val h = OutHandler()
    h.setLevel(l)
    val ret = Logger.getAnonymousLogger()
    ret.useParentHandlers = false
    ret.level = l
    ret.addHandler(h)
    return ret
}

private class OutHandler : ConsoleHandler() {
    @Synchronized
    @Throws(SecurityException::class)
    override fun setOutputStream(out: OutputStream) {
        super.setOutputStream(System.out)
    }

}