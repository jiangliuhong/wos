package top.jiangliuhong.wos.entity

import com.alibaba.fastjson.JSON
import top.jiangliuhong.wos.common.utils.ResourceFileReader
import top.jiangliuhong.wos.entity.def.EntityDef

class EntityLoader {

    fun loadAllResourceDef(): List<EntityDef> {
        val defList = mutableListOf<EntityDef>()
        ResourceFileReader("entity").load { content ->
            val list = JSON.parseArray(content, EntityDef::class.java)
            defList.addAll(list)
        }
        return defList
    }
}