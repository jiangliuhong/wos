package top.jiangliuhong.wos.sql.builder

import top.jiangliuhong.wos.sql.builder.internal.Bb
import top.jiangliuhong.wos.sql.builder.internal.CondQ
import top.jiangliuhong.wos.sql.consts.Op
import top.jiangliuhong.wos.sql.consts.SqlScripts
import top.jiangliuhong.wos.sql.mapping.SqlNormalizer
import top.jiangliuhong.wos.sql.util.EnumUtil
import top.jiangliuhong.wos.sql.util.SqlStringUtil


open class CondBuilder : SqlNormalizer {
    private lateinit var instance: CondBuilder
    private var bbList: MutableList<Bb>? = null

    //private var isAbort: Boolean? = null
    private var isOr: Boolean? = null
    private var subsList: MutableList<List<Bb>>? = null

    constructor() {
        this.instance = CondBuilder()
    }

    constructor(bbList: MutableList<Bb>?) {
        this.bbList = bbList
    }

    fun init(bbList: MutableList<Bb>?) {
        this.bbList = bbList
    }

    private fun getSubsList(): List<List<Bb>>? {
        if (this.subsList == null) {
            this.subsList = mutableListOf()
        }
        return this.subsList
    }

    fun build(): CondQ {
        return CondQ { bbList!! }
    }

    fun bool(condition: Bool, thenBuilder: ThenBuilder): CondBuilder {
        if (condition.isOk()) {
            thenBuilder.build(this.instance)
        }
        return this.instance
    }

    fun any(any: AnyBuilder): CondBuilder {
        any.build(this.instance)
        return this.instance
    }

    fun eq(property: String, value: Any?): CondBuilder {
        return doGle(Op.EQ, property, value)
    }

    fun lt(property: String, value: Any?): CondBuilder {
        return doGle(Op.LT, property, value)
    }

    fun lte(property: String, value: Any?): CondBuilder {
        return doGle(Op.LTE, property, value)
    }

    fun gt(property: String, value: Any?): CondBuilder {
        return doGle(Op.GT, property, value)
    }

    fun gte(property: String, value: Any?): CondBuilder {
        return doGle(Op.GTE, property, value)
    }

    fun ne(property: String, value: Any?): CondBuilder {
        return doGle(Op.NE, property, value)
    }

    /**
     * like() default LIKE %value%
     * @param property
     * @param value
     */
    fun like(property: String, value: String): CondBuilder {
        if (SqlStringUtil.isNullOrEmpty(value)) {
            isOr()
            return instance
        }
        val likeValue: String = SqlScripts.LIKE_HOLDER + value + SqlScripts.LIKE_HOLDER
        return doLike(Op.LIKE, property, likeValue)
    }

    /**
     * like() default LIKE %value%, then likeLeft() LIKE value%
     * @param property
     * @param value
     */
    fun likeLeft(property: String, value: String): CondBuilder {
        if (SqlStringUtil.isNullOrEmpty(value)) {
            isOr()
            return instance
        }
        val likeValue = value + SqlScripts.LIKE_HOLDER
        return doLike(Op.LIKE, property, likeValue)
    }

    fun notLike(property: String, value: String): CondBuilder {
        if (SqlStringUtil.isNullOrEmpty(value)) {
            isOr()
            return instance
        }
        val likeValue: String = SqlScripts.LIKE_HOLDER + value + SqlScripts.LIKE_HOLDER
        return doLike(Op.NOT_LIKE, property, likeValue)
    }

    fun in_(property: String, list: List<Any>): CondBuilder {
        return doIn(Op.IN, property, list)
    }

    fun notIn(property: String, list: List<Any>): CondBuilder {
        return doIn(Op.NOT_IN, property, list)
    }

    fun nonNull(property: String): CondBuilder {
        return doNull(Op.IS_NOT_NULL, property)
    }

    fun isNull(property: String): CondBuilder {
        return doNull(Op.IS_NULL, property)
    }

    fun x(sqlSegment: String?, vararg values: Any): CondBuilder {
        if (SqlStringUtil.isNullOrEmpty(sqlSegment)) {
            isOr()
            return instance
        }

        val sql = normalizeSql(sqlSegment!!)

        var arr: Array<Any?>? = null
        val length = values.size
        arr = arrayOfNulls(length)
        for (i in 0 until length) {
            arr[i] = EnumUtil.filterInComplexScriptSimply(values[i])
        }

        val bb = Bb(isOr())
        bb.p = Op.X
        bb.key = sql
        bb.value = arr
        this.add(bb)

        return instance
    }

    fun or(): CondBuilder {
        this.isOr = true
        return this.instance
    }

    fun and(sub: SubCond): CondBuilder {
        return orAnd(Op.AND, sub)
    }

    fun or(sub: SubCond): CondBuilder {
        return orAnd(Op.OR, sub)
    }

    private fun orAnd(c: Op, s: SubCond): CondBuilder {
        val sub = builder()
        s.build(sub)
        val condQ: CondQ = sub.build()

        val subList: List<Bb> = condQ.getBbs()
        if (subList.isEmpty()) return instance

        val bb = Bb(isOr()).apply {
            this.p = Op.SUB
            this.c = c
            this.subList = subList
        }
        this.add(bb)

        return instance
    }

    private fun doGle(p: Op, property: String, value: Any?): CondBuilder {
        if (value == null) {
            isOr()
            return instance
        }
        if (SqlStringUtil.isNullOrEmpty(value)) {
            isOr()
            return instance
        }
        require(!(value is List<*> || value.javaClass.isArray)) { property + " " + p + " " + value + ", try " + property + " " + Op.IN + " (" + value + ")" }
        val bb = Bb(isOr()).apply {
            this.p = p
            this.key = property
            this.value = value
        }
        this.add(bb)

        return instance
    }

    private fun doLike(p: Op, property: String, likeWalue: String): CondBuilder {
        val bb = Bb(isOr()).apply {
            this.p = p
            this.key = property
            this.value = likeWalue
        }
        this.add(bb)
        return instance
    }

    private fun doIn(p: Op, property: String, list: List<Any>): CondBuilder {
        if (list.isEmpty()) {
            isOr()
            return instance
        }

        val tempList: MutableList<Any> = ArrayList()
        for (obj in list) {
            if (SqlStringUtil.isNullOrEmpty(obj)) continue
            if (!tempList.contains(obj)) {
                tempList.add(obj)
            }
        }

        if (tempList.isEmpty()) {
            isOr()
            return instance
        }

        val bb = Bb(isOr()).apply {
            this.p = p
            this.key = property
            this.value = tempList
        }
        this.add(bb)

        return instance
    }

    private fun doNull(p: Op, property: String): CondBuilder {
        if (SqlStringUtil.isNullOrEmpty(property)) {
            isOr()
            return instance
        }

        val bb = Bb(isOr()).apply {
            this.p = p
            this.key = property
            this.value = p
        }
        this.add(bb)

        return instance
    }

    private fun isOr(): Boolean {
        if (isOr!!) {
            isOr = false
            return true
        } else {
            return false
        }
    }

    private fun add(bb: Bb) {
        bbList!!.add(bb)
    }


    companion object {
        fun builder(bbList: MutableList<Bb>?): CondBuilder {
            return CondBuilder(bbList)
        }

        fun builder(): CondBuilder {
            return CondBuilder(ArrayList())
        }

    }
}