package automap

import data.Defines
import data.Limits
import data.Tables
import doom.*
import doom.SourceCode.AM_Map
import g.Signals.ScanCode
import m.cheatseq_t
import m.fixed_t
import p.mobj_t
import rr.line_t
import rr.patch_t
import utils.GenericCopy
import v.DoomGraphicSystem
import v.graphics.Plotter
import v.renderers.DoomScreen
import java.awt.Rectangle
import java.util.*

val NUMLITES = 8

class Map_<T,V>(val DOOM: DoomMain<T, V>) : IAutoMap<T,V> {
    companion object {
        val BACKGROUND: Color = Color.BLACK
        val YOURCOLORS: Color = Color.WHITE
        val WALLCOLORS: Color = Color.REDS
        val TELECOLORS: Color = Color.DARK_REDS
        val TSWALLCOLORS: Color = Color.GRAYS
        val FDWALLCOLORS: Color = Color.BROWNS
        val CDWALLCOLORS: Color = Color.YELLOWS
        val THINGCOLORS: Color = Color.GREENS
        val SECRETWALLCOLORS: Color = Color.REDS
        val GRIDCOLORS: Color = Color.DARK_GREYS
        val MAPPOWERUPSHOWNCOLORS: Color = Color.GRAYS
        val CROSSHAIRCOLORS: Color = Color.GRAYS
        val GENERATE_LITE_LEVELS_FOR: EnumSet<Color> = EnumSet.of(
            TELECOLORS,
            WALLCOLORS,
            FDWALLCOLORS,
            CDWALLCOLORS,
            TSWALLCOLORS,
            SECRETWALLCOLORS,
            MAPPOWERUPSHOWNCOLORS,
            THINGCOLORS
        )
        val THEIR_COLORS: Array<Color> = arrayOf(
            Color.GREENS,
            Color.GRAYS,
            Color.BROWNS,
            Color.REDS
        )
        val AM_PANDOWNKEY = ScanCode.SC_DOWN
        val AM_PANUPKEY = ScanCode.SC_UP
        val AM_PANRIGHTKEY = ScanCode.SC_RIGHT
        val AM_PANLEFTKEY = ScanCode.SC_LEFT
        val AM_ZOOMINKEY = ScanCode.SC_EQUALS
        val AM_ZOOMOUTKEY = ScanCode.SC_MINUS
        val AM_STARTKEY = ScanCode.SC_TAB
        val AM_ENDKEY = ScanCode.SC_TAB
        val AM_GOBIGKEY = ScanCode.SC_0
        val AM_FOLLOWKEY = ScanCode.SC_F
        val AM_GRIDKEY = ScanCode.SC_G
        val AM_MARKKEY = ScanCode.SC_M
        val AM_CLEARMARKKEY = ScanCode.SC_C
        const val AM_NUMMARKPOINTS = 10
        const val INITSCALEMTOF = (.2 * fixed_t.FRACUNIT).toInt()
        const val F_PANINC = 4
        const val M_ZOOMIN = (1.02 * fixed_t.FRACUNIT).toInt()
        const val M_ZOOMOUT = (fixed_t.FRACUNIT / 1.02).toInt()
    }
    val fixedColorSources: EnumMap<Color, V> = EnumMap<Color, V>(Color::class.java)
    val litedColorSources: EnumMap<Color, V> = EnumMap<Color, V>(Color::class.java)
    var overlay = 0
    var cheating = 0
    var grid = false
    var leveljuststarted = 1
    var finit_width = 0
    var finit_height = 0
    var f_x = 0
    var f_y = 0
    var f_w = 0
    var f_h = 0
    var f_rect: Rectangle? = null
    var lightlev = 0
    var plotter: Plotter<V>? = null
    var amclock = 0
    var m_paninc: mpoint_t? = null
    var mtof_zoommul = 0
    var ftom_zoommul = 0
    var m_x = 0
    var m_y:Int = 0
    var m_x2 = 0
    var m_y2:Int = 0
    var m_w = 0
    var m_h:Int = 0
    var min_x = 0
    var min_y:Int = 0
    var max_x:Int = 0
    var max_y:Int = 0
    var max_w = 0
    var max_h = 0
    var min_w = 0
    var min_h:Int = 0
    var min_scale_mtof = 0
    var max_scale_mtof = 0
    var old_m_w = 0
    var old_m_h:Int = 0
    var old_m_x:Int = 0
    var old_m_y:Int = 0
    var f_oldloc: mpoint_t? = null
    var scale_mtof = INITSCALEMTOF
    var scale_ftom = 0
    var plr: player_t? = null
    private val marknums = arrayOfNulls<patch_t>(10)
    private val markpoints: Array<mpoint_t>
    private var markpointnum = 0
    var followplayer = true
    var cheat_amap_seq =
        charArrayOf(0xb2.toChar(), 0x26.toChar(), 0x26.toChar(), 0x2e.toChar(), 0xff.toChar())
    var cheat_amap = cheatseq_t(cheat_amap_seq, 0)
    private val cheat_strobe_seq = charArrayOf(
        0x6e.toChar(), 0xa6.toChar(), 0xea.toChar(), 0x2e.toChar(), 0x6a.toChar(),
        0xf6.toChar(), 0x62.toChar(), 0xa6.toChar(), 0xff.toChar()
    )
    private val cheat_strobe = cheatseq_t(cheat_strobe_seq, 0)
    private var stopped = true
    val LINE_NEVERSEE = line_t.ML_DONTDRAW.toShort()
    private val MINIMUM_SCALE = (0.7 * fixed_t.FRACUNIT).toInt()
    private val MINIMUM_VIABLE_SCALE = fixed_t.FRACUNIT shr 5
    var player_arrow: Array<mline_t>? = null
    var NUMPLYRLINES = 0
    var cheat_player_arrow: Array<mline_t>? = null
    var NUMCHEATPLYRLINES = 0
    var triangle_guy: Array<mline_t>? = null
    var NUMTRIANGLEGUYLINES = 0
    var thintriangle_guy: Array<mline_t>? = null
    var NUMTHINTRIANGLEGUYLINES = 0
    var lastlevel = -1
    var lastepisode:Int = -1
    var cheatstate = false
    var bigstate:Boolean = false
    var buffer: String? = null
    private var tmpx = 0
    private var tmpy:Int = 0
    var LEFT = 1
    var RIGHT:Int = 2
    var BOTTOM:Int = 4
    var TOP:Int = 8
    var fuck:Int = 0
    private val fl = fline_t()
    private val ml = mline_t()
    var l = mline_t()
    private var rotx = 0
    private var roty:Int = 0

