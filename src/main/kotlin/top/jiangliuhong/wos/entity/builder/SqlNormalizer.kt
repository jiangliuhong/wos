package top.jiangliuhong.wos.entity.builder

import top.jiangliuhong.wos.entity.builder.consts.ScriptConsts


open class SqlNormalizer : Script {

    var operates: Set<String> = hashSetOf<String>().apply {
        add("=")
        add("!")
        add(">")
        add("<")
        add("+")
        add("-")
        add("*")
        add("/")
        add(";")
        add(":")
    }

    fun normalizeFunctionParentThesis(idx: Int, strEle: String, valueSB: StringBuilder, handwritten: String) {
        if (strEle.equals(ScriptConsts.LEFT_PARENTTHESIS) && idx - 1 > -1) {
            val pre: String = java.lang.String.valueOf(handwritten[idx - 1])
            if (pre == ScriptConsts.SPACE || operates.contains(pre)) {
                valueSB.append(ScriptConsts.SPACE)
            }
        } else {
            valueSB.append(ScriptConsts.SPACE)
        }
    }

    fun normalizeParentThesis(idx: Int, length: Int, strEle: String, valueSB: StringBuilder, handwritten: String): Int {
        normalizeFunctionParentThesis(idx, strEle, valueSB, handwritten)
        valueSB.append(strEle);
        var idx_ = idx
        while (idx_ + 1 < length) {
            val nextOp: String = java.lang.String.valueOf(handwritten[idx_ + 1])
            if (nextOp == strEle) {
                valueSB.append(ScriptConsts.SPACE).append(nextOp)
                idx_++
            } else {
                break
            }
        }
        valueSB.append(ScriptConsts.SPACE)
        return idx_
    }

    fun normalizeOp(idx: Int, length: Int, strEle: String, valueSB: StringBuilder, handwritten: String): Int {
        var idx_ = idx
        valueSB.append(ScriptConsts.SPACE).append(strEle)
        if (idx_ + 1 < length) {
            val nextOp: String = java.lang.String.valueOf(handwritten[idx_ + 1])
            if (operates.contains(nextOp)) {
                valueSB.append(nextOp)
                idx_++
            }
        }
        return idx_
    }

    fun normalizeSql(handwritten: String): String {
        val valueSB = StringBuilder()
        var ignored = false
        val length = handwritten.length
        var i = 0
        while (i < length) {
            val strEle = handwritten[i].toString()
            if (ScriptConsts.SPACE == strEle) {
                ignored = true
                i++
                continue
            }
            if (strEle == ScriptConsts.LEFT_PARENTTHESIS || strEle == ScriptConsts.RIGHT_PARENTTHESIS) {
                val pre = handwritten[i - 1].toString()
                if (pre == ScriptConsts.LEFT_PARENTTHESIS || pre == ScriptConsts.RIGHT_PARENTTHESIS) {
                    valueSB.append(ScriptConsts.SPACE).append(strEle)
                    i++
                    continue
                }
                i = normalizeParentThesis(i, length, strEle, valueSB, handwritten)
                continue
            }

            if (operates.contains(strEle)) {
                i = normalizeOp(i, length, strEle, valueSB, handwritten)
            } else {
                if (ignored) {
                    valueSB.append(ScriptConsts.SPACE)
                }
                valueSB.append(strEle)
                ignored = false
            }
            i++
        }
        return valueSB.toString()
    }
}