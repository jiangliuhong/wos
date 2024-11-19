package top.jiangliuhong.wos.sql.util

class SqlStringUtil {

    companion object {
        fun isNotNull(str: String): Boolean {
            return !isNullOrEmpty(str)
        }

        fun isNullOrEmpty(str: String?): Boolean {
            return str == null || str == "" || str.trim { it <= ' ' } == ""
        }

        fun isNullOrEmpty(obj: Any?): Boolean {
            if (obj == null) return true
            val clz: Class<*> = obj.javaClass
            if (clz == String::class.java) {
                return isNullOrEmpty(obj.toString())
            }
            return false
        }
    }

}