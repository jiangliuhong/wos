package top.jiangliuhong.wos.repository

import org.springframework.data.jpa.repository.JpaRepository
import top.jiangliuhong.wos.entity.User

interface UserRepository : JpaRepository<User, Long> {
}