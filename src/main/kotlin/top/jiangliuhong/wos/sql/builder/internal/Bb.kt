package top.jiangliuhong.wos.sql.builder.internal

import top.jiangliuhong.wos.sql.consts.Op


class Bb(
    var c: Op?,
    var p: Op?,
    var key: String?,
    var value: Any?,
    var subList: List<Bb>?
) {

    constructor() : this(null, null, null, null, null)

    constructor(isOr: Boolean) : this() {
        if (isOr) {
            c = Op.OR
        } else {
            c = Op.AND
        }
    }

    companion object {
        fun of(c: Op?, p: Op?, key: String, value: Any): Bb {
            return Bb().apply { this.c = c; this.p = p; this.key = key;this.value = value }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bb

        if (c != other.c) return false
        if (p != other.p) return false
        if (key != other.key) return false
        if (value != other.value) return false
        if (subList != other.subList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = c?.hashCode() ?: 0
        result = 31 * result + (p?.hashCode() ?: 0)
        result = 31 * result + (key?.hashCode() ?: 0)
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (subList?.hashCode() ?: 0)
        return result
    }


}