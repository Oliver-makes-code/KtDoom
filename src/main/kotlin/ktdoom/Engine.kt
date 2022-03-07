package ktdoom

class Engine {
    companion object {
        lateinit var instance: Engine
    }

    constructor() {
        instance = this
    }
}