package userservice.domain.service

import userservice.domain.entity.User
import userservice.domain.model.SignUpRequest
import userservice.domain.repository.UserRepository
import userservice.exception.UserExistsException
import userservice.utils.BCryptUtils

class UserService(
        private val userRepository: UserRepository
) {
    suspend fun signUp(signUpRequest: SignUpRequest) {
        with(signUpRequest) {
            userRepository.findByEmail(this.email)?. let {
                throw UserExistsException()
            }
            val user = User(
                    email = email,
                    password = BCryptUtils.hash(password),
                    username = username,
            )
            userRepository.save(user)
        }
    }
}