package automap

class mpoint_t {
    @JvmField var x: Int? = null
    @JvmField var y: Int? = null

    init {
        x = 0
        y = 0
    }

    override fun toString(): String {
        return Integer.toHexString(x!!) + " , " + Integer.toHexString(y!!)
    }
}