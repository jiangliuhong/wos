package top.jiangliuhong.wos.sql.mapping

interface Mappable {

    fun getParsed(): Parsed?
    fun getAliaMap(): Map<String?, String?>?
    fun getResultKeyAliaMap(): Map<String?, String?>?
}