    init {
        this.markpoints = GenericCopy.malloc(
            { mpoint_t() },
            ::getPointArr, AM_NUMMARKPOINTS
        )
        f_oldloc = mpoint_t()
        m_paninc = mpoint_t()
        this.plotter = DOOM.graphicSystem.createPlotter(DoomScreen.FG)
        this.plr = DOOM.players[DOOM.displayplayer]
        Repalette()
        finit_width = DOOM.vs.screenWidth
        finit_height = DOOM.vs.screenHeight - 32 * DOOM.vs.safeScaling
    }

    fun getPointArr(len: Int): Array<mpoint_t> = Array(len, {mpoint_t()})
    private fun FTOM(x: Int): Int = fixed_t.FixedMul(x shl 16, scale_ftom)
    private fun MTOF(x: Int): Int = fixed_t.FixedMul(x, scale_mtof) shr 16
    private fun CXMTOF(x: Int): Int = f_x + MTOF(x - m_x)
    private fun CYMTOF(y: Int): Int = f_y + (f_h - MTOF(y - m_y))

    fun initVectorGraphics() {
        var R = 8 * Defines.PLAYERRADIUS / 7
        player_arrow = arrayOf(
            mline_t(-R + R / 8, 0, R, 0),
            mline_t(R, 0, R - R / 2, R / 4),
            mline_t(R, 0, R - R / 2, -R / 4),
            mline_t(-R + R / 8, 0, -R - R / 8, R / 4),
            mline_t(-R + R / 8, 0, -R - R / 8, -R / 4),
            mline_t(-R + 3 * R / 8, 0, -R + R / 8, R / 4),
            mline_t(-R + 3 * R / 8, 0, -R + R / 8, -R / 4)
        )
        NUMPLYRLINES = player_arrow!!.size
        cheat_player_arrow = arrayOf(
            mline_t(-R + R / 8, 0, R, 0),
            mline_t(R, 0, R - R / 2, R / 6),
            mline_t(R, 0, R - R / 2, -R / 6),
            mline_t(-R + R / 8, 0, -R - R / 8, R / 6),
            mline_t(-R + R / 8, 0, -R - R / 8, -R / 6),
            mline_t(-R + 3 * R / 8, 0, -R + R / 8, R / 6),
            mline_t(-R + 3 * R / 8, 0, -R + R / 8, -R / 6),
            mline_t(-R / 2, 0, -R / 2, -R / 6),
            mline_t(-R / 2, -R / 6, -R / 2 + R / 6, -R / 6),
            mline_t(-R / 2 + R / 6, -R / 6, -R / 2 + R / 6, R / 4),
            mline_t(-R / 6, 0, -R / 6, -R / 6),
            mline_t(-R / 6, -R / 6, 0, -R / 6),
            mline_t(0, -R / 6, 0, R / 4),
            mline_t(R / 6, R / 4, R / 6, -R / 7),
            mline_t(R / 6, -R / 7, R / 6 + R / 32, -R / 7 - R / 32),
            mline_t(
                R / 6 + R / 32, -R / 7 - R / 32,
                R / 6 + R / 10, -R / 7
            )
        )
        NUMCHEATPLYRLINES = cheat_player_arrow!!.size
        R = fixed_t.FRACUNIT
        triangle_guy = arrayOf(
            mline_t(-.867 * R, -.5 * R, .867 * R, -.5 * R),
            mline_t(.867 * R, -.5 * R, 0.toDouble(), R.toDouble()),
            mline_t(0.toDouble(), R.toDouble(), -.867 * R, -.5 * R)
        )
        NUMTRIANGLEGUYLINES = triangle_guy!!.size
        thintriangle_guy = arrayOf(
            mline_t(-.5 * R, -.7 * R, R.toDouble(), 0.toDouble()),
            mline_t(R.toDouble(), 0.toDouble(), -.5 * R, .7 * R),
            mline_t(-.5 * R, .7 * R, -.5 * R, -.7 * R)
        )
        NUMTHINTRIANGLEGUYLINES = thintriangle_guy!!.size
    }

