package automap

class mline_t {
    @JvmField var ax: Int? = null
    @JvmField var ay: Int? = null
    @JvmField var bx: Int? = null
    @JvmField var by: Int? = null

    constructor(ax: Int, ay: Int, bx: Int, by: Int) {
        this.ax = ax
        this.ay = ay
        this.bx = bx
        this.by = by
    }
    constructor(ax: Double, ay: Double, bx: Double, by: Double) {
        this.ax = ax.toInt()
        this.ay = ay.toInt()
        this.bx = bx.toInt()
        this.by = by.toInt()
    }
    constructor() {
        this.ax = 0
        this.ay = 0
        this.bx = 0
        this.by = 0
    }
}