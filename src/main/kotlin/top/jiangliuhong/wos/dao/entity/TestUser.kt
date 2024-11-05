package top.jiangliuhong.wos.dao.entity

import jakarta.persistence.*

@Entity
data class TestUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String,
    var email: String
)