    fun activateNewScale() {
        m_x += m_w / 2
        m_y += m_h / 2
        m_w = FTOM(f_w)
        m_h = FTOM(f_h)
        m_x -= m_w / 2
        m_y -= m_h / 2
        m_x2 = m_x + m_w
        m_y2 = m_y + m_h
        plotter!!.setThickness(
            Math.min(MTOF(fixed_t.FRACUNIT), DOOM.graphicSystem.scalingX),
            Math.min(MTOF(fixed_t.FRACUNIT), DOOM.graphicSystem.scalingY)
        )
    }

    fun saveScaleAndLoc() {
        old_m_x = m_x
        old_m_y = m_y
        old_m_w = m_w
        old_m_h = m_h
    }

    private fun restoreScaleAndLoc() {
        m_w = old_m_w
        m_h = old_m_h
        if (!followplayer) {
            m_x = old_m_x
            m_y = old_m_y
        } else {
            m_x = plr!!.mo.x - m_w / 2
            m_y = plr!!.mo.y - m_h / 2
        }
        m_x2 = m_x + m_w
        m_y2 = m_y + m_h

        scale_mtof = fixed_t.FixedDiv(f_w shl fixed_t.FRACBITS, m_w)
        scale_ftom = fixed_t.FixedDiv(fixed_t.FRACUNIT, scale_mtof)
        plotter!!.setThickness(
            Math.min(MTOF(fixed_t.FRACUNIT), NUMLITES),
            Math.min(MTOF(fixed_t.FRACUNIT), NUMLITES)
        )
    }
    fun addMark() {
        markpoints[markpointnum].x = m_x + m_w / 2
        markpoints[markpointnum].y = m_y + m_h / 2
        markpointnum = (markpointnum + 1) % AM_NUMMARKPOINTS
    }
    fun findMinMaxBoundaries() {
        val a: Int
        val b: Int
        min_y = Limits.MAXINT
        min_x = min_y
        max_y = -Limits.MAXINT
        max_x = max_y
        for (i in 0 until DOOM.levelLoader.numvertexes) {
            if (DOOM.levelLoader.vertexes[i].x < min_x) min_x =
                DOOM.levelLoader.vertexes[i].x else if (DOOM.levelLoader.vertexes[i].x > max_x) max_x =
                DOOM.levelLoader.vertexes[i].x
            if (DOOM.levelLoader.vertexes[i].y < min_y) min_y =
                DOOM.levelLoader.vertexes[i].y else if (DOOM.levelLoader.vertexes[i].y > max_y) max_y =
                DOOM.levelLoader.vertexes[i].y
        }
        max_w = max_x - min_x
        max_h = max_y - min_y
        min_w = 2 * Defines.PLAYERRADIUS
        min_h = 2 * Defines.PLAYERRADIUS
        a = fixed_t.FixedDiv(f_w shl fixed_t.FRACBITS, max_w)
        b = fixed_t.FixedDiv(f_h shl fixed_t.FRACBITS, max_h)
        min_scale_mtof = if (a < b) a else b
        if (min_scale_mtof < 0) {
            min_scale_mtof = MINIMUM_VIABLE_SCALE
        }
        max_scale_mtof = fixed_t.FixedDiv(f_h shl fixed_t.FRACBITS, 2 * Defines.PLAYERRADIUS)
    }

    fun changeWindowLoc() {
        if (m_paninc!!.x != 0 || m_paninc!!.y != 0) {
            followplayer = false
            f_oldloc!!.x = Limits.MAXINT
        }
        m_x += m_paninc!!.x!!
        m_y += m_paninc!!.y!!
        if (m_x + m_w / 2 > max_x) m_x = max_x - m_w / 2 else if (m_x + m_w / 2 < min_x) m_x = min_x - m_w / 2
        if (m_y + m_h / 2 > max_y) m_y = max_y - m_h / 2 else if (m_y + m_h / 2 < min_y) m_y = min_y - m_h / 2
        m_x2 = m_x + m_w
        m_y2 = m_y + m_h
    }

