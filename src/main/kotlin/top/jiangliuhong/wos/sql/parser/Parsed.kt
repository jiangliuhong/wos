package top.jiangliuhong.wos.sql.parser

import java.util.*
import java.lang.reflect.Field;
import java.sql.Timestamp;

class Parsed {

    private var clzz: Class<*>? = null
    private val tableName: String? = null
    private var originTable: String? = null
    private val isNoSpec = true

    private var key: String? = null
    private var keyField: Field? = null
    private var tagKeyField: Field? = null
    private val tagFieldList: List<Field> = ArrayList<Field>()

    private var beanElementList: List<BeanElement>? = null

    private val elementMap: MutableMap<String, BeanElement> = HashMap<String, BeanElement>()
    private val propertyMapperMap: MutableMap<String, String> = HashMap()
    private val mapperPropertyMapLower: MutableMap<String, String> = HashMap()

    private val isNoCache = false

    fun getClzz(): Class<*>? {
        return clzz
    }

    fun setClzz(clzz: Class<*>?) {
        this.clzz = clzz
    }

    fun Parsed(clzz: Class<*>?) {
        this.clzz = clzz
    }


    fun getElement(property: String): BeanElement? {
        return elementMap[property]
    }

    fun getElementExisted(property: String): BeanElement {
        val be: BeanElement = elementMap[property] ?: throw ParsingException(
            "Not exist: "
                    + property
        )
        return be
    }

    fun getKey(): String? {
        return key
    }

    fun setKeyField(keyField: Field) {
        this.keyField = keyField
        this.key = keyField.getName()
    }

    fun getKeyField(): Field? {
        return keyField
    }

    fun getTagKeyField(): Field? {
        return tagKeyField
    }

    fun setTagKeyField(tagKeyField: Field?) {
        this.tagKeyField = tagKeyField
    }

    fun getTagFieldList(): List<Field> {
        return tagFieldList
    }

    fun isAutoIncreaseId(keyOneValue: Long?): Boolean {
        if (keyField == null) return false
        val keyOneType: Class<*> = keyField.getType()
        return (keyOneType != String::class.java && keyOneType != Date::class.java && keyOneType != Timestamp::class.java) && (keyOneValue == null || keyOneValue == 0L)
    }

    fun tryToGetLongKey(obj: Any?): Long? {
        var keyOneValue = 0L
        if (keyField == null) return null
        val keyOneType: Class<*> = keyField.getType()
        if (keyOneType != String::class.java && keyOneType != Date::class.java && keyOneType != Timestamp::class.java) {
            try {
                val keyValue: Any = keyField.get(obj)
                if (keyValue != null) {
                    keyOneValue = keyValue.toString().toLong()
                }
            } catch (e: Exception) {
                throw ParsingException(e.message)
            }
        }
        return keyOneValue
    }

    fun getBeanElementList(): List<BeanElement>? {
        return beanElementList
    }

    fun setOriginTable(originTable: String?) {
        this.originTable = originTable
    }

    fun reset(beanElementList: List<BeanElement>?) {
        this.beanElementList = beanElementList
        propertyMapperMap.clear()
        elementMap.clear()
        mapperPropertyMapLower.clear()
        for (e in this.beanElementList) {
            val property: String = e.getProperty()
            val mapper: String = e.getMapper()
            elementMap[property] = e
            propertyMapperMap[property] = mapper
            mapperPropertyMapLower[mapper.lowercase(Locale.getDefault())] = property
        }
    }


    fun getTableName(alia: String): String? {
        if (SqliStringUtil.isNullOrEmpty(alia)) return tableName
        if (!alia.equals(getClzName(), ignoreCase = true)) return alia
        return tableName
    }
}