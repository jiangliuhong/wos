package top.jiangliuhong.wos.sql.consts


object  Operates {
    val operates: Set<String> = hashSetOf<String>().apply {
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

    fun contains(name: String) = operates.contains(name)

}