    fun initVariables() {
        var pnum: Int
        DOOM.automapactive = true
        f_oldloc!!.x = Limits.MAXINT
        amclock = 0
        lightlev = 0
        m_paninc!!.y = 0
        m_paninc!!.x = m_paninc!!.y
        ftom_zoommul = fixed_t.FRACUNIT
        mtof_zoommul = fixed_t.FRACUNIT
        m_w = FTOM(f_w)
        m_h = FTOM(f_h)

        // find player to center on initially
        if (!DOOM.playeringame[DOOM.consoleplayer.also { pnum = it }]) {
            pnum = 0
            while (pnum < Limits.MAXPLAYERS) {
                println(pnum)
                if (DOOM.playeringame[pnum]) break
                pnum++
            }
        }
        plr = DOOM.players[pnum] as player_t
        m_x = plr!!.mo.x - (m_w / 2)
        m_y = plr!!.mo.y - m_h / 2
        changeWindowLoc()

        old_m_x = m_x
        old_m_y = m_y
        old_m_w = m_w
        old_m_h = m_h

        DOOM.statusBar.NotifyAMEnter()
    }

    fun loadPics() {
        var i: Int
        var namebuf: String
        i = 0
        while (i < 10) {
            namebuf = "AMMNUM$i"
            marknums[i] = DOOM.wadLoader.CachePatchName(namebuf)
            i++
        }
    }

    fun unloadPics() {
        var i: Int
        i = 0
        while (i < 10) {
            DOOM.wadLoader.UnlockLumpNum(marknums[i])
            i++
        }
    }

    fun clearMarks() {
        var i: Int
        i = 0
        while (i < AM_NUMMARKPOINTS) {
            markpoints[i].x = -1
            i++
        }
        markpointnum = 0
    }

    fun LevelInit() {
        leveljuststarted = 0
        f_y = 0
        f_x = f_y
        f_w = finit_width
        f_h = finit_height
        f_rect = Rectangle(0, 0, f_w, f_h)
        clearMarks()
        findMinMaxBoundaries()
        scale_mtof = fixed_t.FixedDiv(min_scale_mtof, MINIMUM_SCALE)
        if (scale_mtof > max_scale_mtof) scale_mtof = min_scale_mtof
        scale_ftom = fixed_t.FixedDiv(fixed_t.FRACUNIT, scale_mtof)
        plotter!!.setThickness(
            Math.min(MTOF(fixed_t.FRACUNIT), DOOM.graphicSystem.scalingX),
            Math.min(MTOF(fixed_t.FRACUNIT), DOOM.graphicSystem.scalingY)
        )
    }

    fun minOutWindowScale() {
        scale_mtof = min_scale_mtof
        scale_ftom = fixed_t.FixedDiv(fixed_t.FRACUNIT, scale_mtof)
        plotter!!.setThickness(DOOM.graphicSystem.scalingX, DOOM.graphicSystem.scalingY)
        activateNewScale()
    }
    fun maxOutWindowScale() {
        scale_mtof = max_scale_mtof
        scale_ftom = fixed_t.FixedDiv(fixed_t.FRACUNIT, scale_mtof)
        plotter!!.setThickness(0, 0)
        activateNewScale()
    }

    private fun changeWindowScale() {
        scale_mtof = fixed_t.FixedMul(scale_mtof, mtof_zoommul)
        scale_ftom = fixed_t.FixedDiv(fixed_t.FRACUNIT, scale_mtof)
        if (scale_mtof < min_scale_mtof) minOutWindowScale() else if (scale_mtof > max_scale_mtof) maxOutWindowScale() else activateNewScale()
    }

    private fun doFollowPlayer() {
        if (f_oldloc!!.x != plr!!.mo.x || f_oldloc!!.y != plr!!.mo.y) {
            m_x = FTOM(MTOF(plr!!.mo.x)) - m_w / 2
            m_y = FTOM(MTOF(plr!!.mo.y)) - m_h / 2
            m_x2 = m_x + m_w
            m_y2 = m_y + m_h
            f_oldloc!!.x = plr!!.mo.x
            f_oldloc!!.y = plr!!.mo.y
        }
    }

    private fun updateLightLev() {
        if (amclock % 6 == 0) {
            val sourceLength: Int = NUMLITES
            val intermeditate = DOOM.graphicSystem.convertPalettedBlock(0.toByte())
            litedColorSources.forEach { (c: Color?, source: V) ->
                GenericCopy.memcpy(source, sourceLength - 1, intermeditate, 0, 1)
                GenericCopy.memcpy(source, 0, source, 1, sourceLength - 1)
                GenericCopy.memcpy(intermeditate, 0, source, 0, 1)
            }
        }
    }

