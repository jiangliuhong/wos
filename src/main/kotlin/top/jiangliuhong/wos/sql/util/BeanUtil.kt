package top.jiangliuhong.wos.sql.util

import java.util.*

class BeanUtil {

    companion object {
        fun getSetter(property: String): String {
            if (property.startsWith("is")) {
                val rest = property.substring(2)
                return "set$rest"
            }

            val a = property.substring(0, 1)
            val rest = property.substring(1)
            return "set" + a.uppercase(Locale.getDefault()) + rest
        }

        fun getByFirstLower(str: String): String {
            if (SqlStringUtil.isNullOrEmpty(str)) return str

            val a = str.substring(0, 1)
            val rest = str.substring(1)
            val result = a.lowercase(Locale.getDefault()) + rest
            return result
        }

        fun getProperty(methodName: String): String {
            if (methodName.startsWith("is")) return methodName
            val str = methodName.substring(3)
            return getByFirstLower(str)
        }

        fun getBooleanPropertyNoIs(property: String): String {
            if (property.startsWith("is")) {
                val str = property.substring(2)
                return getByFirstLower(str)
            }
            return property
        }
    }
}