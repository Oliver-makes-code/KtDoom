package i

interface IDiskDrawer : IDrawer {
    fun setReading(reading: Int)
    fun isReading(): Boolean
    fun Init()
    fun justDoneReading(): Boolean
}