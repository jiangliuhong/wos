package top.jiangliuhong.wos.sql.support

interface EnumSupport {

    fun serialize(obj: Enum<*>?): Any?

    fun deserialize(clzz: Class<Enum<*>?>?, obj: Any?): Enum<*>?
}