package userservice.domain.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import userservice.domain.entity.User

interface UserRepository: CoroutineCrudRepository<User, Long> {

    suspend fun findByEmail(email: String) : User?
}