    private fun DOOUTCODE(mx: Int, my: Int): Int {
        var oc = 0
        if (my < 0) oc = oc or TOP else if (my >= f_h) oc = oc or BOTTOM
        if (mx < 0) oc = oc or LEFT else if (mx >= f_w) oc = oc or RIGHT
        return oc
    }

    private fun clipMline(ml: mline_t, fl: fline_t): Boolean {
        var outcode1 = 0
        var outcode2 = 0
        var outside: Int
        var dx: Int
        var dy: Int
        if (ml.ay!! > m_y2) outcode1 = TOP else if (ml.ay!! < m_y) outcode1 = BOTTOM
        if (ml.by!! > m_y2) outcode2 = TOP else if (ml.by!! < m_y) outcode2 = BOTTOM
        if (outcode1 and outcode2 != 0) return false // trivially outside
        if (ml.ax!! < m_x) outcode1 = outcode1 or LEFT else if (ml.ax!! > m_x2) outcode1 = outcode1 or RIGHT
        if (ml.bx!! < m_x) outcode2 = outcode2 or LEFT else if (ml.bx!! > m_x2) outcode2 = outcode2 or RIGHT
        if (outcode1 and outcode2 != 0) return false
        fl.ax = CXMTOF(ml.ax!!)
        fl.ay = CYMTOF(ml.ay!!)
        fl.bx = CXMTOF(ml.bx!!)
        fl.by = CYMTOF(ml.by!!)
        outcode1 = DOOUTCODE(fl.ax!!, fl.ay!!)
        outcode2 = DOOUTCODE(fl.bx!!, fl.by!!)
        if (outcode1 and outcode2 != 0) return false
        while (outcode1 or outcode2 != 0) {
            outside = if (outcode1 != 0) outcode1 else outcode2
            if (outside and TOP != 0) {
                dy = fl.ay!! - fl.by!!
                dx = fl.bx!! - fl.ax!!
                tmpx = fl.ax!! + dx * fl.ay!! / dy
                tmpy = 0
            } else if (outside and BOTTOM != 0) {
                dy = fl.ay!! - fl.by!!
                dx = fl.bx!! - fl.ax!!
                tmpx = fl.ax!! + dx * (fl.ay!! - f_h) / dy
                tmpy = f_h - 1
            } else if (outside and RIGHT != 0) {
                dy = fl.by!! - fl.ay!!
                dx = fl.bx!! - fl.ax!!
                tmpy = fl.ay!! + dy * (f_w - 1 - fl.ax!!) / dx
                tmpx = f_w - 1
            } else if (outside and LEFT != 0) {
                dy = fl.by!! - fl.ay!!
                dx = fl.bx!! - fl.ax!!
                tmpy = fl.ay!! + dy * -fl.ax!! / dx
                tmpx = 0
            }
            if (outside == outcode1) {
                fl.ax = tmpx
                fl.ay = tmpy
                outcode1 = DOOUTCODE(fl.ax!!, fl.ay!!)
            } else {
                fl.bx = tmpx
                fl.by = tmpy
                outcode2 = DOOUTCODE(fl.bx!!, fl.by!!)
            }
            if (outcode1 and outcode2 != 0) return false
        }
        return true
    }

    private fun drawMline(ml: mline_t, colorSource: V) {
        if (clipMline(ml, fl)) {
            DOOM.graphicSystem
                .drawLine(
                    plotter!!
                        .setColorSource(colorSource, 0)
                        .setPosition(fl.ax!!, fl.ay!!),
                    fl.bx!!, fl.by!!
                )
        }
    }

    private fun drawGrid(colorSource: V) {
        var x: Int
        var y: Int
        var start: Int
        var end: Int
        start = m_x
        if ((start - DOOM.levelLoader.bmaporgx) % (Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS) != 0) start += ((Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS)
                - (start - DOOM.levelLoader.bmaporgx) % (Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS))
        end = m_x + m_w
        ml.ay = m_y
        ml.by = m_y + m_h
        x = start
        while (x < end) {
            ml.ax = x
            ml.bx = x
            drawMline(ml, colorSource)
            x += Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS
        }
        start = m_y
        if ((start - DOOM.levelLoader.bmaporgy) % (Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS) != 0) start += ((Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS)
                - (start - DOOM.levelLoader.bmaporgy) % (Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS))
        end = m_y + m_h
        ml.ax = m_x
        ml.bx = m_x + m_w
        y = start
        while (y < end) {
            ml.ay = y
            ml.by = y
            drawMline(ml, colorSource)
            y += Defines.MAPBLOCKUNITS shl fixed_t.FRACBITS
        }
    }

