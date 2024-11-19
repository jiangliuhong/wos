package top.jiangliuhong.wos.sql.util

import top.jiangliuhong.wos.sql.support.EnumSupport


class EnumUtil {
    companion object {
        var enumSupport: EnumSupport? = null
        fun setEnumSupport(es: EnumSupport?) {
            enumSupport = es
        }

        fun isEnum(clzz: Class<*>): Boolean {
            val superClzz = clzz.superclass
            return clzz.isEnum || (superClzz != null && superClzz.isEnum)
        }

        fun serialize(enumObj: Enum<*>?): Any? {
            return enumSupport!!.serialize(enumObj)
        }

        fun deserialize(clzz: Class<Enum<*>?>?, enumNameOrCode: Any?): Enum<*>? {
            return enumSupport!!.deserialize(clzz, enumNameOrCode)
        }

        fun filterInComplexScriptSimply(maybeEnum: Any?): Any? {
            if (maybeEnum == null) return null
            if (isEnum(maybeEnum.javaClass)) {
                return serialize(maybeEnum as Enum<*>)
            }
            return maybeEnum
        }

        fun serialize(clzz: Class<Enum<*>?>?, strOrEnum: Any?): Any? {
            var strOrEnum = strOrEnum
            if (strOrEnum is String) {
                strOrEnum = deserialize(clzz, strOrEnum)
            }
            return serialize(strOrEnum as Enum<*>?)
        }
    }
}