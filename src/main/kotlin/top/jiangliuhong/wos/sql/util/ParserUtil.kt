package top.jiangliuhong.wos.sql.util

import org.h2.util.geometry.GeometryUtils.X
import top.jiangliuhong.wos.sql.builder.internal.SqlScript
import top.jiangliuhong.wos.sql.consts.SqlFieldType
import top.jiangliuhong.wos.sql.parser.BeanElement
import top.jiangliuhong.wos.sql.parser.Parsed
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class ParserUtil {
    companion object {
        var SQL_KEYWORD_MARK: String = "`"
        private fun parseFieldsOfElementList(
            clz: Class<*>,
            filterMap: MutableMap<String, Field>,
            allMap: MutableMap<String, Field>
        ) {
            val fl: MutableList<Field> = ArrayList<Field>()

            if (clz.superclass != Any::class.java) {
                fl.addAll(Arrays.asList(*clz.superclass.declaredFields))
            }
            fl.addAll(Arrays.asList(*clz.declaredFields))

            /*
         * 排除transient
         */
            for (f in fl) {
                allMap[f.getName()] = f

                if (f.getModifiers() >= 128) {
                    filterMap[f.getName()] = f
                }

                /*
             * ignored anno
             */
                val p: X.Ignore = f.getAnnotation(X.Ignore::class.java)
                if (p != null) {
                    filterMap[f.getName()] = f
                }
            }
        }

        private fun parseMethodsOfElementList(clz: Class<*>, mns: MutableSet<String>, methodList: MutableList<Method>) {
            if (clz.superclass != Any::class.java) {
                methodList.addAll(Arrays.asList(*clz.superclass.declaredMethods))
            }
            methodList.addAll(Arrays.asList(*clz.declaredMethods)) // 仅仅XxxMapped子类

            for (m in methodList) {
                mns.add(m.getName())
            }
        }

        private fun parseFilterListOfElementList(
            filterList: MutableList<BeanElement>,
            mns: Set<String>,
            ml: List<Method>,
            allMap: Map<String, Field>
        ) {
            for (m in ml) {
                val name: String = m.getName()
                if (!(name.startsWith("set") || name.startsWith("get") || name.startsWith("is"))) continue

                var key = BeanUtil.getProperty(name)
                var be: BeanElement? = null
                for (b in filterList) {
                    if (key.startsWith("is")) {
                        if (!allMap.containsKey(key)) {
                            val noIs = BeanUtil.getBooleanPropertyNoIs(key)
                            if (allMap.containsKey(noIs)) {
                                key = noIs
                            }
                        }
                    }
                    if (b.property.equals(key)) {
                        be = b
                        break
                    }
                }
                if (be == null) {
                    be = BeanElement()
                    be.setter = key
                    filterList.add(be)
                }
                if (name.startsWith("set")) {
                    be.setter = name
                } else if (name.startsWith("get")) {
                    be.getter = name
                    be.clz = m.getReturnType()
                } else if (name.startsWith("is")) {
                    be.getter = name
                    be.clz = m.getReturnType()
                    //                be.setProperty(name);
                    val setter = BeanUtil.getSetter(name) // FIXME 可能有BUG
                    if (mns.contains(setter)) {
                        be.setter = setter
                    }
                }
            }
        }

        private fun filterElementList(filterList: MutableList<BeanElement>, filterMap: Map<String, Field>) {
            /*
         * 找出有setter 和 getter的一对
         */
            val ite = filterList.iterator()
            while (ite.hasNext()) { // BUG, 这里去掉了boolen属性
                val be = ite.next()
                if (!be.isPair()) {
                    ite.remove()
                }
            }

            /*
         * 去掉transient
         */
            for (key in filterMap.keys) {
                val beIte = filterList.iterator()
                while (beIte.hasNext()) {
                    val be = beIte.next()
                    if (be.property.equals(key)) {
                        beIte.remove()
                        break
                    }
                }
            }
        }

        private fun buildElementList(
            clz: Class<*>,
            filterList: List<BeanElement>,
            allMap: Map<String, Field>
        ): List<BeanElement> {
            val list: MutableList<BeanElement> = ArrayList()

            for (element in filterList) {
                parseAnno(clz, element, allMap[element.property])

                val ec: Class<*> = element.getClz()
                if (element.getSqlType() == null) {
                    if (ec == Long::class.javaPrimitiveType || ec == Long::class.java) {
                        element.setSqlType(SqlFieldType.LONG)
                        element.setLength(13)
                    } else if (ec == Int::class.javaPrimitiveType || ec == Int::class.java) {
                        element.setSqlType(SqlFieldType.INT)
                        element.setLength(11)
                    } else if (ec == BigInteger::class.java) {
                        element.setSqlType(SqlFieldType.BIG_INTEGER)
                        element.setLength(20)
                    } else if (ec == Double::class.javaPrimitiveType || ec == Double::class.java) {
                        element.setSqlType(SqlFieldType.DOUBLE)
                        element.setLength(13)
                    } else if (ec == Float::class.javaPrimitiveType || ec == Float::class.java) {
                        element.setSqlType(SqlFieldType.FLOAT)
                        element.setLength(13)
                    } else if (ec == Boolean::class.javaPrimitiveType || ec == Boolean::class.java) {
                        element.setSqlType(SqlFieldType.BYTE)
                        element.setLength(1)
                    } else if (ec == String::class.java) {
                        element.setSqlType(SqlFieldType.VARCHAR)
                        if (element.getLength() === 0) element.setLength(60)
                    } else if (ec == BigDecimal::class.java) {
                        element.setSqlType(SqlFieldType.DECIMAL)
                    } else if (ec == LocalDateTime::class.java || ec == LocalDate::class.java || ec == Date::class.java || ec == java.sql.Date::class.java || ec == Timestamp::class.java) {
                        element.setSqlType(SqlFieldType.DATE)
                    } else if (EnumUtil.isEnum(ec)) {
                        element.setSqlType(SqlFieldType.VARCHAR)
                        if (element.getLength() === 0) element.setLength(20)
                    } else {
                        element.setJson(true)
                        if (ec == MutableList::class.java) {
                            var field: Field? = null
                            try {
                                field = clz.getDeclaredField(element.property)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            val pt: ParameterizedType = field.getGenericType() as ParameterizedType

                            element.setGeneType(pt.getActualTypeArguments().get(0))
                        }
                        element.setSqlType(SqlFieldType.VARCHAR)
                        if (element.getLength() === 0) element.setLength(512)
                    }
                } else if (element.getSqlType().contains(SqlFieldType.TEXT)) {
                    element.setLength(0)
                } else {
                    element.setSqlType(SqlFieldType.VARCHAR)
                }

                list.add(element)
            }
            return list
        }

        private fun initMethodCache(clz: Class<*>, list: List<BeanElement>) {
            try {
                for (be in list) {
                    try {
                        be.setSetMethod(clz.getDeclaredMethod(be.getSetter(), be.getClz()))
                    } catch (e: NoSuchMethodException) {
                        be.setSetMethod(clz.superclass.getDeclaredMethod(be.getSetter(), be.getClz()))
                    }
                    try {
                        be.setGetMethod(clz.getDeclaredMethod(be.getGetter()))
                    } catch (e: NoSuchMethodException) {
                        be.setGetMethod(clz.superclass.getDeclaredMethod(be.getGetter()))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun parseElementList(clz: Class<*>): List<BeanElement> {
            val filterMap: MutableMap<String, Field> = HashMap<String, Field>()
            val allMap: MutableMap<String, Field> = HashMap<String, Field>()
            parseFieldsOfElementList(clz, filterMap, allMap) //Step 1

            val mns: MutableSet<String> = HashSet()
            val ml: MutableList<Method> = ArrayList<Method>()
            parseMethodsOfElementList(clz, mns, ml)


            val filterList: MutableList<BeanElement> = ArrayList()
            parseFilterListOfElementList(filterList, mns, ml, allMap)
            filterElementList(filterList, filterMap)

            val list = buildElementList(clz, filterList, allMap)

            initMethodCache(clz, list)

            return list
        }

        fun parseCacheableAnno(clz: Class<*>, parsed: Parsed) {
            val p: X.NoCache? = clz.getAnnotation(X.NoCache::class.java) as X.NoCache?
            if (p != null) {
                parsed.setNoCache(true)
            }
        }


        private fun parseAnno(clz: Class<*>, ele: BeanElement, f: Field?) {
            var m: Method? = null
            try {
                m = clz.getDeclaredMethod(ele.getGetter())
            } catch (e: NoSuchMethodException) {
            }
            if (m != null) {
                val p: X = m.getAnnotation(X::class.java)
                if (p != null) {
                    ele.setLength(p.length())
                }
            }

            if (f != null) {
                val p: X = f.getAnnotation(X::class.java)
                if (p != null) {
                    ele.setLength(p.length())
                }

                val mapping: X.Mapping = f.getAnnotation(X.Mapping::class.java)
                if (mapping != null && SqlStringUtil.isNotNull(mapping.value())) {
                    ele.setMapper(mapping.value())
                }
            }
        }

        fun parseKey(parsed: Parsed, clz: Class<*>) {
            val list: MutableList<Field> = ArrayList<Field>()

            try {
                list.addAll(Arrays.asList(*clz.declaredFields))
                val sc = clz.superclass
                if (sc != Any::class.java) {
                    list.addAll(Arrays.asList(*sc.declaredFields))
                }
            } catch (e: Exception) {
            }

            for (f in list) {
                val a: X.Key = f.getAnnotation(X.Key::class.java)
                if (a != null) {
                    f.setAccessible(true)
                    parsed.setKeyField(f)
                    break
                } else {
                    for (anno in f.getAnnotations()) {
                        val annoName: String = anno.annotationType().getName()
                        if (annoName.endsWith(".Id") || annoName.endsWith(".ID") || annoName.endsWith(".TableId")) {
                            f.setAccessible(true)
                            parsed.setKeyField(f)
                            break
                        }
                    }
                    if (SqlStringUtil.isNotNull(parsed.getKey())) {
                        break
                    }
                }
            }
        }

        fun parseTagAndSub(parsed: Parsed, clz: Class<*>) {
            val list: MutableList<Field> = ArrayList<Field>()

            try {
                list.addAll(Arrays.asList(*clz.declaredFields))
                val sc = clz.superclass
                if (sc != Any::class.java) {
                    list.addAll(Arrays.asList(*sc.declaredFields))
                }
            } catch (e: Exception) {
            }

            for (f in list) {
                val t: X.Tag = f.getAnnotation(X.Tag::class.java)
                if (t != null) {
                    f.setAccessible(true)
                    parsed.getTagFieldList().add(f)
                }
                val tt: X.TagTarget = f.getAnnotation(X.TagTarget::class.java)
                if (tt != null) {
                    f.setAccessible(true)
                    if (parsed.getTagKeyField() != null) throw ParsingException("find another annotation: X.TagTarget, class: $clz")
                    parsed.setTagKeyField(f)
                }
            }
        }

        fun filterSQLKeyword(mapper: String): String {
            for (keyWord in SqlScript.KEYWORDS) {
                if (keyWord.equals(mapper, ignoreCase = true)) {
                    return SQL_KEYWORD_MARK + mapper + SQL_KEYWORD_MARK
                }
            }
            return mapper
        }

        fun getClzName(alia: String?, aliaMap: Map<String?, String?>?): String? {
            if (aliaMap == null) throw ParsingException("QB.of(Class) not support the key contains '.'")
            val a = aliaMap[alia]
            if (SqlStringUtil.isNotNull(a)) return a
            return alia
        }

        fun <T> tryToGetId(t: T, parsed: Parsed): Any {
            val f: Field? = parsed.getKeyField()
            var id: Any? = null
            try {
                id = f.get(t)
            } catch (e: Exception) {
            }
            requireNotNull(id) { "obj keyOne = $id, $t" }
            return id
        }

        fun getCacheKey(obj: Any, parsed: Parsed): String? {
            try {
                val keyOneObj = tryToGetId(obj, parsed)
                if (keyOneObj != null) return keyOneObj.toString()
            } catch (e: Exception) {
            }
            return null
        }


        fun getMapper(property: String): String {
            val AZ = "AZ"
            val min = AZ[0].code - 1
            val max = AZ[1].code + 1

            try {
                val spec: String = Parser.mappingSpec
                if (SqlStringUtil.isNotNull(spec)) {
                    val arr = property.toCharArray()
                    val length = arr.size
                    val list: MutableList<String> = ArrayList()
                    var temp = StringBuilder()
                    for (i in 0 until length) {
                        val c = arr[i]
                        if (c.code > min && c.code < max) {
                            val ts = temp.toString()
                            if (SqlStringUtil.isNotNull(ts)) {
                                list.add(temp.toString())
                            }
                            temp = StringBuilder()
                            val s = c.toString()
                            temp.append(s.lowercase(Locale.getDefault()))
                        } else {
                            temp = temp.append(c)
                        }

                        if (i == length - 1) {
                            list.add(temp.toString())
                        }
                    }

                    var str = ""

                    val size = list.size
                    for (i in 0 until size) {
                        val s = list[i]
                        str += s
                        if (i < size - 1) {
                            str += "_"
                        }
                    }
                    return str
                }
            } catch (e: Exception) {
            }
            return property
        }
    }
}