    private fun drawWalls() {
        val teleColorSource = litedColorSources[TELECOLORS]
        val wallColorSource = litedColorSources[WALLCOLORS]
        val fdWallColorSource = litedColorSources[FDWALLCOLORS]
        val cdWallColorSource = litedColorSources[CDWALLCOLORS]
        val tsWallColorSource = litedColorSources[TSWALLCOLORS]
        val secretWallColorSource = litedColorSources[SECRETWALLCOLORS]
        for (i in 0 until DOOM.levelLoader.numlines) {
            l.ax = DOOM.levelLoader.lines[i].v1x
            l.ay = DOOM.levelLoader.lines[i].v1y
            l.bx = DOOM.levelLoader.lines[i].v2x
            l.by = DOOM.levelLoader.lines[i].v2y
            if ((cheating != 0 || (DOOM.levelLoader.lines[i].flags != 0.toShort()) && line_t.ML_MAPPED != 0)) {
                if (DOOM.levelLoader.lines[i].flags != 0.toShort() && LINE_NEVERSEE != 0.toShort() && cheating.inv() != 0) continue
                if (DOOM.levelLoader.lines[i].backsector == null) {
                    drawMline(l, wallColorSource!!)
                } else {
                    if (DOOM.levelLoader.lines[i].special.toInt() == 39) { // teleporters
                        drawMline(l, teleColorSource!!)
                    } else if ((DOOM.levelLoader.lines[i].flags != 0.toShort()) && (line_t.ML_SECRET != 0)) // secret
                    // door
                    {
                        if (cheating != 0) drawMline(l, secretWallColorSource!!) else drawMline(l, wallColorSource!!)
                    } else if (DOOM.levelLoader.lines[i].backsector.floorheight != DOOM.levelLoader.lines[i].frontsector.floorheight) {
                        drawMline(l, fdWallColorSource!!)
                    } else if (DOOM.levelLoader.lines[i].backsector.ceilingheight != DOOM.levelLoader.lines[i].frontsector.ceilingheight) {
                        drawMline(l, cdWallColorSource!!)
                    } else if (cheating != 0) {
                        drawMline(l, tsWallColorSource!!)
                    }
                }
            } else if (plr!!.powers[Defines.pw_allmap] != 0) {
                if ((DOOM.levelLoader.lines[i].flags != 0.toShort()) && (LINE_NEVERSEE == 0.toShort())) litedColorSources[MAPPOWERUPSHOWNCOLORS]?.let {
                    drawMline(
                        l,
                        it
                    )
                }
            }
        }
    }

    private fun rotate(x: Int, y: Int, a: Int) {
        rotx = fixed_t.FixedMul(x, Tables.finecosine[a]) - fixed_t.FixedMul(y, Tables.finesine[a])
        roty = fixed_t.FixedMul(x, Tables.finesine[a]) + fixed_t.FixedMul(y, Tables.finecosine[a])
    }

    private fun drawLineCharacter(lineguy: Array<mline_t>, lineguylines: Int, scale: Int, angle: Int, colorSource: V, x: Int, y: Int) {
        var i: Int
        val rotate = angle != 0
        val l = mline_t()
        i = 0
        while (i < lineguylines) {
            l.ax = lineguy[i].ax
            l.ay = lineguy[i].ay
            if (scale != 0) {
                l.ax = fixed_t.FixedMul(scale, l.ax!!)
                l.ay = fixed_t.FixedMul(scale, l.ay!!)
            }
            if (rotate) {
                rotate(l.ax!!, l.ay!!, angle)
                l.ax = rotx
                l.ay = roty
            }
            l.ax = l.ax?.plus(x)
            l.ay = l.ay?.plus(y)
            l.bx = lineguy[i].bx
            l.by = lineguy[i].by
            if (scale != 0) {
                l.bx = fixed_t.FixedMul(scale, l.bx!!)
                l.by = fixed_t.FixedMul(scale, l.by!!)
            }
            if (rotate) {
                rotate(l.bx!!, l.by!!, angle)
                l.bx = rotx
                l.by = roty
            }
            l.bx = l.bx?.plus(x)
            l.by = l.by?.plus(y)
            drawMline(l, colorSource)
            i++
        }
    }

    fun drawPlayers() {
        var p: player_t
        var their_color = -1
        var colorSource: V?

        if (!DOOM.netgame) {
            if (cheating != 0) fixedColorSources[Color.WHITE]?.let {
                drawLineCharacter(
                    cheat_player_arrow!!, NUMCHEATPLYRLINES, 0,
                    Tables.toBAMIndex(plr!!.mo.angle), it, plr!!.mo.x,
                    plr!!.mo.y
                )
            } else fixedColorSources[Color.WHITE]?.let {
                drawLineCharacter(
                    player_arrow!!, NUMPLYRLINES, 0,
                    Tables.toBAMIndex(plr!!.mo.angle), it, plr!!.mo.x,
                    plr!!.mo.y
                )
            }
            return
        }
        for (i in 0 until Limits.MAXPLAYERS) {
            their_color++
            p = DOOM.players[i]
            if (DOOM.deathmatch && !DOOM.singledemo && p !== plr) continue
            if (!DOOM.playeringame[i]) continue
            colorSource =
                if (p.powers[Defines.pw_invisibility] != 0) fixedColorSources[Color.CLOSE_TO_BLACK] else fixedColorSources[THEIR_COLORS[their_color]]
            drawLineCharacter(player_arrow!!, NUMPLYRLINES, 0, p.mo.angle.toInt(), colorSource!!, p.mo.x, p.mo.y)
        }
    }

