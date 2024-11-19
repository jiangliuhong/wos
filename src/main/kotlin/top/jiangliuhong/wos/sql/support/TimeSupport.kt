package top.jiangliuhong.wos.sql.support

import top.jiangliuhong.wos.sql.builder.internal.Bb
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class TimeSupport {

    companion object {
        fun afterReadTime(propertyType: Class<*>, obj: Any): Any {
            var obj = obj
            if (obj is LocalDateTime) {
                if (propertyType == Date::class.java) {
                    val instant = obj.atZone(ZoneId.systemDefault()).toInstant()
                    obj = Date.from(instant)
                } else if (propertyType == Timestamp::class.java) {
                    val instant = obj.atZone(ZoneId.systemDefault()).toInstant()
                    obj = Timestamp.from(instant)
                } else if (propertyType == LocalDate::class.java) {
                    obj = obj.toLocalDate()
                }
            } else if (obj is Timestamp) {
                if (propertyType == LocalDateTime::class.java) {
                    obj = Instant.ofEpochMilli((obj as Timestamp).getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()
                } else if (propertyType == Date::class.java) {
                    obj = Date((obj as Timestamp).getTime())
                } else if (propertyType == LocalDate::class.java) {
                    obj = Instant.ofEpochMilli((obj as Timestamp).getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                }
            }
            return obj
        }

        fun testWriteNumberValueToTime(propertyType: Class<*>, bb: Bb): Boolean {
            if (propertyType == LocalDateTime::class.java) {
                val v = bb.value
                if (v is Long || v is Int) {
                    if (v.toString().toLong() == 0L) {
                        bb.value = null
                    } else {
                        val time = Instant.ofEpochMilli(toLongValue(v)).atZone(ZoneId.systemDefault()).toLocalDateTime()
                        bb.value = time
                    }
                }
                return true
            } else if (propertyType == Date::class.java) {
                val v = bb.value
                if (v is Long || v is Int) {
                    if (v.toString().toLong() == 0L) {
                        bb.value = null
                    } else {
                        bb.value = Date(toLongValue(v))
                    }
                }
                return true
            } else if (propertyType == Timestamp::class.java) {
                val v = bb.value
                if (v is Long || v is Int) {
                    if (v.toString().toLong() == 0L) {
                        bb.value = null
                    } else {
                        bb.value = Timestamp(toLongValue(v))
                    }
                }
                return true
            } else if (propertyType == LocalDate::class.java) {
                val v = bb.value
                if (v is Long || v is Int) {
                    if (v.toString().toLong() == 0L) {
                        bb.value = null
                    } else {
                        val date = Instant.ofEpochMilli(toLongValue(v)).atZone(ZoneId.systemDefault()).toLocalDate()
                        bb.value = date
                    }
                }
                return true
            }
            return false
        }

        private fun toLongValue(v: Any): Long {
            return if (v is Int) {
                (v.toLong())
            } else {
                (v as Long)
            }
        }
    }
}