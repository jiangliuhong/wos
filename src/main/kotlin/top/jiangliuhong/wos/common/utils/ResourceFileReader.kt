package top.jiangliuhong.wos.common.utils

import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isRegularFile

/**
 * 资源文件加载工具类
 */
class ResourceFileReader(private val baseResourcePath: String) {

    fun load(handler: (String) -> Unit) {
        val resource = ClassPathResource(baseResourcePath)
        val directoryPath = Paths.get(resource.uri)
        val fileList = Files.walk(directoryPath, 1) // 1 表示只遍历当前目录，不递归子目录
            .filter { it.isRegularFile() } // 只选择文件，忽略目录
            .toList()
        fileList.forEach { file ->
            val content = Files.readString(file)
            handler(content)
        }

    }
}