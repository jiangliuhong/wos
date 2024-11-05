package top.jiangliuhong.wos.dao.def

import com.alibaba.fastjson.JSON
import jakarta.persistence.*
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.annotation.AnnotationDescription
import net.bytebuddy.description.modifier.Visibility
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.FieldAccessor
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import org.springframework.core.io.ClassPathResource
import org.springframework.data.jpa.repository.JpaRepository
import java.lang.reflect.Modifier
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.reflect.KClass

class DefinitionLoader {

    private val baseEntityPath = "entity"

    fun loadByResources(): List<KClass<*>> {
        val list = mutableListOf<KClass<*>>()
        val entityLists = listFilesInResourceDirectory(baseEntityPath)
        entityLists.forEach { entityFile ->
            val definitions = loadTableDefinitions(entityFile)
            definitions.forEach { def ->
                val dynamicEntityClass = createDynamicEntityClass(def)
                list.add(dynamicEntityClass)
            }
        }
        return list
    }

    fun createDynamicEntityClass(definition: TableDefinition): KClass<*> {
        val buddy = ByteBuddy()
        return buddy.subclass(Any::class.java)
            .name("top.jiangliuhong.wos.dao.entity.MyTestUser")
            .annotateType(AnnotationDescription.Builder.ofType(Entity::class.java).build()) // 添加 @Entity 注解
            .defineField("id", Long::class.javaObjectType, Modifier.PRIVATE) // 定义 id 字段
            .annotateField(AnnotationDescription.Builder.ofType(Id::class.java).build()) // 添加 @Id 注解
            .annotateField(
                AnnotationDescription.Builder.ofType(GeneratedValue::class.java)
                    .define("strategy", GenerationType.IDENTITY)
                    .build()
            )
            .defineField("name", String::class.java, Modifier.PRIVATE) // 定义 name 字段
            .defineField("email", String::class.java, Modifier.PRIVATE) // 定义 email 字段
//            .defineConstructor(Visibility.PUBLIC)
//            .withParameters(String::class.java, String::class.java) // 添加构造函数
//            .intercept(
//                FieldAccessor.ofField("name").setsDefaultValue().andThen(
//                    FieldAccessor.ofField("email").setsDefaultValue()
//                )
//            )
            .make()
            .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION)
            .loaded.kotlin
    }

    fun createDynamicEntityClass2(definition: TableDefinition): KClass<*> {
        val entityName = "${definition.tableName.capitalize()}Entity"
        val entityAnnotation = AnnotationDescription.Builder.ofType(Entity::class.java).build()
        val columnAnnotation = AnnotationDescription.Builder.ofType(Column::class.java).build()

        var entityBuilder = ByteBuddy()
            .subclass(Any::class.java)
            .name("top.jiangliuhong.wos.dao.entity.$entityName")
            .annotateType(entityAnnotation)
        // 为每个字段添加属性和注解
        definition.columns.forEach { column ->
            var fieldBuilder = entityBuilder
                .defineField(column.name, column.javaClass, net.bytebuddy.description.modifier.Visibility.PRIVATE)
                .annotateField(columnAnnotation)

            // 如果是主键，添加 @Id 和 @GeneratedValue 注解
            if (column.primaryKey) {
                fieldBuilder = fieldBuilder
                    .annotateField(AnnotationDescription.Builder.ofType(Id::class.java).build())
                    .annotateField(
                        AnnotationDescription.Builder.ofType(GeneratedValue::class.java)
                            .define("strategy", GenerationType.IDENTITY)
                            .build()
                    )
            }
            entityBuilder = fieldBuilder
        }
        val loadedClass = entityBuilder.make()
            .load(javaClass.classLoader, ClassLoadingStrategy.Default.INJECTION)
            .loaded
        return loadedClass.kotlin
    }

    fun loadTableDefinitions(filePath: String): List<TableDefinition> {
        val content = loadFileFromResources(filePath)
        return JSON.parseArray(content, TableDefinition::class.java)
    }

    fun loadTableDefinitions(filePath: Path): List<TableDefinition> {
        val content = Files.readString(filePath)
        return JSON.parseArray(content, TableDefinition::class.java)
    }

    fun loadFileFromResources(filePath: String): String {
        val resource = ClassPathResource(filePath)
        val path = Paths.get(resource.uri)
        return Files.readString(path)
    }

    fun listFilesInResourceDirectory(directory: String): List<Path> {
        val resource = ClassPathResource(directory)
        val directoryPath = Paths.get(resource.uri)

        return Files.walk(directoryPath, 1) // 1 表示只遍历当前目录，不递归子目录
            .filter { it.isRegularFile() } // 只选择文件，忽略目录
            .toList()
    }
}