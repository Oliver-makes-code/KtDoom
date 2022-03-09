package automap

class fline_t {

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
    constructor()
}