    fun drawThings(colors: Color?, colorrange: Int) {
        var t: mobj_t?
        val colorSource = litedColorSources[colors]
        for (i in 0 until DOOM.levelLoader.numsectors) {
            t = DOOM.levelLoader.sectors[i].thinglist
            while (t != null) {
                drawLineCharacter(
                    thintriangle_guy!!, NUMTHINTRIANGLEGUYLINES,
                    16 shl fixed_t.FRACBITS, Tables.toBAMIndex(t.angle), colorSource!!, t.x, t.y
                )
                t = t.snext as mobj_t
            }
        }
    }

    fun drawMarks() {
        var fx: Int
        var fy: Int
        var w: Int
        var h: Int
        for (i in 0 until AM_NUMMARKPOINTS) {
            if (markpoints[i].x != -1) {
                w = marknums[i]!!.width.toInt()
                h = marknums[i]!!.height.toInt()
                fx = CXMTOF(markpoints[i].x!!)
                fy = CYMTOF(markpoints[i].y!!)
                if (fx >= f_x && fx <= f_w - w && fy >= f_y && fy <= f_h - h) DOOM.graphicSystem.DrawPatchScaled(
                    DoomScreen.FG,
                    marknums[i], DOOM.vs, fx, fy, DoomGraphicSystem.V_NOSCALESTART
                )
            }
        }
    }

    private fun drawCrosshair(colorSource: V) {
    }








    override fun Repalette() {
        GENERATE_LITE_LEVELS_FOR.stream()
            .forEach { c: Color ->
                if (c.liteBlock != null) {
                    litedColorSources[c] = DOOM.graphicSystem.convertPalettedBlock(*c.liteBlock)
                }
            }

        Arrays.stream(Color.values())
            .forEach { c: Color ->
                val converted: V = DOOM.graphicSystem.convertPalettedBlock(c.value)
                val extended: V = java.lang.reflect.Array.newInstance(converted!!::class.java.componentType, NUMLITES) as V
                GenericCopy.memset(extended, 0, NUMLITES, converted, 0, 1)
                fixedColorSources[c] = extended
            }
    }

    @AM_Map.C(AM_Map.AM_Responder)
    override fun Responder(ev: event_t?): Boolean {
        var rc: Boolean
        rc = false
        if (!DOOM.automapactive) {
            if (ev!!.isKey(AM_STARTKEY, evtype_t.ev_keyup)) {
                Start()
                DOOM.viewactive = false
                rc = true
            }
        } else if (ev!!.isType(evtype_t.ev_keydown)) {
            rc = true
            if (ev.isKey(AM_PANRIGHTKEY)) {
                if (!followplayer) m_paninc!!.x = FTOM(F_PANINC) else rc = false
            } else if (ev.isKey(AM_PANLEFTKEY)) {
                if (!followplayer) m_paninc!!.x = -FTOM(F_PANINC) else rc = false
            } else if (ev.isKey(AM_PANUPKEY)) {
                if (!followplayer) m_paninc!!.y = FTOM(F_PANINC) else rc = false
            } else if (ev.isKey(AM_PANDOWNKEY)) {
                if (!followplayer) m_paninc!!.y = -FTOM(F_PANINC) else rc = false
            } else if (ev.isKey(AM_ZOOMOUTKEY)) {
                mtof_zoommul = M_ZOOMOUT
                ftom_zoommul = M_ZOOMIN
            } else if (ev.isKey(AM_ZOOMINKEY)) {
                mtof_zoommul = M_ZOOMIN
                ftom_zoommul = M_ZOOMOUT
            } else if (ev.isKey(AM_GOBIGKEY)) {
                bigstate = !bigstate
                if (bigstate) {
                    saveScaleAndLoc()
                    minOutWindowScale()
                } else restoreScaleAndLoc()
            } else if (ev.isKey(AM_FOLLOWKEY)) {
                followplayer = !followplayer
                f_oldloc!!.x = Limits.MAXINT
                plr!!.message = if (followplayer) englsh.AMSTR_FOLLOWON else englsh.AMSTR_FOLLOWOFF
            } else if (ev.isKey(AM_GRIDKEY)) {
                grid = !grid
                plr!!.message = if (grid) englsh.AMSTR_GRIDON else englsh.AMSTR_GRIDOFF
            } else if (ev.isKey(AM_MARKKEY)) {
                buffer = englsh.AMSTR_MARKEDSPOT + " " + markpointnum
                plr!!.message = buffer
                addMark()
            } else if (ev.isKey(AM_CLEARMARKKEY)) {
                clearMarks()
                plr!!.message = englsh.AMSTR_MARKSCLEARED
            } else {
                cheatstate = false
                rc = false
            }
            if (!DOOM.deathmatch && ev.ifKeyAsciiChar { key: Int -> cheat_amap.CheckCheat(key) }) {
                rc = false
                cheating = (cheating + 1) % 3
            }
            if (ev.ifKeyAsciiChar { key: Int -> cheat_strobe.CheckCheat(key) }) {
                DOOM.mapstrobe = !DOOM.mapstrobe
            }
        } else if (ev.isType(evtype_t.ev_keyup)) {
            rc = false
            if (ev.isKey(AM_PANRIGHTKEY)) {
                if (!followplayer) m_paninc!!.x = 0
            } else if (ev.isKey(AM_PANLEFTKEY)) {
                if (!followplayer) m_paninc!!.x = 0
            } else if (ev.isKey(AM_PANUPKEY)) {
                if (!followplayer) m_paninc!!.y = 0
            } else if (ev.isKey(AM_PANDOWNKEY)) {
                if (!followplayer) m_paninc!!.y = 0
            } else if (ev.isKey(AM_ZOOMOUTKEY) || ev.isKey(AM_ZOOMINKEY)) {
                mtof_zoommul = fixed_t.FRACUNIT
                ftom_zoommul = fixed_t.FRACUNIT
            } else if (ev.isKey(AM_ENDKEY)) {
                bigstate = false
                DOOM.viewactive = true
                Stop()
            }
        }

        return rc
    }

