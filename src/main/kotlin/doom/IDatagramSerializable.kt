package doom

interface IDatagramSerializable {
    fun pack(): ByteArray?
    fun pack(buf: ByteArray?, offset: Int)
    fun unpack(buf: ByteArray?)
    fun unpack(buf: ByteArray?, offset: Int)
    fun cached(): ByteArray?
}