    override fun Ticker() {
        if (!DOOM.automapactive || DOOM.menuactive) return
        amclock++
        if (followplayer) doFollowPlayer()
        if (ftom_zoommul != fixed_t.FRACUNIT) changeWindowScale()
        if (m_paninc!!.x!! or m_paninc!!.y!! != 0) changeWindowLoc()
        if (DOOM.mapstrobe) updateLightLev()
    }

    override fun Drawer() {
        if (!DOOM.automapactive) return
        if (overlay < 1) DOOM.graphicSystem.FillRect(DoomScreen.FG, f_rect, BACKGROUND.value)

        if (grid) fixedColorSources[GRIDCOLORS]?.let { drawGrid(it) }

        drawWalls()
        drawPlayers()
        if (cheating == 2) drawThings(THINGCOLORS, IAutoMap.THINGRANGE)
        fixedColorSources[CROSSHAIRCOLORS]?.let { drawCrosshair(it) }

        drawMarks()
    }

    override fun Stop() {
        unloadPics()
        DOOM.automapactive = false
        DOOM.statusBar.NotifyAMExit()
        stopped = true
    }

    override fun Start() {
        if (!stopped) Stop()

        stopped = false
        if (lastlevel != DOOM.gamemap || lastepisode != DOOM.gameepisode) {
            LevelInit()
            lastlevel = DOOM.gamemap
            lastepisode = DOOM.gameepisode
        }
        initVectorGraphics()
        LevelInit()
        initVariables()
        loadPics()
    }

}

enum class Color(val range: Int, val value: Byte) {
    CLOSE_TO_BLACK(1, 246.toByte()), REDS(16, 176.toByte()), BLUES(8, 200.toByte()), GREENS(16, 112.toByte()), GRAYS(
        16,
        96.toByte()
    ),
    BROWNS(16, 64.toByte()), YELLOWS(8, 160.toByte()), BLACK(1, 0.toByte()), WHITE(1, 4.toByte()), GRAYS_DARKER_25(
        13,
        (GRAYS.value + 4).toByte()
    ),
    DARK_GREYS(8, (GRAYS.value + GRAYS.range / 2).toByte()), DARK_REDS(8, (REDS.value + REDS.range / 2).toByte());

    val liteBlock: ByteArray?

    companion object {
        val LITE_LEVELS_FULL_RANGE = intArrayOf(0, 4, 7, 10, 12, 14, 15, 15)
        val LITE_LEVELS_HALF_RANGE = intArrayOf(0, 2, 3, 5, 6, 6, 7, 7)

        init {
            for (c in values()) {
                when (c.range) {
                    16 -> {
                        var i = 0
                        while (i < NUMLITES) {
                            c.liteBlock!![i] = (c.value + LITE_LEVELS_FULL_RANGE[i]).toByte()
                            ++i
                        }
                    }
                    8 -> {
                        var i = 0
                        while (i < LITE_LEVELS_HALF_RANGE.size) {
                            c.liteBlock!![i] = (c.value + LITE_LEVELS_HALF_RANGE[i]).toByte()
                            ++i
                        }
                    }
                }
            }
        }
    }

    init {
        if (range >= NUMLITES)
            liteBlock = ByteArray(8)
        else
            liteBlock = null
    }
}